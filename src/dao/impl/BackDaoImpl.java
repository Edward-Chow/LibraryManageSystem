package dao.impl;

import dao.BackDao;
import domain.BackInfo;
import domain.PageBean;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BackDaoImpl extends HibernateDaoSupport implements BackDao {

    @Override
    public int addBack(BackInfo backInfo) {
        int a = 1;
        try {
            this.getHibernateTemplate().clear();
            this.getHibernateTemplate().save(backInfo);
            this.getHibernateTemplate().flush();
        } catch (Throwable throwable) {
            a = 0;
            throwable.printStackTrace();
            throw new RuntimeException(throwable.getMessage());
        }
        return a;
    }

    @Override
    public BackInfo updateBackInfo(BackInfo backInfoById) {
        BackInfo newBackInfo = null;
        try {
            this.getHibernateTemplate().clear();
            newBackInfo = (BackInfo) this.getHibernateTemplate().merge(backInfoById);
            this.getHibernateTemplate().flush();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new RuntimeException(throwable.getMessage());
        }
        return newBackInfo;
    }

    @Override
    public boolean deleteBackInfo(BackInfo backInfo) {
        boolean b = true;
        try {
            this.getHibernateTemplate().clear();
            this.getHibernateTemplate().delete(backInfo);
            this.getHibernateTemplate().flush();
        } catch (Throwable e1) {
            b = false;
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return b;
    }



    public List doLimitBackInfo(final String hql,final int pageCode,final int pageSize){
        //调用模板的execute方法，参数是实现了HibernateCallback接口的匿名类，
        return (List) this.getHibernateTemplate().execute(new HibernateCallback(){
            //重写其doInHibernate方法返回一个object对象，
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                //创建query对象
                SQLQuery query=session.createSQLQuery(hql);
                //返回其执行了分布方法的list
                return query.setFirstResult((pageCode-1)*pageSize).setMaxResults(pageSize).list();

            }

        });

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
    public PageBean<BackInfo> findBackInfoByPage(int pageCode, int pageSize) {
        PageBean<BackInfo> pageBean = new PageBean<BackInfo>();
        pageBean.setPageCode(pageCode);
        pageBean.setPageSize(pageSize);
        List beanList = null;

        try {
            String sql = "select count(*) from BackInfo";
            List list = this.getSession().createQuery(sql).list();
            int totalnum = Integer.parseInt(list.get(0).toString());
            pageBean.setTotalRecord(totalnum);
            this.getSession().close();

            String hql = "from BackInfo";
            beanList = doSplitPage(hql, pageCode, pageSize);
        }catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        if(beanList != null && beanList.size() > 0) {
            pageBean.setBeanList(beanList);
            return pageBean;
        }
        return null;
    }

    @Override
    public BackInfo getBackInfoById(BackInfo backInfo) {
        String hql = "from BackInfo b where b.borrowId=?";
        List list = this.getHibernateTemplate().find(hql, backInfo.getBorrowId());
        if (list != null && list.size() > 0) {
            return (BackInfo) list.get(0);
        }
        return null;
    }

    @Override
    public PageBean<Integer> getBorrowIdList(String iSBN, String cardId, int borrowId, int pageCode, int pageSize) {
        PageBean<Integer> pb = new PageBean<Integer>();	//pageBean对象，用于分页
        //根据传入的pageCode当前页码和pageSize页面记录数来设置pb对象
        pb.setPageCode(pageCode);//设置当前页码
        pb.setPageSize(pageSize);//设置页面记录数

        List<Integer> integers = new ArrayList<Integer>();
        StringBuilder sb = new StringBuilder();
        StringBuilder sb_sql = new StringBuilder();
        String sql = "select count(*) from BackInfo ba,BorrowInfo bo,Book bk,Reader r "
                +"where ba.borrowId=bo.borrowId and Bk.bookId=Bo.bookId and bo.readerId=r.readerId ";
        //不支持limit分页
        String hql= "select ba.borrowId from BackInfo ba,BorrowInfo bo,Book bk,Reader r "
                +"where ba.borrowId=bo.borrowId and Bk.bookId=Bo.bookId and bo.readerId=r.readerId ";
        sb.append(hql);
        sb_sql.append(sql);
        if(!"".equals(iSBN.trim())){
            sb.append(" and bk.ISBN like '%" + iSBN +"%'");
            sb_sql.append(" and bk.ISBN like '%" + iSBN +"%'");
        }
        if(!"".equals(cardId.trim())){
            sb.append(" and r.cardId like '%" + cardId +"%'");
            sb_sql.append(" and r.cardId like '%" + cardId +"%'");
        }
        if(borrowId!=0){
            sb.append(" and bo.borrowId like '%" + borrowId +"%'");
            sb_sql.append(" and bo.borrowId like '%" + borrowId +"%'");
        }

        try {
            SQLQuery createSQLQuery1 = this.getSession().createSQLQuery(sb_sql.toString());
            List list = createSQLQuery1.list();
            int totalRecord = Integer.parseInt(list.get(0).toString()); //得到总记录数
            pb.setTotalRecord(totalRecord);	//设置总记录数
            this.getSession().close();

            //不支持limit分页
            //分页查询
            List list2 = doLimitBackInfo(sb.toString(),pageCode,pageSize);
            for(Object object : list2){
                Integer i = new Integer(object.toString());
                integers.add(i);
            }
            pb.setBeanList(integers);
        }catch (Throwable e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return pb;
    }
}
