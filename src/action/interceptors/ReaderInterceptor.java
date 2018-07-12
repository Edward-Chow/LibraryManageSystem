package action.interceptors;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import domain.Admin;
import domain.Authorization;
import org.apache.struts2.ServletActionContext;

import java.util.Map;

public class ReaderInterceptor implements Interceptor{
    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void init() {
        // TODO Auto-generated method stub

    }

    @Override
    public String intercept(ActionInvocation actionInvocation) throws Exception {
        Map sessionMap = ServletActionContext.getContext().getSession();

        Object obj = sessionMap.get("admin");
        if(obj!=null && obj instanceof Admin){
            Admin admin = (Admin) obj;
            Authorization authorization = admin.getAuthorization();
            if(authorization.getReaderSet()==1 || authorization.getSuperSet()==1){
                return actionInvocation.invoke();
            }
        }
        return "nopass";
    }
}
