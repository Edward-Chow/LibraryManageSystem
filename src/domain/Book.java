package domain;

import java.io.Serializable;
import java.util.Date;

public class Book implements Serializable{

    private Integer bookId;
    private String ISBN;
    private String bookName;
    private BookType bookType;
    private String auth; //作者
    private String press; //出版社
    private Integer totalNum;
    private Date onDate; //上架日期
    private Integer numInLibrary;
    private Double price;
    private String description;
    private Admin admin; //操作这本书的管理员

    public Book(String ISBN, String bookName, BookType bookType, String auth, String press, Integer totalNum, Date onDate, Integer numInLibrary, Double price, String description, Admin admin) {
        super();
        this.ISBN = ISBN;
        this.bookName = bookName;
        this.bookType = bookType;
        this.auth = auth;
        this.press = press;
        this.totalNum = totalNum;
        this.onDate = onDate;
        this.numInLibrary = numInLibrary;
        this.price = price;
        this.description = description;
        this.admin = admin;
    }

    public Book(String ISBN, String bookName, BookType bookType, String auth, String press) {
        this.ISBN = ISBN;
        this.bookName = bookName;
        this.bookType = bookType;
        this.auth = auth;
        this.press = press;
    }

    public Book() {
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public BookType getBookType() {
        return bookType;
    }

    public void setBookType(BookType bookType) {
        this.bookType = bookType;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Date getOnDate() {
        return onDate;
    }

    public void setOnDate(Date onDate) {
        this.onDate = onDate;
    }

    public Integer getNumInLibrary() {
        return numInLibrary;
    }

    public void setNumInLibrary(Integer numInLibrary) {
        this.numInLibrary = numInLibrary;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}
