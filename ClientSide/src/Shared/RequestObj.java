package Shared;
import java.io.Serializable;
public class RequestObj  implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Action {
        ADD_INQUIRY, GET_ALL,GET_INQUIRY_STATUS, TEST,GET_COUNT_BY_MONTH,CANCEL_INQUIRY,
        AGENT_LOGIN, AGENT_LOGOUT, ADD_AGENT, REMOVE_AGENT  // הפעולות החדשות שלך!
    }

    private Action action;
    private Object object;

    public RequestObj(Action action, Object obj) {
        this.action = action;
        this.object = obj;
    }

    public Action getAction() { return action; }
    public Object getParams() { return object; }
}