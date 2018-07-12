package service.impl;

import dao.*;
import domain.*;
import service.BorrowService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BorrowServiceImpl implements BorrowService {
    private BorrowDao borrowDao;
    private BookDao bookDao;
    private ReaderDao readerDao;
    private ForfeitDao forfeitDao;
    private BackDao backDao;

    public void setBorrowDao(BorrowDao borrowDao) {
        this.borrowDao = borrowDao;
    }

    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public void setReaderDao(ReaderDao readerDao) {
        this.readerDao = readerDao;
    }

    public void setForfeitDao(ForfeitDao forfeitDao) {
        this.forfeitDao = forfeitDao;
    }

    public void setBackDao(BackDao backDao) {
        this.backDao = backDao;
    }

    @Override
    public PageBean<BorrowInfo> findBorrowInfoByPage(int pageCode, int pageSize) {
        return borrowDao.findBorrowInfoByPage(pageCode, pageSize);
    }

    @Override
    public BorrowInfo getBorrowInfoById(BorrowInfo info) {
        return borrowDao.getBorrowInfoById(info);
    }

    /*
     * 1. 得到借阅的读者
     *
     * 2. 查看读者输入的密码是否匹配
     * 		2.1 如果不匹配提示 密码错误
     * 		2.2 如果匹配,执行第3步骤
     *
     * 3. 查看该读者的借阅信息
     * 		3.1 查看借阅信息的条数
     * 		3.2 查看该读者的类型得出该读者的最大借阅数量
     * 		3.3 匹配借阅的数量是否小于最大借阅量
     * 			3.3.1 小于,则可以借阅
     * 			3.3.2 大于,则不可以借阅,直接返回借阅数量已超过
     * 		3.4 查看读者的罚款信息,是否有未缴纳的罚款
     * 			3.4.1 如果没有,继续第3的操作步骤
     * 			3.4.2 如果有,直接返回有尚未缴纳的罚金
     *
     * 4. 查看借阅的书籍
     * 		4.1 查看该书籍的在馆数量是否大于1,,如果为1则不能借阅,必须留在馆内浏览
     * 			4.1.1 如果大于1,进入第4操作步骤
     * 			4.1.2 如果小于等于1,提示该图书为馆内最后一本,无法借阅
     *
     * 5. 处理借阅信息
     * 		5.1 得到该读者的最大借阅天数,和每日罚金
     * 			5.1.1 获得当前时间
     * 			5.1.2 根据最大借阅天数,计算出截止日期
     * 			5.1.3 为借阅信息设置每日的罚金金额
     * 		5.2 获得该读者的信息,为借阅信息设置读者信息
     * 		5.3 获得图书信息,为借阅信息设置图书信息
     * 		5.4 获得管理员信息,为借阅信息设置操作的管理员信息
     *
     * 6. 存储借阅信息
     *
     *
     *
     * 7. 借阅的书籍的在馆数量需要减少
     *
     * 8. 生成归还记录
     *
     */
    @Override
    public int addBorrow(BorrowInfo info) {
        Reader reader = readerDao.getReaderByCardID(info.getReader());
        String pwd = info.getReader().getPassword();
        if (reader == null) {
            //读者证号有误
            return 2;
        }
        Book book = bookDao.getBookByISBN(info.getBook());
        if (book == null) {
            //图书ISBN号有误
            return 3;
        }
        //查看读者输入的密码是否匹配
        if (!reader.getPassword().equals(pwd)){
            //密码有误
            return -1;
        }
        //查看该读者的借阅信息
        List<BorrowInfo> borrowInfoList = borrowDao.getNoBackBorrowInfoByReader(reader);
        //查看该读者的类型得出该读者的最大借阅数量
        if (borrowInfoList != null && borrowInfoList.size() >= reader.getReaderType().getMaxNum()) {
            return -2;
        }
        // 查看读者的罚款信息,是否有未缴纳的罚款
        List<ForfeitInfo> forfeitInfoList = forfeitDao.getForfeitByReader(reader);
        for (ForfeitInfo forfeitInfo : forfeitInfoList) {
            if (forfeitInfo.getIsPay() == 0) {
                return -3;
            }
        }
        //查看该书籍的在馆数量是否大于1,,如果为1则不能借阅,必须留在馆内浏览
        if (book.getNumInLibrary() <= 1) {
            return -4;
        }
        //得到该读者的最大借阅天数,和每日罚金
        int maxDay = reader.getReaderType().getBackDay();
        double penalty = reader.getReaderType().getPenalty();
        //获得当前时间
        Date borrow_date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(borrow_date);
        //根据最大借阅天数,计算出截止日期
        calendar.add(Calendar.DAY_OF_MONTH, maxDay);
        Date back_day = calendar.getTime();
        //获得该读者的信息,为借阅信息设置读者信息,图书信息,管理员信息
        BorrowInfo borrowInfo = new BorrowInfo();
        borrowInfo.setAdmin(info.getAdmin());
        borrowInfo.setBook(book);
        borrowInfo.setBorrowDate(borrow_date);
        borrowInfo.setPenalty(penalty);
        borrowInfo.setReader(reader);
        borrowInfo.setReturnDate(back_day);

        int id = borrowDao.addBorrow(borrowInfo);
        int state = 0;
        if (id != 0) {
            //借阅的书籍的在馆数量需要减少
            book.setNumInLibrary(book.getNumInLibrary()-1);
            bookDao.updateBookInfo(book);
            BackInfo info2 = new BackInfo();
            BorrowInfo borrowInfo2 = new BorrowInfo();
            borrowInfo2.setBorrowId(id);
            info2.setBorrowInfo(borrowInfo2);
            info2.setBorrowId(id);
            state = backDao.addBack(info2);
        }
        return state;
    }

    /*
     *	1.得到所有的未归还的借阅记录(包括未归还,逾期未归还,续借未归还,续借逾期未归还)
     *
     * 	2.遍历所有的未归还的借阅记录(包括未归还,逾期为归还,续借为归还,续借逾期未归还)
     *
     * 	3.查看当前时间和借阅的截止的时间的大小
     *		3.1 如果小于,直接跳过
     *		3.2如果大于则需要进行逾期处理的,进行第4步操作
     *
     *	4.用当前时间减去截止时间得到逾期的天数
     *
     *	5.为当前借阅记录设置逾期天数,并进行数据库修改
     *
     *	6.需要生成罚金记录
     *		6.1 得到当前借阅记录的罚金金额,和当前的逾期天数进行相乘,得到罚金金额
     *		6.2 将当前借阅记录的id和罚金的金额设置进新生成的罚金记录
     *
     *	7.设置当前借阅记录的状态
     *		7.1 如果是未归还，则改为逾期未归还
     *		7.2如果是续借未归还，则改为续借逾期未归还
     *		7.3如果是逾期未归,则不改变
     *		7.4如果是续借逾期不归还,则不改变
     *
     */
    @Override
    public boolean checkBorrowInfo() {
        List<BorrowInfo> borrowInfos = borrowDao.getBorrowInfoByNoBackState();
        if (borrowInfos != null) {
            for (BorrowInfo borrowInfo : borrowInfos) {
                //查看当前时间和借阅的截止的时间的大小
                long current_date = System.currentTimeMillis();
                long return_date = borrowInfo.getReturnDate().getTime();
                if (current_date > return_date) {
                    //用当前时间减去截止时间得到逾期的天数
                    Double over_days = Math.floor((current_date-return_date)/(24*60*60*1000));
                    int overdays = over_days.intValue();
                    //为当前借阅记录设置逾期天数
                    borrowInfo.setOverday(overdays);
                    //如果是未归还，则改为逾期未归还
                    if (borrowInfo.getState() == 0) {
                        borrowInfo.setState(1);
                        //如果是续借未归还，则改为续借逾期未归还
                    } else if (borrowInfo.getState() == 3) {
                        borrowInfo.setState(4);
                    }
                    //进行数据库修改
                    borrowDao.updateBorrowInfo(borrowInfo);
                    //得到当前借阅记录的罚金金额,和当前的逾期天数进行相乘,得到罚金金额
                    ForfeitInfo forfeitInfo = new ForfeitInfo();
                    forfeitInfo.setBorrowId(borrowInfo.getBorrowId());
                    forfeitInfo.setBorrowInfo(borrowInfo);
                    double penalty = overdays * borrowInfo.getPenalty();
                    forfeitInfo.setForfeit(penalty);
                    //将当前借阅记录的id和罚金的金额设置进新生成的罚金记录
                    if (!forfeitDao.addForfeitInfo(forfeitInfo)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /*
     * 1. 得到借阅的记录
     *
     * 2. 得到借阅的记录的状态
     * 		2.1 如果已经是续借状态(包括续借未归还,续借逾期未归还),则返回已经续借过了,返回-2
     * 		2.2 如果是归还状态(包括归还,续借归还),则返回该书已还,无法续借,返回-1
     *		2.3 如果都不是以上情况,则进行下一步。
     *
     * 3. 得到借阅的读者
     *
     * 4. 得到借阅的读者类型
     *
     * 5. 得到可续借的天数
     *
     * 6. 在当前记录的截止日期上叠加可续借天数
     * 		6.1 如果叠加后的时候比当前时间小,则返回你已超续借期了,无法进行续借,提示读者快去还书和缴纳罚金  返回-3
     * 		6.2如果大于当前时间进行下一步
     *
     * 7. 得到当前借阅记录的状态
     * 		7.1 如果当前记录为逾期未归还,则需要取消当前借阅记录的罚金记录,并将该记录的状态设置为续借未归还
     * 		7.2 如果为未归还状态,直接将当前状态设置为续借未归还
     *
     * 8. 为当前借阅记录进行设置,设置之后重新update该记录 返回1
     */
    @Override
    public int renewBook(BorrowInfo borrowInfo) {
        //得到借阅的记录
        BorrowInfo borrowInfoById = borrowDao.getBorrowInfoById(borrowInfo);
        //如果已经是续借状态(包括续借未归还,续借逾期未归还),则返回已经续借过了,返回-2
        if (borrowInfoById.getState() == 3 || borrowInfoById.getState() == 4) {
            return -2;
        }
        //如果是归还状态(包括归还,续借归还),则返回该书已还,无法续借,返回-1
        if (borrowInfoById.getState() == 2 || borrowInfoById.getState() == 5) {
            return -1;
        }
        //得到借阅的读者可续借的天数
        Integer renewdays = borrowInfoById.getReader().getReaderType().getRenewDay();
        //在当前记录的截止日期上叠加可续借天数
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(borrowInfoById.getReturnDate());
        calendar.add(Calendar.DAY_OF_MONTH, +renewdays);
        //如果叠加后的时候比当前时间小,则返回你已超续借期了,无法进行续借,提示读者快去还书和缴纳罚金  返回-3
        Date endDate = calendar.getTime();
        borrowInfoById.setReturnDate(endDate);
        if (endDate.getTime() < System.currentTimeMillis()) {
            return -3;
        }
        //如果当前记录为逾期未归还,则需要取消当前借阅记录的罚金记录,并将该记录的状态设置为续借未归还
        if (borrowInfoById.getState() == 1) {
            ForfeitInfo forfeitInfo = new ForfeitInfo();
            forfeitInfo.setBorrowId(borrowInfoById.getBorrowId());
            if (!forfeitDao.deleteForfeitInfo(forfeitInfo)) {
                return 0;
            }
        }
        borrowInfoById.setState(3);
        BorrowInfo updateBorrowInfo = borrowDao.updateBorrowInfo(borrowInfoById);
        if (borrowInfo != null) {
            return  1;
        }
        return 0;
    }
}

