package dao.impl;

import dao.BookDao;
import dao.BorrowDao;
import domain.Book;
import domain.BorrowInfo;
import domain.PageBean;
import domain.Reader;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

public class BorrowDaoImpl extends HibernateDaoSupport implements BorrowDao {

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
    public PageBean<BorrowInfo> findBorrowInfoByPage(int pageCode, int pageSize) {
        PageBean<BorrowInfo> pageBean = new PageBean<BorrowInfo>();
        pageBean.setPageCode(pageCode);
        pageBean.setPageSize(pageSize);
        List beanBorrowList = null;
        try {
            String sql = "select count(*) from BorrowInfo";
            List list = this.getSession().createQuery(sql).list();
            int totalRecord = Integer.parseInt(list.get(0).toString());
            pageBean.setTotalRecord(totalRecord);
            this.getSession().close();
            String hql = "from BorrowInfo";
            beanBorrowList = doSplitPage(hql, pageCode, pageSize);
        }catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new RuntimeException(throwable.getMessage());
        }
        if (beanBorrowList != null && beanBorrowList.size() > 0) {
            pageBean.setBeanList(beanBorrowList);
            return pageBean;
        }
        return null;
    }

    @Override
    public BorrowInfo getBorrowInfoById(BorrowInfo info) {
        String hql = "from BorrowInfo b where b.borrowId=?";
        List list = this.getHibernateTemplate().find(hql, info.getBorrowId());
        if (list != null && list.size() > 0) {
            return (BorrowInfo) list.get(0);
        }
        return null;
    }

    @Override
    public int addBorrow(BorrowInfo info) {
        Integer integer = 0;
        try{
            this.getHibernateTemplate().clear();
            //save方法返回的是Serializable接口，该结果的值就是插入到数据库后新记录的主键值
            Serializable save = this.getHibernateTemplate().save(info);
            this.getHibernateTemplate().flush();
            integer = (Integer)save;
        }catch (Throwable e1) {
            integer = 0;
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return integer;
    }

    @Override
    public List<BorrowInfo> getNoBackBorrowInfoByReader(Reader reader) {
        String hql = "from BorrowInfo b where b.state=0 or b.state=1 or b.state=3 or b.state=4 and b.reader.readerId=?";
        List list = null;
        try {
            list = this.getHibernateTemplate().find(hql, reader.getReaderId());
        }catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }

    @Override
    public BorrowInfo updateBorrowInfo(BorrowInfo borrowInfoById) {
        BorrowInfo newBorrowInfo = null;
        try {
            this.getHibernateTemplate().clear();
            newBorrowInfo = (BorrowInfo) this.getHibernateTemplate().merge(borrowInfoById);
            this.getHibernateTemplate().flush();
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return newBorrowInfo;
    }

    @Override
    public List<BorrowInfo> getBorrowInfoByNoBackState() {
        String hql = "from BorrowInfo b where b.state=0 or b.state=1 or b.state=3 or b.state=4";
        List list = null;
        try {
            list = this.getHibernateTemplate().find(hql);
        }catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }

    @Override
    public List<BorrowInfo> getBorrowInfoByBook(Book book) {
        String hql= "from BorrowInfo b where b.book.bookId=?";
        List<BorrowInfo> list = null;
        try{
            list = this.getHibernateTemplate().find(hql, book.getBookId());
        }catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }
}
