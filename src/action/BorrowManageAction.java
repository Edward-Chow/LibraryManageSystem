package action;

import com.opensymphony.xwork2.ActionSupport;
import com.sun.org.glassfish.gmbal.AMXMBeanInterface;
import domain.*;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;
import org.apache.struts2.ServletActionContext;
import service.BorrowService;

import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class BorrowManageAction extends ActionSupport {
    private BorrowService borrowService;

    public void setBorrowService(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    private int pageCode;
    private int borrowId;
    private String cardId;
    private String ISBN;
    private String password;

    public void setPageCode(int pageCode) {
        this.pageCode = pageCode;
    }

    public void setBorrowId(int borrowId) {
        this.borrowId = borrowId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String findBorrowInfoByPage() {
        if (pageCode == 0) {
            pageCode = 1;
        }
        int pageSize = 5;
        PageBean<BorrowInfo> pageBean = borrowService.findBorrowInfoByPage(pageCode, pageSize);
        if (pageBean != null) {
            pageBean.setUrl("findBorrowInfoByPage.action?");
        }
        ServletActionContext.getRequest().setAttribute("pb", pageBean);
        return "success";
    }

    public String borrowBook() {
        BorrowInfo borrowInfo = new BorrowInfo();
        Reader reader = new Reader();
        reader.setCardId(cardId);
        reader.setPassword(password);
        borrowInfo.setReader(reader);
        Admin admin = (Admin) ServletActionContext.getContext().getSession().get("admin");
        borrowInfo.setAdmin(admin);
        Book book = new Book();
        book.setISBN(ISBN);
        borrowInfo.setBook(book);
        int newBI = borrowService.addBorrow(borrowInfo);
        try {
            ServletActionContext.getResponse().getWriter().print(newBI);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    public String getBorrowInfoById() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        BorrowInfo borrowInfo = new BorrowInfo();
        borrowInfo.setBorrowId(borrowId);
        BorrowInfo newborrowInfo = borrowService.getBorrowInfoById(borrowInfo);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
            @Override
            public boolean apply(Object obj, String name, Object value) {
                if(obj instanceof Authorization||name.equals("authorization") || obj instanceof Set || name.equals("borrowInfos")){
                    return true;
                }else{
                    return false;
                }
            }
        });
        JSONObject jsonObject = JSONObject.fromObject(newborrowInfo, jsonConfig);
        try {
            ServletActionContext.getResponse().getWriter().print(jsonObject);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    public String renewBook() {
        BorrowInfo borrowInfo = new BorrowInfo();
        borrowInfo.setBorrowId(borrowId);
        int renewBook = borrowService.renewBook(borrowInfo);
        try {
            ServletActionContext.getResponse().getWriter().print(renewBook);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }
}
