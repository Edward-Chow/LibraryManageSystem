package service.impl;

import dao.BackDao;
import dao.BookDao;
import dao.BorrowDao;
import dao.ForfeitDao;
import domain.*;
import service.BackService;

import javax.sound.midi.SysexMessage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BackServiceImpl implements BackService{
    private BorrowDao borrowDao;
    private BookDao bookDao;
    private BackDao backDao;

    public void setBorrowDao(BorrowDao borrowDao) {
        this.borrowDao = borrowDao;
    }

    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public void setBackDao(BackDao backDao) {
        this.backDao = backDao;
    }

    /*
     * 1. 获得操作的借阅编号
     *
     * 2. 获得当前的管理员
     *
     * 3. 获得借阅的书籍
     * 		3.1 书籍的在馆数量增加
     *
     *
     * 4. 获取当前时间
     *
     * 5. 设置操作管理员
     *
     * 6. 设置归还时间
     *
     *
     * 7. 设置借阅的状态
     * 		7.1 如果当前借阅不属于续借，则设置为归还
     * 		7.2 如果当前借阅属于续借,则设置为续借归还
     *
     * 8. 查看该借阅记录有逾期罚金未缴纳的记录
     * 		8.1 如果有，返回状态码2,提示读者去缴费
     *		8.2 如果没有,则结束
     *
     *
     *
     */
    @Override
    public int addBackInfo(BackInfo backInfo) {
        BorrowInfo borrowInfo = borrowDao.getBorrowInfoById(backInfo.getBorrowInfo());
        //书籍已经归还
        if (borrowInfo.getState() == 2 || borrowInfo.getState() == 5) {
            return -1;
        }
        Book book = borrowInfo.getBook();
        Book bookById = bookDao.getBookById(book);
        // 书籍的在馆数量增加
        bookById.setNumInLibrary(bookById.getNumInLibrary()+1);
        Book b = bookDao.updateBookInfo(bookById);
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        BackInfo backInfoById = backDao.getBackInfoById(backInfo);
        backInfoById.setBackDay(date);
        //设置操作管理员
        backInfoById.setAdmin(backInfo.getAdmin());

        int state = borrowInfo.getState();
        BackInfo backInfo1 = null;
        if(b != null) {
            backInfo1 = backDao.updateBackInfo(backInfoById);
        }
        //如果当前借阅不属于续借，则设置为归还
        if (state == 0 || state == 1) {
            borrowInfo.setState(2);
        }
        //如果当前借阅属于续借,则设置为续借归还
        if (state == 3 || state == 5) {
            borrowInfo.setState(5);
        }
        BorrowInfo bi = null;
        if(backInfo1 != null) {
            bi = borrowDao.updateBorrowInfo(borrowInfo);
        }
        if (bi != null) {
            if (state == 1 || state == 4) {
                return 2;
            } else {
                return 1;
            }
        }
        return 0;
    }

    @Override
    public PageBean<BackInfo> findBackInfoByPage(int pageCode, int pageSize) {
        return backDao.findBackInfoByPage(pageCode, pageSize);
    }

    @Override
    public BackInfo getBackInfoById(BackInfo backInfo) {
        return backDao.getBackInfoById(backInfo);
    }

    @Override
    public PageBean<BackInfo> queryBackInfo(String ISBN, String cardId, int borrowId, int pageCode, int pageSize) {
        PageBean<BackInfo> pageBean = new PageBean<BackInfo>();
        pageBean.setPageCode(pageCode);
        pageBean.setPageSize(pageSize);
        PageBean<Integer> list = backDao.getBorrowIdList(ISBN, cardId, borrowId, pageCode, pageSize);
        pageBean.setTotalRecord(list.getTotalRecord());
        List<Integer> beanList = list.getBeanList();
        if (beanList.size() == 0) {
            return null;
        }
        List<BackInfo> backInfos = new ArrayList<BackInfo>();
        for (Integer i : beanList) {
            BackInfo backInfo = new BackInfo();
            backInfo.setBorrowId(i);
            BackInfo info = backDao.getBackInfoById(backInfo);
            backInfos.add(info);
        }
        pageBean.setBeanList(backInfos);
        return pageBean;
    }

    @Override
    public PageBean<BackInfo> findMyBorrowInfoByPage(Reader reader, int pageCode, int pageSize) {
        String ISBN = "";
        int borrowId = 0;
        String cardId = reader.getCardId();
        return queryBackInfo(ISBN, cardId, borrowId, pageCode, pageSize);
    }
}
