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

public class BorrowAction extends ActionSupport{
    private BackService backService;

    public void setBackService(BackService backService) {
        this.backService = backService;
    }

    private int borrowId;
    private String ISBN;
    private int pageCode;

    public void setBorrowId(int borrowId) {
        this.borrowId = borrowId;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public void setPageCode(int pageCode) {
        this.pageCode = pageCode;
    }

    public String findMyBorrowInfoByPage() {
        Reader reader = (Reader) ServletActionContext.getContext().getSession().get("reader");
        if (pageCode == 0) {
            pageCode = 1;
        }
        int pageSize = 5;
        PageBean<BackInfo> pageBean = null;
        pageBean = backService.findMyBorrowInfoByPage(reader, pageCode, pageSize);
        if (pageBean != null) {
            pageBean.setUrl("findMyBorrowInfoByPage.action?");
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

    public String queryBorrowSearchInfo() {
        if (pageCode == 0) {
            pageCode = 1;
        }
        int pageSize = 5;
        PageBean<BackInfo> pageBean = null;
        Reader reader = (Reader) ServletActionContext.getContext().getSession().get("reader");
        if ("".equals(ISBN.trim()) && borrowId == 0) {
            pageBean = backService.findMyBorrowInfoByPage(reader, pageCode, pageSize);
        } else {
            pageBean = backService.queryBackInfo(ISBN, reader.getCardId(), borrowId, pageCode, pageSize);
        }
        if (pageBean != null) {
            pageBean.setUrl("queryBorrowSearchInfo.action?ISBN=" + ISBN + "&borrowId=" + borrowId + "&");
        }
        ServletActionContext.getRequest().setAttribute("pb", pageBean);
        return "success";
    }

}
