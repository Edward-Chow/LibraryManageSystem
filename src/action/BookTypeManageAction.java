package action;

import com.opensymphony.xwork2.ActionSupport;
import domain.BookType;
import domain.PageBean;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;
import org.apache.struts2.ServletActionContext;
import service.BookTypeService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class BookTypeManageAction extends ActionSupport {
    private BookTypeService bookTypeService;

    public void setBookTypeService(BookTypeService bookTypeService) {
        this.bookTypeService = bookTypeService;
    }

    private int pageCode;
    private String typeName;
    private int id;

    public void setPageCode(int pageCode) {
        this.pageCode = pageCode;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String findBookTypeByPage() {
        if (pageCode == 0) {
            pageCode = 1;
        }
        int pageSize = 5;
        PageBean<BookType> pageBean = bookTypeService.findBookTypeByPage(pageCode, pageSize);
        if (pageBean != null) {
            pageBean.setUrl("findBookTypeByPage.action?");
        }
        ServletActionContext.getRequest().setAttribute("pb", pageBean);
        return "success";
    }

    public String addBookType() {
        BookType bookType = new BookType();
        bookType.setTypeName(typeName);
        BookType bookType1 = bookTypeService.getBookTypeByName(bookType);
        int success = 0;
        if (bookType1 != null) {
            success = -1;
        } else {
            if (bookTypeService.addBookType(bookType)) {
                success = 1;
            } else {
                success = 0;
            }
        }
        try {
            ServletActionContext.getResponse().getWriter().print(success);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    public String queryBookType() {
        if (pageCode == 0) {
            pageCode = 1;
        }
        int pageSize = 5;
        PageBean<BookType> pageBean = null;
        if ("".equals(typeName.trim())) {
            pageBean = bookTypeService.findBookTypeByPage(pageCode, pageSize);
        } else {
            BookType bookType = new BookType();
            bookType.setTypeName(typeName);
            pageBean = bookTypeService.queryBookType(bookType, pageCode, pageSize);
        }
        if (pageBean != null) {
            pageBean.setUrl("queryBookType?typeName=" + typeName + "&");
        }
        ServletActionContext.getRequest().setAttribute("pb", pageBean);
        return "success";
    }

    public String deleteBookType() {
        BookType bookType = new BookType();
        bookType.setTypeId(id);
        int success = 0;
        if (bookTypeService.deleteBookType(bookType)) {
            success = 1;
        }
        try {
            ServletActionContext.getResponse().getWriter().print(success);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    public String getBookType() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        BookType bookType = new BookType();
        bookType.setTypeId(id);
        BookType newBookType = bookTypeService.getBookTypeById(bookType);
        JsonConfig jsonConfig = new JsonConfig();

        jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
            public boolean apply(Object obj, String name, Object value) {
                if(obj instanceof Set ||name.equals("books")){
                    return true;
                }else{
                    return false;
                }
            }
        });

        JSONObject jsonObject = JSONObject.fromObject(newBookType,jsonConfig);
        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


    public String updateBookType(){
        BookType bookType = new BookType();
        bookType.setTypeId(id);
        BookType updateBookType = bookTypeService.getBookTypeById(bookType);//得到要修改的图书类型对象
        updateBookType.setTypeName(typeName);//重新设置名称
        BookType newBookType = bookTypeService.updateBookTypeInfo(updateBookType);//更新该图书类型对象
        int success = 0;
        if(newBookType!=null){
            success = 1;
            //由于是转发并且js页面刷新,所以无需重查
        }
        try {
            ServletActionContext.getResponse().getWriter().print(success);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }
}
