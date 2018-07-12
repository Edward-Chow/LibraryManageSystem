package service.impl;

import dao.BookDao;
import dao.BookTypeDao;
import dao.BorrowDao;
import dao.ForfeitDao;
import domain.Book;
import domain.BorrowInfo;
import domain.ForfeitInfo;
import domain.PageBean;
import service.BookService;

import java.util.List;

public class BookServiceImpl implements BookService {
    private BookDao bookDao;
    private BorrowDao borrowDao;
    private ForfeitDao forfeitDao;

    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public void setBorrowDao(BorrowDao borrowDao) {
        this.borrowDao = borrowDao;
    }

    public void setForfeitDao(ForfeitDao forfeitDao) {
        this.forfeitDao = forfeitDao;
    }

    @Override
    public boolean addBook(Book book) {
        return bookDao.addBook(book);
    }

    @Override
    public int deleteBook(Book book) {
        //删除图书需要注意的事项：如果该书有尚未归还的记录或者尚未缴纳的罚款记录,则不能删除
        List<BorrowInfo> borrowInfoList = borrowDao.getBorrowInfoByBook(book);
        for (BorrowInfo borrowInfo : borrowInfoList) {
            //图书还未归还
            if (!(borrowInfo.getState() == 2 || borrowInfo.getState() == 5)) {
                return -1;
            }
            ForfeitInfo forfeitInfo = new ForfeitInfo();
            forfeitInfo.setBorrowId(borrowInfo.getBorrowId());
            ForfeitInfo forfeitInfo1 = forfeitDao.getForfeitInfoById(forfeitInfo);
            //罚金未支付
            if (forfeitInfo1 != null) {
                if (forfeitInfo1.getIsPay() == 0) {
                    return -2;
                }
            }
        }
        if (bookDao.deleteBook(book)){
            return 1;
        }
        return 0;
    }

    @Override
    public Book getBookById(Book book) {
        return bookDao.getBookById(book);
    }

    @Override
    public Book getBookByISBN(Book book) {
        return bookDao.getBookByISBN(book);
    }

    @Override
    public Book updateBookInfo(Book book) {
        return bookDao.updateBookInfo(book);
    }

    @Override
    public PageBean<Book> findBookByPage(int pageCode, int pageSize) {
        return bookDao.findBookByPage(pageCode, pageSize);
    }

    @Override
    public PageBean<Book> queryBook(Book book, int pageCode, int pageSize) {
        return bookDao.queryBook(book, pageCode, pageSize);
    }
}
