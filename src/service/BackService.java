package service;

import domain.BackInfo;
import domain.PageBean;
import domain.Reader;

public interface BackService{
    public int addBackInfo(BackInfo backInfo);

    public PageBean<BackInfo> findBackInfoByPage(int pageCode, int pageSize);

    public BackInfo getBackInfoById(BackInfo backInfo);

    public PageBean<BackInfo> queryBackInfo(String ISBN, String cardId,int borrowId,int pageCode,int pageSize);

    public PageBean<BackInfo> findMyBorrowInfoByPage(Reader reader, int pageCode, int pageSize);
}
