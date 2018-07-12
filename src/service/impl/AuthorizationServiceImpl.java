package service.impl;

import dao.AuthorizationDao;
import domain.Authorization;
import service.AuthorizationService;

public class AuthorizationServiceImpl implements AuthorizationService {

    private AuthorizationDao authorizationDao;

    public void setAuthorizationDao(AuthorizationDao authorizationDao) {
        this.authorizationDao = authorizationDao;
    }

    @Override
    public boolean addAuthorization(Authorization authorization) {
        return authorizationDao.addAuthorization(authorization);
    }

    @Override
    public Authorization getAuthorizationByAid(Authorization authorization) {
        return authorizationDao.getAuthorizationByAid(authorization);
    }

    @Override
    public Authorization updateAuthorization(Authorization authorization) {
        return authorizationDao.updateAuthorization(authorization);
    }
}
