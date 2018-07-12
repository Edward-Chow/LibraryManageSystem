package action;

import com.opensymphony.xwork2.ActionSupport;
import domain.ReaderType;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import service.ReaderTypeService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ReaderTypeManageAction extends ActionSupport {
    private ReaderTypeService readerTypeService;

    public void setReaderTypeService(ReaderTypeService readerTypeService) {
        this.readerTypeService = readerTypeService;
    }

    private Integer id;
    private Integer maxNum;
    private Integer bday;
    private Double penalty;
    private String readerTypeName;
    private Integer renewDays;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setMaxNum(Integer maxNum) {
        this.maxNum = maxNum;
    }

    public void setBday(Integer bday) {
        this.bday = bday;
    }

    public void setPenalty(Double penalty) {
        this.penalty = penalty;
    }

    public void setReaderTypeName(String readerTypeName) {
        this.readerTypeName = readerTypeName;
    }

    public void setRenewDays(Integer renewDays) {
        this.renewDays = renewDays;
    }

    public String getAllReaderType(){

        List<ReaderType> allReaderType = readerTypeService.getAllReaderType();
        HttpServletRequest request = ServletActionContext.getRequest();
        request.setAttribute("readerTypes", allReaderType);
        return "success";
    }


    /**
     * 得到指定的读者类型信息
     * @return
     */
    public String getReaderType(){
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        ReaderType readerType = new  ReaderType();
        readerType.setReaderTypeId(id);
        ReaderType newReaderType = readerTypeService.getTypeById(readerType);
        JSONObject jsonObject = JSONObject.fromObject(newReaderType);
        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


    public String updateReaderType(){
        ReaderType readerType = new ReaderType();
        readerType.setReaderTypeId(id);
        ReaderType updateReaderType = readerTypeService.getTypeById(readerType);
        updateReaderType.setReaderTypeName(readerTypeName);
        updateReaderType.setMaxNum(maxNum);
        updateReaderType.setBackDay(bday);
        updateReaderType.setPenalty(penalty);
        updateReaderType.setRenewDay(renewDays);
        ReaderType newReaderType = readerTypeService.updateReaderType(updateReaderType);
        int success = 0;
        if(newReaderType!=null){
            success = 1;
            //由于是转发并且js页面刷新,所以无需重查
        }
        try {
            ServletActionContext.getResponse().getWriter().print(success);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }



    public String addReaderType(){
        ReaderType readerType = new ReaderType();
        readerType.setReaderTypeName(readerTypeName);
        readerType.setPenalty(penalty);
        readerType.setMaxNum(maxNum);
        readerType.setBackDay(bday);
        readerType.setRenewDay(renewDays);
        boolean b = readerTypeService.addReaderType(readerType);
        int success = 0;
        if(b){
            success = 1;
        }
        try {
            ServletActionContext.getResponse().getWriter().print(success);//向浏览器响应是否成功的状态码
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


    public String getAllReaderTypes(){
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        List<ReaderType> allReaderTypes = readerTypeService.getAllReaderType();

        String json = JSONArray.fromObject(allReaderTypes).toString();//List------->JSONArray
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }
}
