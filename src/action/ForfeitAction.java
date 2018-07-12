package action;

import com.opensymphony.xwork2.ActionSupport;
import domain.Authorization;
import domain.ForfeitInfo;
import domain.PageBean;
import domain.Reader;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;
import org.apache.struts2.ServletActionContext;
import service.ForfeitService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class ForfeitAction extends ActionSupport {
    private ForfeitService forfeitService;

    public void setForfeitService(ForfeitService forfeitService) {
        this.forfeitService = forfeitService;
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

    public String findMyForfeitInfoByPage() {
        if (pageCode == 0) {
            pageCode = 1;
        }
        int pageSize = 5;
        Reader reader = (Reader) ServletActionContext.getContext().getSession().get("reader");
        PageBean<ForfeitInfo> pageBean = null;
        pageBean = forfeitService.findMyForfeitInfoByPage(reader, pageCode, pageSize);
        if (pageBean != null) {
            pageBean.setUrl("findMyForfeitInfoByPage.action?");
        }
        ServletActionContext.getRequest().setAttribute("pb", pageBean);
        return "success";
    }

    public String queryForfeInfo() {
        Reader reader = (Reader) ServletActionContext.getContext().getSession().get("reader");
        if (pageCode == 0) {
            pageCode = 1;
        }
        int pageSize = 5;
        PageBean<ForfeitInfo> pageBean = null;
        if ("".equals(ISBN.trim()) && borrowId == 0) {
            pageBean = forfeitService.findMyForfeitInfoByPage(reader, pageCode, pageSize);
        } else {
            pageBean = forfeitService.queryForfeitInfo(ISBN, reader.getCardId(), borrowId, pageCode, pageSize);
        }
        if (pageBean != null) {
            pageBean.setUrl("queryForfeInfo.action?ISBN=" + ISBN + "&borrowId=" + borrowId + "&");
        }
        ServletActionContext.getRequest().setAttribute("pb", pageBean);
        return "success";
    }

    public String getForfeitInfoById() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        ForfeitInfo forfeitInfo = new ForfeitInfo();
        forfeitInfo.setBorrowId(borrowId);
        ForfeitInfo newforfeitinfo = forfeitService.getForfeitInfoById(forfeitInfo);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
            @Override
            public boolean apply(Object o, String s, Object o1) {
                if (o instanceof Authorization || s.equals("authorization") || o instanceof Set || s.equals("borrowInfos")) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        JSONObject jsonObject = JSONObject.fromObject(newforfeitinfo,jsonConfig);
        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }
}
