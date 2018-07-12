package dao;

import domain.Book;
import domain.BorrowInfo;
import domain.PageBean;
import domain.Reader;

import java.util.List;

public interface BorrowDao {

    public PageBean<BorrowInfo> findBorrowInfoByPage(int pageCode, int pageSize);

    public BorrowInfo getBorrowInfoById(BorrowInfo info);

    public int addBorrow(BorrowInfo info);

    //通过读者编号查询未归还书籍的借阅信息
    public List<BorrowInfo> getNoBackBorrowInfoByReader(Reader reader);

    public BorrowInfo updateBorrowInfo(BorrowInfo borrowInfoById);
    //查询为归还图书的借阅信息
    public List<BorrowInfo> getBorrowInfoByNoBackState();
    //查询某本书的借阅信息
    public List<BorrowInfo> getBorrowInfoByBook(Book book);
}
