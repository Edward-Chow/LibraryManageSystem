package domain;

import java.io.Serializable;

public class ForfeitInfo implements Serializable {

    private Integer borrowId;
    private BorrowInfo borrowInfo;
    private Admin admin;
    private Double forfeit; //罚金总金额
    private int isPay; //是否支付罚金

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

    public Double getForfeit() {
        return forfeit;
    }

    public void setForfeit(Double forfeit) {
        this.forfeit = forfeit;
    }

    public int getIsPay() {
        return isPay;
    }

    public void setIsPay(int isPay) {
        this.isPay = isPay;
    }

    public ForfeitInfo() {

    }
}
