package action;

import com.opensymphony.xwork2.ActionSupport;
import domain.Admin;
import domain.Authorization;
import domain.PageBean;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;
import org.apache.struts2.ServletActionContext;
import service.AdminService;
import service.AuthorizationService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminManageAction extends ActionSupport {
    private AdminService adminService;
    private AuthorizationService authorizationService;

    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    public void setAuthorizationService(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    private int pageCode;
    private String adminUserName;
    private String adminName;
    private int id;
    private String username;
    private String name;
    private String pwd;
    private String phone;

    public void setPageCode(int pageCode) {
        this.pageCode = pageCode;
    }

    public void setAdminUserName(String adminUserName) {
        this.adminUserName = adminUserName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String findAdminByPage() {
        //获取页面传递过来的当前页码数
        if(pageCode==0){
            pageCode = 1;
        }
        //给pageSize,每页的记录数赋值
        int pageSize = 5;

        PageBean<Admin> pb = adminService.findAdminByPage(pageCode,pageSize);
        if(pb!=null){
            pb.setUrl("findAdminByPage.action?");
        }
        //存入request域中
        ServletActionContext.getRequest().setAttribute("pb", pb);
        return  "success";
    }

    public String queryAdmin() {
        if (pageCode == 0) {
            pageCode = 1;
        }
        int pageSize = 5;
        PageBean<Admin> pageBean = null;
        if (("".equals(adminName.trim()) || adminName == null) && ("".equals(adminUserName.trim()) || adminUserName == null)) {
            pageBean = adminService.findAdminByPage(pageCode, pageSize);
        } else {
            Admin admin = new Admin();
            admin.setUsername(adminUserName);
            admin.setName(adminName);
            pageBean = adminService.queryAdmin(admin, pageCode, pageSize);
        }
        if(pageBean!=null){
            pageBean.setUrl("queryAdmin.action?adminUserName="+adminUserName+"&adminName="+adminName+"&");
        }
        ServletActionContext.getRequest().setAttribute("pb", pageBean);
        return "success";
    }

    public String getAdmin() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        Admin admin = new Admin();
        admin.setAid(id);
        Admin newAdmin = adminService.getAdminById(admin);
        JsonConfig jsonConfig = new JsonConfig();

        jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
            public boolean apply(Object obj, String name, Object value) {
                if(name.equals("admin")){//过滤掉Authorization中的admin
                    return true;
                }else{
                    return false;
                }
            }
        });

        JSONObject jsonObject = JSONObject.fromObject(newAdmin,jsonConfig);
        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    public String updateAdmin() {
        Admin admin = new Admin();
        admin.setAid(id);
        Admin updateAdmin = adminService.getAdminById(admin);
        updateAdmin.setUsername(username);
        updateAdmin.setName(name);
        updateAdmin.setPhone(phone);
        Admin newAdmin = adminService.updateAdminInfo(updateAdmin);
        int success = 0;
        if (newAdmin != null) {
            success = 1;
        }
        try {
            ServletActionContext.getResponse().getWriter().print(success);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    public String addAdmin() {
        Admin admin = new Admin();
        admin.setUsername(username);
        Admin admin2 = adminService.getAdminByUserName(admin);//按照姓名查找管理员，查看用户名是否已经存在
        int success = 0;
        if(admin2!=null){
            success = -1;//已经存在该管理员
        }else{
            admin.setName(name);
            admin.setPhone(phone);
            admin.setPassword(pwd);
            Authorization authorization = new Authorization();
            authorization.setAdmin(admin);
            admin.setAuthorization(authorization);//设置权限
            boolean b = adminService.addAdmin(admin);//添加管理员,返回是否添加成功
            if(b){
                success = 1;
            }else{
                success = 0;

            }
        }
        try {
            ServletActionContext.getResponse().getWriter().print(success);//向浏览器响应是否成功的状态码
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    public String deleteAdmin() {
        Admin admin = new Admin();
        admin.setAid(id);
        int success = 0;
        if(adminService.deleteAdmin(admin)){
            success = 1;
        }
        try {
            ServletActionContext.getResponse().getWriter().print(success);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

}
