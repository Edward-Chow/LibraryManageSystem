package action;

import com.opensymphony.xwork2.ActionSupport;
import domain.Reader;
import org.apache.struts2.ServletActionContext;
import service.ReaderService;

import java.io.IOException;
import java.util.Map;


@SuppressWarnings("serial")
public class ReaderInfoAction extends ActionSupport {
    private ReaderService readerService;

    private String name;
    private String phone;
    private String email;
    private String oldPwd;
    private String newPwd;
    private String confirmPwd;

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public void setConfirmPwd(String confirmPwd) {
        this.confirmPwd = confirmPwd;
    }

    public void setReaderService(ReaderService readerService) {
        this.readerService = readerService;
    }

    public String readerInfo() {
        Map<String, Object> session = ServletActionContext.getContext().getSession();
        Reader reader = (Reader) session.get("reader");
        reader.setName(name);
        reader.setPhone(phone);
        reader.setEmail(email);
        Reader newReader = readerService.updateReaderInfo(reader);
        int success = 0;
        if (newReader != null) {
            success = 1;
            session.put("reader", newReader);
        }
        try {
            ServletActionContext.getResponse().getWriter().print(success);
        }catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    public String readerPwd() {
        Reader reader = (Reader) ServletActionContext.getContext().getSession().get("reader");
        int state = -1; //原密码错误
        if (reader.getPassword().equals(oldPwd)) {
            if (newPwd.equals(confirmPwd)) {
                state = 1;
                reader.setPassword(newPwd);
                reader = readerService.updateReaderInfo(reader);
                ServletActionContext.getContext().getSession().put("reader", reader);
            } else {
                state = 0;
            }
        }
        try {
            ServletActionContext.getResponse().getWriter().print(state);
        }catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }
}
