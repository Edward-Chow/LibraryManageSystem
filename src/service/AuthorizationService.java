package service;

import domain.Authorization;

public interface AuthorizationService {

    public boolean addAuthorization(Authorization authorization);

    public Authorization getAuthorizationByAid(Authorization authorization);

    public Authorization updateAuthorization(Authorization authorization);
}
