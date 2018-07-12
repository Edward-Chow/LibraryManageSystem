package dao.impl;

import dao.BookTypeDao;
import domain.Book;
import domain.BookType;
import domain.PageBean;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.sql.SQLException;
import java.util.List;

public class BookTypeDaoImpl extends HibernateDaoSupport implements BookTypeDao{

    //分页查询函数
    public List doSplitPage(final String hql,final int pageCode,final int pageSize){
        //调用模板的execute方法，参数是实现了HibernateCallback接口的匿名类，
        return (List) this.getHibernateTemplate().execute(new HibernateCallback(){
            //重写其doInHibernate方法返回一个object对象，
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                //创建query对象
                Query query=session.createQuery(hql);
                //返回其执行了分布方法的list
                return query.setFirstResult((pageCode-1)*pageSize).setMaxResults(pageSize).list();
            }
        });
    }

    @Override
    public PageBean<BookType> findBookTypeByPage(int pageCode, int pageSize) {
        PageBean<BookType> pageBean = new PageBean<BookType>();
        pageBean.setPageCode(pageCode);
        pageBean.setPageSize(pageSize);
        List beanList = null;
        try {
            String sql = "select count(*) from BookType";
            List list = this.getSession().createQuery(sql).list();
            int totalnum = Integer.parseInt(list.get(0).toString());
            pageBean.setTotalRecord(totalnum);
            this.getSession().close();

            String hql = "from BookType";
            beanList = doSplitPage(hql, pageCode, pageSize);
        }catch (Throwable throwable){
            throwable.printStackTrace();
            throw new RuntimeException(throwable);
        }
        if (beanList != null && beanList.size() > 0) {
            pageBean.setBeanList(beanList);
            return pageBean;
        }
        return null;
    }

    @Override
    public PageBean<BookType> queryBookType(BookType bookType, int pageCode, int pageSize) {
        PageBean<BookType> pageBean = new PageBean<BookType>();
        pageBean.setPageCode(pageCode);
        pageBean.setPageSize(pageSize);

        StringBuilder sb_hql = new StringBuilder();
        StringBuilder sb_sql = new StringBuilder();
        String hql = "from BookType b where 1=1";
        String sql = "select count(*) from BookType b where 1=1";
        sb_hql.append(hql);
        sb_sql.append(sql);

        if(!"".equals(bookType.getTypeName().trim())) {
            sb_hql.append(" and b.typeName like '%" + bookType.getTypeName() + "%'");
            sb_sql.append(" and b.typeName like '%" + bookType.getTypeName() + "%'");
        }

        try {
            List list = this.getSession().createQuery(sb_sql.toString()).list();
            int totalnum = Integer.parseInt(list.get(0).toString());
            pageBean.setTotalRecord(totalnum);
            this.getSession().close();

            List<BookType> bookTypeList = doSplitPage(sb_hql.toString(), pageCode, pageSize);
            if (bookTypeList != null && bookTypeList.size() > 0) {
                pageBean.setBeanList(bookTypeList);
                return pageBean;
            }

        }catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new RuntimeException(throwable.getMessage());
        }
        return null;
    }

    @Override
    public BookType getBookTypeByName(BookType bookType) {
        String hql = "from BookType b where b.typeName=?";
        List list = this.getHibernateTemplate().find(hql, bookType.getTypeName());
        if (list != null && list.size() > 0) {
            return (BookType)list.get(0);
        }
        return null;
    }

    @Override
    public boolean addBookType(BookType bookType) {
        boolean b = true;
        try {
            this.getHibernateTemplate().clear();
            this.getHibernateTemplate().save(bookType);
            this.getHibernateTemplate().flush();
        }catch (Throwable throwable) {
            b = false;
            throwable.printStackTrace();
            throw new RuntimeException(throwable.getMessage());
        }
        return b;
    }

    @Override
    public BookType getBookTypeById(BookType bookType) {
        String hql = "from BookType b where b.typeId=?";
        List list = this.getHibernateTemplate().find(hql, bookType.getTypeId());
        if (list != null && list.size() > 0) {
            return (BookType)list.get(0);
        }
        return null;
    }

    @Override
    public BookType updateBookTypeInfo(BookType bookType) {
        BookType newBookType = null;
        try {
            this.getHibernateTemplate().clear();
            newBookType = (BookType)this.getHibernateTemplate().merge(bookType);
            this.getHibernateTemplate().flush();
        }catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new RuntimeException(throwable.getMessage());
        }
        return newBookType;
    }

    @Override
    public boolean deleteBookType(BookType bookType) {
        boolean b = true;
        try {
            this.getHibernateTemplate().clear();
            this.getHibernateTemplate().delete(bookType);
            this.getHibernateTemplate().flush();
        }catch (Throwable throwable) {
            b = false;
            throwable.printStackTrace();
            throw new RuntimeException(throwable.getMessage());
        }
        return b;
    }

    @Override
    public Book getBookById(Book book) {
        String hql = "from Book b where b.bookId=?";
        List list = this.getHibernateTemplate().find(hql, book.getBookId());
        if (list != null && list.size() > 0) {
            return (Book)list.get(0);
        }
        return null;
    }

    @Override
    public List<BookType> getAllBookTypes() {
        String hql = "from BookType";
        List list = this.getHibernateTemplate().find(hql);
        return list;
    }
}
