package dao.impl;

import dao.BookDao;
import domain.Book;
import domain.PageBean;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.sql.SQLException;
import java.util.List;

public class BookDaoImpl extends HibernateDaoSupport implements BookDao {

    @Override
    public boolean addBook(Book book) {
        boolean flag = true;
        try {
            this.getHibernateTemplate().clear();
            this.getHibernateTemplate().save(book);
            this.getHibernateTemplate().flush();
        } catch (Throwable throwable) {
            flag = false;
            throwable.printStackTrace();
            throw new RuntimeException(throwable.getMessage());
        }
        return flag;
    }

    @Override
    public boolean deleteBook(Book book) {
        boolean b = true;
        try {
            this.getHibernateTemplate().clear();
            this.getHibernateTemplate().delete(book);
            this.getHibernateTemplate().flush();
        } catch (Throwable e1) {
            b = false;
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return b;
    }

    @Override
    public Book getBookById(Book book) {
        String hql = "from Book b where b.bookId=? ";
        List list = this.getHibernateTemplate().find(hql, book.getBookId());
        if (list != null && list.size() > 0) {
            return (Book) list.get(0);
        }
        return null;
    }

    @Override
    public Book getBookByISBN(Book book) {
        String hql = "from Book b where b.ISBN=? ";
        List list = this.getHibernateTemplate().find(hql, book.getISBN());
        if (list != null && list.size() > 0) {
            return (Book) list.get(0);
        }
        return null;
    }

    @Override
    public List<Book> findAllBooks() {
        String hql = "from Book";
        List list = this.getHibernateTemplate().find(hql);
        return list;
    }

    @Override
    public Book updateBookInfo(Book book) {
        Book newBook = null;
        try {
            this.getHibernateTemplate().clear();
            newBook = (Book) this.getHibernateTemplate().merge(book);
            this.getHibernateTemplate().flush();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new RuntimeException(throwable.getMessage());
        }
        return newBook;
    }

    @Override
    public int batchAddBook(List<Book> books, List<Book> failBooks) {
        int successCnt = 0;
        for (int i = 0; i < books.size(); i++) {
            try {
                this.getHibernateTemplate().clear();
                this.getHibernateTemplate().save(books.get(i));
                this.getHibernateTemplate().flush();
                successCnt++;
            } catch (Throwable throwable) {
                this.getHibernateTemplate().clear();
                books.get(i).setISBN(books.get(i).getISBN() + "可能已经存在该图书");
                failBooks.add(books.get(i));
                continue;
            }
        }
        return successCnt;
    }

    //分页查询函数
    public List doSplitPage(final String hql, final int pageCode, final int pageSize) {
        //调用模板的execute方法，参数是实现了HibernateCallback接口的匿名类，
        return (List) this.getHibernateTemplate().execute(new HibernateCallback() {
            //重写其doInHibernate方法返回一个object对象，
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                //创建query对象
                Query query = session.createQuery(hql);
                //返回其执行了分布方法的list
                return query.setFirstResult((pageCode - 1) * pageSize).setMaxResults(pageSize).list();
            }
        });
    }

    @Override
    public PageBean<Book> findBookByPage(int pageCode, int pageSize) {
        PageBean<Book> pageBean = new PageBean<Book>();
        pageBean.setPageCode(pageCode);
        pageBean.setPageSize(pageSize);
        List beanList = null;

        try {
            String sql = "select count(*) from Book";
            List list = this.getSession().createQuery(sql).list();
            int totalRecord = Integer.parseInt(list.get(0).toString());
            pageBean.setTotalRecord(totalRecord);
            this.getSession().close();

            String hql = "from Book";
            beanList = doSplitPage(hql, pageCode, pageSize);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new RuntimeException(throwable.getMessage());
        }
        if (beanList != null && beanList.size() > 0) {
            pageBean.setBeanList(beanList);
            return pageBean;
        }

        return null;
    }

    @Override
    public PageBean<Book> queryBook(Book book, int pageCode, int pageSize) {
        PageBean<Book> pageBean = new PageBean<Book>();
        pageBean.setPageCode(pageCode);
        pageBean.setPageSize(pageSize);

        StringBuilder sb_hql = new StringBuilder();
        StringBuilder sb_sql = new StringBuilder();
        String hql = "from Book b where 1=1";
        String sql = "select count(*) from Book b where 1=1";
        sb_hql.append(hql);
        sb_sql.append(sql);

        if(!"".equals(book.getISBN().trim())) {
            sb_hql.append(" and b.ISBN like '%" + book.getISBN() + "%'");
            sb_sql.append(" and b.ISBN like '%" + book.getISBN() + "%'");
        }
        if(!"".equals(book.getBookName().trim())){
            sb_hql.append(" and b.bookName like '%" +book.getBookName() +"%'");
            sb_sql.append(" and b.bookName like '%" + book.getBookName() +"%'");
        }
        if(!"".equals(book.getPress())){
            sb_hql.append(" and b.press like '%" +book.getPress() +"%'");
            sb_sql.append(" and b.press like '%" + book.getPress() +"%'");
        }
        if(!"".equals(book.getAuth().trim())){
            sb_hql.append(" and b.auth like '%" +book.getAuth() +"%'");
            sb_sql.append(" and b.auth like '%" + book.getAuth() +"%'");
        }
        if(book.getBookType().getTypeId()!=-1){
            sb_hql.append(" and b.bookType="+book.getBookType().getTypeId());
            sb_sql.append(" and b.bookType="+book.getBookType().getTypeId());
        }

        try {
            List list = this.getSession().createQuery(sb_sql.toString()).list();
            int totalnum = Integer.parseInt(list.get(0).toString());
            pageBean.setTotalRecord(totalnum);
            this.getSession().close();

            List<Book> bookList = doSplitPage(sb_hql.toString(), pageCode, pageSize);
            if (bookList != null && bookList.size() > 0) {
                pageBean.setBeanList(bookList);
                return pageBean;
            }

        }catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new RuntimeException(throwable.getMessage());
        }
        return null;
    }
}
