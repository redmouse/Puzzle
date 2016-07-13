package hello;
 
import java.util.*;
 
import javax.servlet.http.HttpServletResponse;
 
import com.opensymphony.xwork2.ActionSupport;
 
import org.apache.struts2.interceptor.ServletResponseAware;
 
import org.apache.struts2.interceptor.SessionAware;
 
public class AbstractAction extends ActionSupport implements ServletResponseAware,SessionAware {
 
    private static final long serialVersionUID = 1L;
 
    public HttpServletResponse response;
 
    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }
 
    public Map sessionMap;
 
    public void setSession(Map sessionMap) {
        this.sessionMap = sessionMap;
    }
}