package dao.impl;

import dao.ReaderTypeDao;
import domain.ReaderType;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

public class ReaderTypeDaoImpl extends HibernateDaoSupport implements ReaderTypeDao{

    @Override
    public List<ReaderType> getAllReaderType() {
        String hql = "from ReaderType";
        List list = this.getHibernateTemplate().find(hql);
        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }

    @Override
    public ReaderType getTypeById(ReaderType readerType) {
        String hql = "from ReaderType r where r.readerTypeId=?";
        List list = this.getHibernateTemplate().find(hql, readerType.getReaderTypeId());
        if (list != null && list.size() > 0) {
            return (ReaderType)list.get(0);
        }
        return null;
    }

    @Override
    public ReaderType updateReaderType(ReaderType updateReaderType) {
        ReaderType readerType = null;
        try {
            this.getHibernateTemplate().clear();
            readerType = (ReaderType)this.getHibernateTemplate().merge(updateReaderType);
            this.getHibernateTemplate().flush();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new RuntimeException(throwable.getMessage());
        }
        return readerType;
    }

    @Override
    public boolean addReaderType(ReaderType readerType) {
        boolean flag = true;

        try {
            this.getHibernateTemplate().clear();
            this.getHibernateTemplate().save(readerType);
            this.getHibernateTemplate().flush();
        } catch (Throwable throwable) {
            flag = false;
            throwable.printStackTrace();
            throw new RuntimeException(throwable.getMessage());
        }
        return flag;
    }

    @Override
    public ReaderType getTypeByName(ReaderType type) {

        String hql = "from ReaderType r where r.readerTypeName=?";
        List list = this.getHibernateTemplate().find(hql, type.getReaderTypeName());
        if (list != null && list.size() > 0) {
            return (ReaderType)list.get(0);
        }
        return null;

    }
}
