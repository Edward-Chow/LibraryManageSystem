package service.impl;

import dao.ReaderTypeDao;
import domain.ReaderType;
import service.ReaderTypeService;

import java.util.List;

public class ReaderTypeServiceImpl implements ReaderTypeService{
    private ReaderTypeDao readerTypeDao;

    public void setReaderTypeDao(ReaderTypeDao readerTypeDao) {
        this.readerTypeDao = readerTypeDao;
    }

    @Override
    public List<ReaderType> getAllReaderType() {
        return readerTypeDao.getAllReaderType();
    }

    @Override
    public ReaderType getTypeById(ReaderType readerType) {
        return readerTypeDao.getTypeById(readerType);
    }

    @Override
    public ReaderType updateReaderType(ReaderType updateReaderType) {
        return readerTypeDao.updateReaderType(updateReaderType);
    }

    @Override
    public boolean addReaderType(ReaderType readerType) {
        return readerTypeDao.addReaderType(readerType);
    }

    @Override
    public ReaderType getTypeByName(ReaderType type) {
        return readerTypeDao.getTypeByName(type);
    }
}
