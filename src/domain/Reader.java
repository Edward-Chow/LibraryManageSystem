package domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class Reader implements Serializable {

    private Integer readerId;
    private String name;
    private String cardId; //证件号码
    private String password;
    private String phone;
    private String email;
    private Admin admin; //操作管理员
    private ReaderType readerType;
    private Date createTime;

    public Reader() {
    }

    private Set<BorrowInfo> borrowInfos;

    public Set<BorrowInfo> getBorrowInfos() {
        return borrowInfos;
    }

    public void setBorrowInfos(Set<BorrowInfo> borrowInfos) {
        this.borrowInfos = borrowInfos;
    }

    public Reader(String name, String cardId, String password, String phone, String email, Admin admin, ReaderType readerType, Date createTime) {
        super();
        this.name = name;
        this.cardId = cardId;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.admin = admin;
        this.readerType = readerType;
        this.createTime = createTime;
    }

    public Integer getReaderId() {

        return readerId;
    }

    public void setReaderId(Integer readerId) {
        this.readerId = readerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public ReaderType getReaderType() {
        return readerType;
    }

    public void setReaderType(ReaderType readerType) {
        this.readerType = readerType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
