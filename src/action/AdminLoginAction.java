package action;

import com.opensymphony.xwork2.ActionSupport;
import domain.Admin;
import org.apache.struts2.ServletActionContext;
import service.AdminService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminLoginAction extends ActionSupport {
    private AdminService adminService;

    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    private String username;
    private String password;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String login() {
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(password);
        int login = 1;
        Admin newAdmin = adminService.getAdminByUserName(admin);
        if (newAdmin == null) {
            login = -1;
        } else if (!admin.getPassword().equals(newAdmin.getPassword())) {
            login = -2;
        } else {
            ServletActionContext.getContext().getSession().put("admin", newAdmin);
        }
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.getWriter().print(login);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    public String logout() {
        ServletActionContext.getContext().getSession().remove("admin");
        return "logout";
    }
}
