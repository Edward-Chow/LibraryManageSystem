package domain;

import java.io.Serializable;
import java.util.List;

//分页类
public class PageBean<T> implements Serializable {

    private int pageCode;//当前页码
    private int totaPage;//总页数
    private int totalRecord;//总记录数
    private int pageSize;//每页记录数
    private List<T> beanList;// 当前页记录数
    private String url;	//url的条件

    public int getPageCode() {
        return pageCode;
    }

    public void setPageCode(int pageCode) {
        this.pageCode = pageCode;
    }

    public int getTotaPage() {
        int tp = totalRecord / pageSize;
        return totalRecord%pageSize==0 ? tp : tp+1;
    }

    public void setTotaPage(int totaPage) {
        this.totaPage = totaPage;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<T> beanList) {
        this.beanList = beanList;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
