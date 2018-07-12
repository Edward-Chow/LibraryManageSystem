package service.impl;

import dao.BackDao;
import dao.BorrowDao;
import dao.ForfeitDao;
import domain.BorrowInfo;
import domain.ForfeitInfo;
import domain.PageBean;
import domain.Reader;
import service.ForfeitService;

import java.util.ArrayList;
import java.util.List;

public class ForfeitServiceImpl implements ForfeitService{

    private ForfeitDao forfeitDao;
    private BackDao backDao;
    private BorrowDao borrowDao;

    public void setForfeitDao(ForfeitDao forfeitDao) {
        this.forfeitDao = forfeitDao;
    }

    public void setBackDao(BackDao backDao) {
        this.backDao = backDao;
    }

    public void setBorrowDao(BorrowDao borrowDao) {
        this.borrowDao = borrowDao;
    }

    @Override
    public PageBean<ForfeitInfo> queryForfeitInfo(String iSBN, String cardId, int borrowId, int pageCode, int pageSize) {
        PageBean<ForfeitInfo> pageBean = new PageBean<ForfeitInfo>();
        pageBean.setPageCode(pageCode);
        pageBean.setPageSize(pageSize);
        PageBean<Integer> list = backDao.getBorrowIdList(iSBN, cardId, borrowId, pageCode, pageSize);
        pageBean.setTotalRecord(list.getTotalRecord());
        List<Integer> beanList = list.getBeanList();
        if (beanList.size() == 0) {
            return null;
        }
        List<ForfeitInfo> forfeitInfoList = new ArrayList<ForfeitInfo>();
        for (Integer i : beanList) {
            ForfeitInfo forfeitInfo = new ForfeitInfo();
            forfeitInfo.setBorrowId(i);
            ForfeitInfo info = forfeitDao.getForfeitInfoById(forfeitInfo);
            if (info != null) {
                forfeitInfoList.add(info);
            }
        }
        if (forfeitInfoList.size() == 0) {
            return null;
        }
        pageBean.setBeanList(forfeitInfoList);
        return pageBean;
    }

    /*
     * 1. 得到借阅记录
     *
     * 2. 查看当前的借阅状态
     * 		2.1 如果当前状态为未归还(逾期未归还,借阅逾期未归还),则提示读者先去还书再来缴纳罚金,返回-1
     * 		2.2 如果当前状态为归还,则继续下一步
     *
     * 3. 获得当前的管理员
     *
     * 4. 为当前罚金记录进行设置为已支付并设置管理员
     *
     * 5. 修改罚金记录
     */
    @Override
    public int payForfeit(ForfeitInfo forfeitInfo) {
        BorrowInfo borrowInfo = new BorrowInfo();
        borrowInfo.setBorrowId(forfeitInfo.getBorrowId());
        BorrowInfo borrowInfoById = borrowDao.getBorrowInfoById(borrowInfo);
        //如果当前状态为未归还(逾期未归还,借阅逾期未归还),则提示读者先去还书再来缴纳罚金,返回-1
        if (borrowInfoById.getState() == 1 || borrowInfoById.getState() == 4) {
            return -1;
        }
        ForfeitInfo forfeitInfoById = forfeitDao.getForfeitInfoById(forfeitInfo);
        //已经缴纳罚金
        if (forfeitInfoById.getIsPay() == 1) {
            return -2;
        }
        forfeitInfoById.setIsPay(1);
        forfeitInfoById.setAdmin(forfeitInfo.getAdmin());
        ForfeitInfo ff =  forfeitDao.updateForfeitInfo(forfeitInfoById);
        if (ff != null) {
            return 1;
        }
        return 0;
    }

    @Override
    public PageBean<ForfeitInfo> findForfeitInfoByPage(int pageCode, int pageSize) {
        return forfeitDao.findForfeitInfoByPage(pageCode, pageSize);
    }

    @Override
    public ForfeitInfo getForfeitInfoById(ForfeitInfo forfeitInfo) {
        return forfeitDao.getForfeitInfoById(forfeitInfo);
    }

    @Override
    public PageBean<ForfeitInfo> findMyForfeitInfoByPage(Reader reader, int pageCode, int pageSize) {
        String ISBN = "";
        int borrowId = 0;
        String cardId = reader.getCardId();

        return queryForfeitInfo(ISBN, cardId, borrowId, pageCode, pageSize);
    }
}
