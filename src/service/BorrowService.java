package service;

import domain.Book;
import domain.BorrowInfo;
import domain.PageBean;
import domain.Reader;

import java.util.List;

public interface BorrowService {
    public PageBean<BorrowInfo> findBorrowInfoByPage(int pageCode, int pageSize);

    public BorrowInfo getBorrowInfoById(BorrowInfo info);

    public int addBorrow(BorrowInfo info);
    //检查借阅表是否有逾期
    public boolean checkBorrowInfo();
    //续借
    public int renewBook(BorrowInfo borrowInfo);
}
