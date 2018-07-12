package service;

import domain.Book;
import domain.BookType;
import domain.PageBean;

import java.util.List;

public interface BookTypeService {
    public PageBean<BookType> findBookTypeByPage(int pageCode, int pageSize);

    public BookType getBookTypeByName(BookType bookType);

    public boolean addBookType(BookType bookType);

    public BookType getBookTypeById(BookType bookType);

    public BookType updateBookTypeInfo(BookType bookType);

    public boolean deleteBookType(BookType bookType);

    public PageBean<BookType> queryBookType(BookType bookType, int pageCode, int pageSize);

    public Book getBookById(Book book);

    public List<BookType> getAllBookTypes();
}
