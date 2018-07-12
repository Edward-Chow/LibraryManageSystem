package dao;

import domain.Book;
import domain.PageBean;

import java.util.List;

public interface BookDao {

    public boolean addBook(Book book);

    public boolean deleteBook(Book book);

    public Book getBookById(Book book);

    public Book getBookByISBN(Book book);

    public List<Book> findAllBooks();

    public Book updateBookInfo(Book book);

    public int batchAddBook(List<Book> books, List<Book> failBooks);

    public PageBean<Book> findBookByPage(int pageCode, int pageSize);

    public PageBean<Book> queryBook(Book book, int pageCode, int pageSize);
}
