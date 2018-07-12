package dao.impl;

import dao.ReaderDao;
import domain.PageBean;
import domain.Reader;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.sql.SQLException;
import java.util.List;

public class ReaderDaoImpl extends HibernateDaoSupport implements ReaderDao {

    @Override
    public Reader getReader(Reader reader) {
        String hql = "from Reader r where r.readerId=?";

        try {
            List list = this.getHibernateTemplate().find(hql, reader.getReaderId());
            if (list != null && list.size() > 0) {
                return  (Reader)list.get(0);
            }
        } catch (Throwable throwable){
            throw new RuntimeException(throwable.getMessage());
        }

        return null;
    }

    @Override
    public Reader updateReaderInfo(Reader reader) {
        Reader reader1 = null;
        //将传入的detached(分离的)状态的对象的属性复制到持久化对象中，并返回该持久化对象
        try{
            this.getHibernateTemplate().clear();
            reader1 = (Reader)this.getHibernateTemplate().merge(reader);
            this.getHibernateTemplate().flush();
        }catch (Throwable throwable){
            throw new RuntimeException(throwable.getMessage());
        }

        return reader1;
    }

    @Override
    public boolean deleteReader(Reader reader) {

        boolean flag = true;

        try {
            this.getHibernateTemplate().clear();
            this.getHibernateTemplate().delete(reader);
            this.getHibernateTemplate().flush();
        }catch (Throwable throwable){
            flag = false;
            throwable.printStackTrace();
            throw new RuntimeException(throwable.getMessage());
        }

        return flag;
    }

    @Override
    public boolean addReader(Reader reader) {
        boolean flag = true;

        try {
            this.getHibernateTemplate().clear();
            this.getHibernateTemplate().save(reader);
            this.getHibernateTemplate().flush();
        }catch (Throwable throwable){
            flag = false;
            throwable.printStackTrace();
            throw new RuntimeException(throwable.getMessage());
        }

        return flag;
    }


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
    public PageBean<Reader> findReaderByPage(int pageCode, int pageSize) {
        PageBean<Reader> pageBean = new PageBean<Reader>();
        pageBean.setPageCode(pageCode);
        pageBean.setPageSize(pageSize);
        List readerList = null;

        try{
            String sql = "SELECT count(*) FROM Reader";
            List list = this.getSession().createQuery(sql).list();
            int totalRecord = Integer.parseInt(list.get(0).toString()); //得到总记录数

            pageBean.setTotalRecord(totalRecord);	//设置总记录数
            this.getSession().close();

            //不支持limit分页
            String hql= "from Reader";
            //分页查询
            readerList = doSplitPage(hql,pageCode,pageSize);

        }catch (Throwable throwable){
            throwable.printStackTrace();
            throw  new RuntimeException(throwable.getMessage());
        }

        if(readerList != null && readerList.size() > 0) {
            pageBean.setBeanList(readerList);
            return pageBean;
        }

        return null;
    }

    @Override
    public Reader getReaderById(Reader reader) {
        String hql = "from Reader r where r.readerId=?";

        List list = this.getHibernateTemplate().find(hql, reader.getReaderId());
        if (list != null && list.size() > 0) {
            return (Reader) list.get(0);
        }
        return null;
    }

    @Override
    public PageBean<Reader> queryReader(Reader reader, int pageCode, int pageSize) {
        PageBean<Reader> pageBean = new PageBean<Reader>();
        pageBean.setPageCode(pageCode);
        pageBean.setPageSize(pageSize);

        StringBuilder sb_hql = new StringBuilder();
        StringBuilder sb_sql = new StringBuilder();
        String hql = "from Reader r where 1=1";
        String sql = "select count(*) from Reader r where 1=1";
        sb_hql.append(hql);
        sb_sql.append(sql);
        if(!"".equals(reader.getCardId().trim())) {
            sb_hql.append(" and r.cardId like '%" + reader.getCardId() + "%'");
            sb_sql.append(" and r.cardId like '%" + reader.getCardId() + "%'");
        }
        if(!"".equals(reader.getName().trim())) {
            sb_hql.append(" and r.name like '%" + reader.getName() + "%'");
            sb_sql.append(" and r.name like '%" + reader.getName() + "%'");
        }
        //*************************

        //用readerTypeID 还是 readerType 进行查询
        if(reader.getReaderType().getReaderTypeId() != -1) {
            sb_hql.append(" and r.readerType="+reader.getReaderType().getReaderTypeId());
            sb_sql.append(" and r.readerType="+reader.getReaderType().getReaderTypeId());
        }

        try {
            List list = this.getSession().createQuery(sb_sql.toString()).list();
            int totalnum = Integer.parseInt(list.get(0).toString());
            pageBean.setTotalRecord(totalnum);
            this.getSession().close();

            List<Reader> adminlist = doSplitPage(sb_hql.toString(), pageCode, pageSize);
            if (adminlist != null && adminlist.size() > 0) {
                pageBean.setBeanList(adminlist);
                return pageBean;
            }

        }catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new RuntimeException(throwable.getMessage());
        }

        return null;
    }

    @Override
    public Reader getReaderByCardID(Reader reader) {
        String hql = "from Reader r where r.cardId=?";
        List list = this.getHibernateTemplate().find(hql, reader.getCardId());
        if (list != null && list.size() > 0) {
            return (Reader)list.get(0);
        }
        return null;
    }

    @Override
    public int batchAddReader(List<Reader> readers, List<Reader> failReaders) {
        int successCnt = 0;

        for (int i = 0; i < readers.size(); i++) {
            try {
                this.getHibernateTemplate().clear();
                this.getHibernateTemplate().save(readers.get(i));
                this.getHibernateTemplate().flush();
                successCnt++;
            } catch (Throwable throwable) {
                this.getHibernateTemplate().clear();
                readers.get(i).setCardId(readers.get(i).getCardId() + "(可能已存在该读者)");
                failReaders.add(readers.get(i));
                continue ;
            }
        }

        return successCnt;
    }

    @Override
    public List<Reader> findAllReaders() {
        String hql = "from Reader";
        List list = this.getHibernateTemplate().find(hql);
        return list;
    }
}
