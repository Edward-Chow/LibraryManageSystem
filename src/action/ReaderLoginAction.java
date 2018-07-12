package action;

import com.opensymphony.xwork2.ActionSupport;
import domain.Reader;
import org.apache.struts2.ServletActionContext;
import service.ReaderService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ReaderLoginAction extends ActionSupport{
    private ReaderService readerService;

    public void setReaderService(ReaderService readerService) {
        this.readerService = readerService;
    }

    private String cardId;
    private String password;

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    // Ajax异步请求获得登录许可
    //返回登陆状态
    public String login(){
        Reader reader = new Reader();
        reader.setCardId(cardId);
        reader.setPassword(password);
        int login = 1;
        Reader newreader = readerService.getReaderByCardID(reader);
        if (newreader == null) {
            login = -1;
        } else if (!reader.getPassword().equals(newreader.getPassword())) {
            login = -2;
        } else {
            ServletActionContext.getContext().getSession().put("reader", newreader);
        }
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.getWriter().print(login);

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    public String logout(){
        ServletActionContext.getContext().getSession().remove("reader");
        return "logout";
    }

}
