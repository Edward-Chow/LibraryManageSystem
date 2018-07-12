package dao;

import domain.BackInfo;
import domain.PageBean;

public interface BackDao {

    public int addBack(BackInfo backInfo);

    public BackInfo updateBackInfo(BackInfo backInfoById);

    public boolean deleteBackInfo(BackInfo backInfo);

    public PageBean<BackInfo> findBackInfoByPage(int pageCode, int pageSize);

    public BackInfo getBackInfoById(BackInfo backInfo);

    public PageBean<Integer> getBorrowIdList(String iSBN, String paperNO,int borrowId,int pageCode, int pageSize);

}
