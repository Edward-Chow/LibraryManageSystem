package service;

import domain.PageBean;
import domain.Reader;

import java.util.List;

public interface ReaderService {

    public Reader getReader(Reader reader);

    public Reader updateReaderInfo(Reader reader);

    public int deleteReader(Reader reader);

    public boolean addReader(Reader reader);

    public PageBean<Reader> findReaderByPage(int pageCode, int pageSize);

    public Reader getReaderById(Reader reader);


    public PageBean<Reader> queryReader(Reader reader,int pageCode, int pageSize);

    public Reader getReaderByCardID(Reader reader);

    //public JSONObject batchAddReader(List<Reader> readers, List<Reader> failReaders);

    public List<Reader> findAllReaders();
}
