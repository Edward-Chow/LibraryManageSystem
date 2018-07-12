package service;

import domain.ForfeitInfo;
import domain.PageBean;
import domain.Reader;

import java.util.List;

public interface ForfeitService {
    public PageBean<ForfeitInfo> queryForfeitInfo(String iSBN, String cardId, int borrowId, int pageCode, int pageSize);

    public int payForfeit(ForfeitInfo forfeitInfo);

    public PageBean<ForfeitInfo> findForfeitInfoByPage(int pageCode, int pageSize);

    public ForfeitInfo getForfeitInfoById(ForfeitInfo forfeitInfo);

    public PageBean<ForfeitInfo> findMyForfeitInfoByPage(Reader reader, int pageCode, int pageSize);
}
