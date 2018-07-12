package action;

import com.opensymphony.xwork2.ActionSupport;
import domain.Admin;
import org.apache.struts2.ServletActionContext;
import service.AdminService;

import java.io.IOException;

public class AdminInfoAction extends ActionSupport {
    private AdminService adminService;

    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    private String username;
    private String name;
    private String phone;
    private String oldPwd;
    private String newPwd;
    private String confirmPwd;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public void setConfirmPwd(String confirmPwd) {
        this.confirmPwd = confirmPwd;
    }

    public String adminInfo() {
        Admin admin = (Admin) ServletActionContext.getContext().getSession().get("admin");
        admin.setUsername(username);
        admin.setName(name);
        admin.setPhone(phone);
        Admin newAdmin = adminService.updateAdminInfo(admin);
        int success = 0;
        if (newAdmin != null) {
            success = 1;
            ServletActionContext.getContext().getSession().put("admin", newAdmin);
        }
        try {
            ServletActionContext.getResponse().getWriter().print(success);
        }catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    public String adminPwd() {
        Admin admin = (Admin)ServletActionContext.getContext().getSession().get("admin");
        int state = -1;
        if (admin.getPassword().equals(oldPwd)) {
            if (newPwd.equals(confirmPwd)) {
                state = 1;
                admin.setPassword(newPwd);
                admin = adminService.updateAdminInfo(admin);
                ServletActionContext.getContext().getSession().put("admin", admin);
            } else {
                state = 0;
            }
        }
        try {
            ServletActionContext.getResponse().getWriter().print(state);
        }catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

}
