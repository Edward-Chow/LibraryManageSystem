package action;

import com.opensymphony.xwork2.ActionSupport;
import domain.*;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;
import org.apache.struts2.ServletActionContext;
import service.ForfeitService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;


public class ForfeitManageAction extends ActionSupport {
    private ForfeitService forfeitService;

    public void setForfeitService(ForfeitService forfeitService) {
        this.forfeitService = forfeitService;
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

    public String getForfeitInfoById() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        ForfeitInfo forfeitInfo = new ForfeitInfo();
        forfeitInfo.setBorrowId(borrowId);
        ForfeitInfo  newForfeitInfo = forfeitService.getForfeitInfoById(forfeitInfo);
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


        JSONObject jsonObject = JSONObject.fromObject(newForfeitInfo,jsonConfig);
        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    public String findForfeitInfoByPage() {
        if (pageCode == 0) {
            pageCode = 1;
        }
        int pageSize = 5;
        PageBean<ForfeitInfo> pageBean = null;
        pageBean = forfeitService.findForfeitInfoByPage(pageCode, pageSize);
        if (pageBean != null) {
            pageBean.setUrl("findForfeitInfoByPage.action?");
        }
        ServletActionContext.getRequest().setAttribute("pb", pageBean);
        return "success";
    }

    public String queryForfeitInfo() {
        if (pageCode == 0) {
            pageCode = 1;
        }
        int pageSize = 5;
        PageBean<ForfeitInfo> pageBean = null;
        if ("".equals(ISBN.trim()) && "".equals(cardId.trim())  && borrowId == 0) {
            pageBean = forfeitService.findForfeitInfoByPage(pageCode, pageSize);
        } else {
            pageBean = forfeitService.queryForfeitInfo(ISBN, cardId, borrowId, pageCode, pageSize);
        }
        if(pageBean!=null){
            pageBean.setUrl("queryForfeitInfo.action?ISBN="+ISBN+"&cardId="+cardId+"&borrowId="+borrowId+"&");
        }
        ServletActionContext.getRequest().setAttribute("pb", pageBean);
        return "success";
    }

    public String payForfeit() {
        ForfeitInfo forfeitInfo = new ForfeitInfo();
        forfeitInfo.setBorrowId(borrowId);
        forfeitInfo.setAdmin((Admin) ServletActionContext.getContext().getSession().get("admin"));
        int pay = forfeitService.payForfeit(forfeitInfo);
        try {
            ServletActionContext.getResponse().getWriter().print(pay);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }
}
