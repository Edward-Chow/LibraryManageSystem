package domain;

import java.io.Serializable;

public class ReaderType implements Serializable {

    private Integer readerTypeId;
    private String readerTypeName;
    private Integer maxNum; //最大借书数量
    private Double penalty; //逾期每日罚金
    private Integer backDay; //最大借书天数
    private Integer renewDay; //续借天数

    public Integer getReaderTypeId() {
        return readerTypeId;
    }

    public void setReaderTypeId(Integer readerTypeId) {
        this.readerTypeId = readerTypeId;
    }

    public String getReaderTypeName() {
        return readerTypeName;
    }

    public void setReaderTypeName(String readerTypeName) {
        this.readerTypeName = readerTypeName;
    }

    public Integer getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(Integer maxNum) {
        this.maxNum = maxNum;
    }

    public Double getPenalty() {
        return penalty;
    }

    public void setPenalty(Double penalty) {
        this.penalty = penalty;
    }

    public Integer getBackDay() {
        return backDay;
    }

    public void setBackDay(Integer backDay) {
        this.backDay = backDay;
    }

    public Integer getRenewDay() {
        return renewDay;
    }

    public void setRenewDay(Integer renewDay) {
        this.renewDay = renewDay;
    }
}
