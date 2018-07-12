package domain;

import java.io.Serializable;
import java.util.Date;

public class BorrowInfo implements Serializable {

    private Integer borrowId;	//借阅编号
    private Book book;	//借阅书籍
    private Reader reader;	//借阅读者
    private Date borrowDate;	//借阅日期
    private Admin admin;	//操作员
    private Date returnDate;	//截止日期
    private Double penalty;	//每日罚金
    private Integer overday;	//逾期天数
    private Integer state; //状态 (未归还=0,逾期未归还=1,归还=2,续借未归还=3,续借逾期未归还=4,续借归还=5)

    public BorrowInfo() {
    }

    public Integer getBorrowId() {

        return borrowId;
    }

    public void setBorrowId(Integer borrowId) {
        this.borrowId = borrowId;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Double getPenalty() {
        return penalty;
    }

    public void setPenalty(Double penalty) {
        this.penalty = penalty;
    }

    public Integer getOverday() {
        return overday;
    }

    public void setOverday(Integer overday) {
        this.overday = overday;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
