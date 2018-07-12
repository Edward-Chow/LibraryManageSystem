package action;

import com.opensymphony.xwork2.ActionSupport;
import domain.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;
import org.apache.struts2.ServletActionContext;
import service.BookService;
import service.BookTypeService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class BookManageAction extends ActionSupport{
    private BookService bookService;
    private BookTypeService bookTypeService;

    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    public void setBookTypeService(BookTypeService bookTypeService) {
        this.bookTypeService = bookTypeService;
    }

    private Integer bookTypeId;
    private String bookName;
    private String auth;
    private String press;
    private Integer totalNum;
    private Double price;
    private String description;
    private int bookId;
    private String ISBN;

    private int pageCode;

    private int num; //添加图书数量

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

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public void setPageCode(int pageCode) {
        this.pageCode = pageCode;
    }

    public void setNum(int num) {
        this.num = num;
    }


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

    public String addBook() {
        BookType bookType = new BookType();
        bookType.setTypeId(bookTypeId);
        Date onDate = new Date(System.currentTimeMillis());
        Admin admin  = (Admin)ServletActionContext.getContext().getSession().get("admin");
        Book book = new Book(ISBN, bookName, bookType, auth, press, totalNum, onDate, totalNum, price, description, admin);
        int success = 0;
        if (bookService.addBook(book)) {
            success = 1;
        } else {
            success = 0;
        }
        try {
            ServletActionContext.getResponse().getWriter().print(success);
        }catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

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

    public String updateBook() {
        Book book = new Book();
        book.setBookId(bookId);
        Book updateBook = bookService.getBookById(book);
        updateBook.setBookName(bookName);
        updateBook.setAuth(auth);
        updateBook.setISBN(ISBN);
        updateBook.setDescription(description);
        updateBook.setPress(press);
        updateBook.setPrice(price);
        BookType bookType = new BookType();
        bookType.setTypeId(bookTypeId);
        updateBook.setBookType(bookType);
        Book newBook = bookService.updateBookInfo(updateBook);
        int success = 0;
        if (newBook != null) {
            success = 1;
        }
        try {
            ServletActionContext.getResponse().getWriter().print(success);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    public String deleteBook() {
        Book book = new Book();
        book.setBookId(bookId);
        int success = bookService.deleteBook(book);
        try {
            ServletActionContext.getResponse().getWriter().print(success);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    public String addBookNum() {
        Book book = new Book();
        book.setBookId(bookId);
        Book updateBook = bookService.getBookById(book);
        updateBook.setNumInLibrary(updateBook.getNumInLibrary() + num);
        updateBook.setTotalNum(updateBook.getTotalNum() + num);
        Book newBook = bookService.updateBookInfo(updateBook);
        int success = 0;
        if (newBook != null) {
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
