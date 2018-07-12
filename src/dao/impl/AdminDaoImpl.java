package dao.impl;

import dao.AdminDao;
import domain.Admin;
import domain.PageBean;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import sun.jvm.hotspot.debugger.Page;

import java.sql.SQLException;
import java.util.List;

public class AdminDaoImpl extends HibernateDaoSupport implements AdminDao {

    @Override
    public Admin getAdminByUserName(Admin admin) {
        String hql = "from Admin a where a.username=? and a.state=1";
        List list = this.getHibernateTemplate().find(hql, admin.getUsername());
        if (list != null && list.size() > 0) {
            return (Admin)list.get(0);
        }
        return null;
    }

    @Override
    public Admin updateAdminInfo(Admin admin) {
        Admin newAdmin = null;
        try {
            this.getHibernateTemplate().clear();
            newAdmin = (Admin)this.getHibernateTemplate().merge(admin);
            this.getHibernateTemplate().flush();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new RuntimeException(throwable.getMessage());
        }
        return newAdmin;
    }

    @Override
    public List<Admin> getAllAdmins() {
        String hql= "from Admin a where a.state=1";
        List<Admin> list = null;
        try{
            list = this.getHibernateTemplate().find(hql);
        }catch (Throwable e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return list;
    }

    @Override
    public boolean addAdmin(Admin admin) {
        boolean flag = true;

        try {
            this.getHibernateTemplate().clear();
            this.getHibernateTemplate().save(admin);
            this.getHibernateTemplate().flush();
        } catch (Throwable throwable) {
            flag = false;
            throwable.printStackTrace();
            throw new RuntimeException(throwable.getMessage());
        }

        return flag;
    }

    @Override
    public Admin getAdminById(Admin admin) {
        String hql = "from Admin a where a.aid=? and a.state=1";
        List list = this.getHibernateTemplate().find(hql, admin.getAid());
        if (list != null && list.size() > 0) {
            return (Admin)list.get(0);
        }
        return null;
    }

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
    public PageBean<Admin> findAdminByPage(int pageCode, int pageSize) {
        PageBean<Admin> pageBean = new PageBean<Admin>();
        pageBean.setPageCode(pageCode);
        pageBean.setPageSize(pageSize);
        List beanList = null;

        try {
            String sql = "select count(*) from Admin a where a.state=1";
            List list = this.getSession().createQuery(sql).list();
            int totalRecord = Integer.parseInt(list.get(0).toString());
            pageBean.setTotalRecord(totalRecord);
            this.getSession().close();

            String hql = "from Admin a where a.state=1";
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
    public PageBean<Admin> queryAdmin(Admin admin, int pageCode, int pageSize) {
        PageBean<Admin> pageBean = new PageBean<Admin>();
        pageBean.setPageCode(pageCode);
        pageBean.setPageSize(pageSize);

        StringBuilder sb_sql = new StringBuilder();
        sb_sql.append("select count(*) from Admin a where a.state=1 ");
        StringBuilder sb_hql = new StringBuilder();
        sb_hql.append("from Admin a where a.state=1 ");

        if(!"".equals(admin.getName().trim())) {
            sb_sql.append(" and a.name like '%" + admin.getName() + "%'");
            sb_hql.append(" and a.name like '%" + admin.getName() + "%'");
        }
        if(!"".equals(admin.getUsername().trim())) {
            sb_hql.append(" and a.username like '%" + admin.getUsername() + "%'");
            sb_sql.append(" and a.username like '%" + admin.getUsername() + "%'");
        }

        try {
            List list = this.getSession().createQuery(sb_sql.toString()).list();
            int totalNum = Integer.parseInt(list.get(0).toString());
            pageBean.setTotalRecord(totalNum);
            this.getSession().close();

            List<Admin> beanList = doSplitPage(sb_hql.toString(), pageCode, pageSize);
            if (beanList != null && beanList.size() >0) {
                pageBean.setBeanList(beanList);
                return pageBean;
            }

        } catch (Throwable throwable){
            throwable.printStackTrace();
            throw new RuntimeException(throwable.getMessage());
        }
        return null;
    }

    @Override
    public boolean deleteAdmin(Admin admin) {
        boolean b = true;
        try {
            admin.setState(0);
            this.getHibernateTemplate().clear();
            this.getHibernateTemplate().merge(admin);
            this.getHibernateTemplate().flush();
        } catch (Throwable e) {
            b = false;
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return b;
    }
}
