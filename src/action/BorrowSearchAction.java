package action;

import com.opensymphony.xwork2.ActionSupport;
import domain.BackInfo;
import domain.BorrowInfo;
import domain.PageBean;
import org.apache.struts2.ServletActionContext;
import service.BackService;
import service.BorrowService;

public class BorrowSearchAction extends ActionSupport {
    private BackService backService;

    public void setBackService(BackService backService) {
        this.backService = backService;
    }

    private int pageCode;

    public void setPageCode(int pageCode) {
        this.pageCode = pageCode;
    }

    private String ISBN;
    private String cardId;
    private int borrowId;

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public void setBorrowId(int borrowId) {
        this.borrowId = borrowId;
    }

    public String findBackInfoByPage() {
        if (pageCode == 0) {
            pageCode = 1;
        }
        int pageSize = 5;
        PageBean<BackInfo> pageBean = backService.findBackInfoByPage(pageCode, pageSize);
        if (pageBean != null) {
            pageBean.setUrl("findBackInfoByPage.action?");
        }
        ServletActionContext.getRequest().setAttribute("pb", pageBean);
        return "success";
    }

    public String queryBorrowSearchInfo() {
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
            pageBean.setUrl("queryBorrowSearchInfo.action?ISBN="+ISBN+"&cardId="+cardId+"&borrowId="+borrowId+"&");
        }
        ServletActionContext.getRequest().setAttribute("pb", pageBean);
        return "success";
    }
}
