package action;

import com.opensymphony.xwork2.ActionSupport;
import domain.Authorization;
import domain.Book;
import domain.BookType;
import domain.PageBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;
import org.apache.struts2.ServletActionContext;
import service.BookService;
import service.BookTypeService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("serial")
public class BookAction extends ActionSupport {
    private BookService bookService;
    private BookTypeService bookTypeService;

    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    public void setBookTypeService(BookTypeService bookTypeService) {
        this.bookTypeService = bookTypeService;
    }

    private Integer bookTypeId;	//图书类型
    private String bookName;	//图书名称
    private String auth;	//作者名称
    private String press;	//出版社
    private int bookId;     //图书编号
    private String ISBN;

    public void setBookTypeId(Integer bookTypeId) {
        this.bookTypeId = bookTypeId;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    private int pageCode;

    public void setPageCode(int pageCode) {
        this.pageCode = pageCode;
    }

    /**
     * 得到图书类型的集合
     * ajax请求该方法
     * 返回图书类型集合的json对象
     */
    public String getAllBookTypes() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        List<BookType> allBookTypes = bookTypeService.getAllBookTypes();
        String json = JSONArray.fromObject(allBookTypes).toString();
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    /**
     * 得到指定图书编号的图书信息
     * ajax请求该方法
     * 返回该图书信息的json对象
     */
    public String getBook() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        Book book = new Book();
        book.setBookId(bookId);
        Book newBook = bookService.getBookById(book);
        JsonConfig jsonConfig = new JsonConfig();

        jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
            public boolean apply(Object obj, String name, Object value) {
                if(obj instanceof Authorization ||name.equals("authorization")){
                    return true;
                }else{
                    return false;
                }
            }
        });

        JSONObject jsonObject = JSONObject.fromObject(newBook,jsonConfig);
        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    public String findBookByPage() {
        //获取页面传递过来的当前页码数
        if(pageCode==0){
            pageCode = 1;
        }
        //给pageSize,每页的记录数赋值
        int pageSize = 5;

        PageBean<Book> pb = bookService.findBookByPage(pageCode,pageSize);
        if(pb!=null){
            pb.setUrl("findBookByPage.action?");
        }
        //存入request域中
        ServletActionContext.getRequest().setAttribute("pb", pb);
        return  "success";
    }

    public String queryBook() {
        if (pageCode == 0) {
            pageCode = 1;
        }
        int pageSize = 5;
        PageBean<Book> pageBean = null;
        if ("".equals(bookName.trim()) && "".equals(auth.trim()) && "".equals(ISBN.trim()) && "".equals(press.trim()) && bookTypeId == -1) {
            pageBean = bookService.findBookByPage(pageCode, pageSize);
        } else {
            BookType bookType = new BookType();
            bookType.setTypeId(bookTypeId);
            Book book = new Book(ISBN, bookName, bookType, auth, press);
            pageBean = bookService.queryBook(book, pageCode, pageSize);
        }
        if(pageBean!=null){
            pageBean.setUrl("queryBook.action?ISBN="+ISBN+"&bookName="+bookName+"&bookTypeId="+bookTypeId+"&press="+press+"&auth="+auth+"&");
        }
        ServletActionContext.getRequest().setAttribute("pb", pageBean);
        return "success";

    }

}
