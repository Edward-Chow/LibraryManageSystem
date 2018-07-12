package action;

import com.opensymphony.xwork2.ActionSupport;
import domain.*;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;
import org.apache.struts2.ServletActionContext;
import service.BackService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class BackManageAction extends ActionSupport{
    private BackService backService;

    public void setBackService(BackService backService) {
        this.backService = backService;
    }

    private int pageCode;
    private int borrowId;
    private String ISBN;
    private String cardId;

    public void setPageCode(int pageCode) {
        this.pageCode = pageCode;
    }

    public void setBorrowId(int borrowId) {
        this.borrowId = borrowId;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String findBackInfoByPage() {
        //获取页面传递过来的当前页码数
        if(pageCode==0){
            pageCode = 1;
        }
        //给pageSize,每页的记录数赋值
        int pageSize = 5;

        PageBean<BackInfo> pb = backService.findBackInfoByPage(pageCode,pageSize);
        if(pb!=null){
            pb.setUrl("findBackInfoByPage.action?");
        }
        //存入request域中
        ServletActionContext.getRequest().setAttribute("pb", pb);
        return  "success";
    }

    public String queryBackInfo() {
        if (pageCode == 0) {
            pageCode = 1;
        }
        int pageSize = 5;
        PageBean<BackInfo> pageBean = null;
        if ("".equals(ISBN.trim()) && "".equals(cardId.trim())  && borrowId == 0) {
            pageBean = backService.findBackInfoByPage(pageCode, pageSize);
        } else {
            pageBean = backService.queryBackInfo(ISBN, cardId, borrowId, pageCode, pageSize);
        }
        if(pageBean!=null){
            pageBean.setUrl("queryBackInfo.action?ISBN="+ISBN+"&cardId="+cardId+"&borrowId="+borrowId+"&");
        }
        ServletActionContext.getRequest().setAttribute("pb", pageBean);
        return "success";
    }

    public String getBackInfoById() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        BackInfo backInfo = new BackInfo();
        backInfo.setBorrowId(borrowId);
        BackInfo newBackInfo = backService.getBackInfoById(backInfo);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
            public boolean apply(Object obj, String name, Object value) {
                if(obj instanceof Authorization ||name.equals("authorization") || obj instanceof Set || name.equals("borrowInfos")){
                    return true;
                }else{
                    return false;
                }
            }
        });


        JSONObject jsonObject = JSONObject.fromObject(newBackInfo,jsonConfig);
        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    public String backBook() {
        BackInfo backInfo = new BackInfo();
        backInfo.setBorrowId(borrowId);
        backInfo.setAdmin((Admin)ServletActionContext.getContext().getSession().get("admin"));
        BorrowInfo borrowInfo = new BorrowInfo();
        borrowInfo.setBorrowId(borrowId);
        backInfo.setBorrowInfo(borrowInfo);
        try {
            ServletActionContext.getResponse().getWriter().print(backService.addBackInfo(backInfo));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }
}
