package dao.impl;

import dao.AuthorizationDao;
import domain.Authorization;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

public class AuthorizationDaoImpl extends HibernateDaoSupport implements AuthorizationDao{

    @Override
    public boolean addAuthorization(Authorization authorization) {
        boolean b = true;
        try {
            this.getHibernateTemplate().clear();
            this.getHibernateTemplate().merge(authorization);
            this.getHibernateTemplate().flush();
        } catch (Throwable throwable) {
            b = false;
            throwable.printStackTrace();
            throw new RuntimeException(throwable.getMessage());
        }
        return false;
    }

    @Override
    public Authorization getAuthorizationByAid(Authorization authorization) {
        String hql = "from Authorization a where aid=?";
        List list = this.getHibernateTemplate().find(hql, authorization.getAid());
        if (list != null && list.size() > 0) {
            return (Authorization)list.get(0);
        }
        return null;
    }

    @Override
    public Authorization updateAuthorization(Authorization authorization) {
        Authorization authorization1 = null;

        try {
            this.getHibernateTemplate().clear();
            authorization1 = (Authorization)this.getHibernateTemplate().merge(authorization);
            this.getHibernateTemplate().flush();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new RuntimeException(throwable.getMessage());
        }

        return authorization1;
    }
}
