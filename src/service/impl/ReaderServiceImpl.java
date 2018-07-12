package service.impl;

import dao.ForfeitDao;
import dao.ReaderDao;
import domain.BorrowInfo;
import domain.ForfeitInfo;
import domain.PageBean;
import domain.Reader;
import service.ReaderService;

import java.util.List;
import java.util.Set;

public class ReaderServiceImpl implements ReaderService{
    private ReaderDao readerDao;
    private ForfeitDao forfeitDao;

    public void setReaderDao(ReaderDao readerDao) {
        this.readerDao = readerDao;
    }

    public void setForfeitDao(ForfeitDao forfeitDao) {
        this.forfeitDao = forfeitDao;
    }

    @Override
    public Reader getReader(Reader reader) {
        return readerDao.getReader(reader);
    }

    @Override
    public Reader updateReaderInfo(Reader reader) {
        return readerDao.updateReaderInfo(reader);
    }

    @Override
    public int deleteReader(Reader reader) {
        //删除读者需要注意的点：如果该读者有尚未归还的书籍或者尚未缴纳的罚款,则不能删除
        Reader reader1 = readerDao.getReaderById(reader);
        Set<BorrowInfo> borrowInfoList = reader1.getBorrowInfos();
        for (BorrowInfo borrowInfo : borrowInfoList) {
            //该读者有尚未归还的书籍
            if (!(borrowInfo.getState() == 2 || borrowInfo.getState() == 5)) {
                return -1;
            }
            ForfeitInfo forfeitInfo = new ForfeitInfo();
            forfeitInfo.setBorrowId(borrowInfo.getBorrowId());
            ForfeitInfo forfeitInfo1 = forfeitDao.getForfeitInfoById(forfeitInfo);
            //该读者有罚金尚未支付
            if (forfeitInfo1 != null)
                if (forfeitInfo1.getIsPay()==0)
                    return -2;
        }
        if(readerDao.deleteReader(reader)) {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean addReader(Reader reader) {
        return readerDao.addReader(reader);
    }

    @Override
    public PageBean<Reader> findReaderByPage(int pageCode, int pageSize) {
        return readerDao.findReaderByPage(pageCode, pageSize);
    }

    @Override
    public Reader getReaderById(Reader reader) {
        return readerDao.getReaderById(reader);
    }

    @Override
    public PageBean<Reader> queryReader(Reader reader, int pageCode, int pageSize) {
        return readerDao.queryReader(reader, pageCode, pageSize);
    }

    @Override
    public Reader getReaderByCardID(Reader reader) {
        return readerDao.getReaderByCardID(reader);
    }

    @Override
    public List<Reader> findAllReaders() {
        return readerDao.findAllReaders();
    }
}
