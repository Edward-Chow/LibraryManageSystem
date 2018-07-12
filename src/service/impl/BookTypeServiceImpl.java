package service.impl;

import dao.BookTypeDao;
import domain.Book;
import domain.BookType;
import domain.PageBean;
import service.BookTypeService;

import java.util.List;

public class BookTypeServiceImpl implements BookTypeService{
    private BookTypeDao bookTypeDao;

    public void setBookTypeDao(BookTypeDao bookTypeDao) {
        this.bookTypeDao = bookTypeDao;
    }

    @Override
    public PageBean<BookType> findBookTypeByPage(int pageCode, int pageSize) {
        return bookTypeDao.findBookTypeByPage(pageCode, pageSize);
    }

    @Override
    public BookType getBookTypeByName(BookType bookType) {
        return bookTypeDao.getBookTypeByName(bookType);
    }

    @Override
    public boolean addBookType(BookType bookType) {
        return bookTypeDao.addBookType(bookType);
    }

    @Override
    public BookType getBookTypeById(BookType bookType) {
        return bookTypeDao.getBookTypeById(bookType);
    }

    @Override
    public BookType updateBookTypeInfo(BookType bookType) {
        return bookTypeDao.updateBookTypeInfo(bookType);
    }

    @Override
    public boolean deleteBookType(BookType bookType) {
        return bookTypeDao.deleteBookType(bookType);
    }

    @Override
    public PageBean<BookType> queryBookType(BookType bookType, int pageCode, int pageSize) {
        return bookTypeDao.queryBookType(bookType, pageCode, pageSize);
    }

    @Override
    public Book getBookById(Book book) {
        return bookTypeDao.getBookById(book);
    }

    @Override
    public List<BookType> getAllBookTypes() {
        return bookTypeDao.getAllBookTypes();
    }
}
