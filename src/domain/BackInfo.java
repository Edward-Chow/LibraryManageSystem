package domain;

import java.io.Serializable;
import java.util.Date;

public class BackInfo implements Serializable{

    private Integer borrowId;
    private BorrowInfo borrowInfo;
    private Admin admin;
    private Date backDay;

    public Integer getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Integer borrowId) {
        this.borrowId = borrowId;
    }

    public BorrowInfo getBorrowInfo() {
        return borrowInfo;
    }

    public void setBorrowInfo(BorrowInfo borrowInfo) {
        this.borrowInfo = borrowInfo;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Date getBackDay() {
        return backDay;
    }

    public void setBackDay(Date backDay) {
        this.backDay = backDay;
    }

    public BackInfo() {

    }
}
