package quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import service.BorrowService;

public class CheckBorrowInfo extends QuartzJobBean {

    private BorrowService borrowService;

    public void setBorrowService(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    public CheckBorrowInfo() {
        super();
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        boolean checkBorrowInfo = true;
        try {
            checkBorrowInfo = borrowService.checkBorrowInfo();
        } catch (Throwable e){
            e.printStackTrace();
        }
        if (!checkBorrowInfo) {
            System.err.print("定时检查功能出错！");
        }
    }
}
