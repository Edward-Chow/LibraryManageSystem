package dao;

import domain.Authorization;

public interface AuthorizationDao {

    public boolean addAuthorization(Authorization authorization);

    public Authorization getAuthorizationByAid(Authorization authorization);

    public Authorization updateAuthorization(Authorization authorization);
}
