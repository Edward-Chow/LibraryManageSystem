package dao;

import domain.ForfeitInfo;
import domain.PageBean;
import domain.Reader;

import java.util.List;

public interface ForfeitDao {

    public List<ForfeitInfo> getForfeitByReader(Reader reader);

    public boolean addForfeitInfo(ForfeitInfo forfeitInfo);

    public PageBean<ForfeitInfo> findForfeitInfoByPage(int pageCode, int pageSize);

    public ForfeitInfo getForfeitInfoById(ForfeitInfo forfeitInfo);

    public ForfeitInfo updateForfeitInfo(ForfeitInfo forfeitInfoById);

    public boolean deleteForfeitInfo(ForfeitInfo forfeitInfo);

}
