package Shared;
import java.io.Serializable;

public class RequestObj implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Action {
        ADD_INQUIRY, GET_ALL, TEST
    }

    private Action action;
    private Object params;

    public RequestObj(Action action, Object params) {
        this.action = action;
        this.params = params;
    }

    public Action getAction() { return action; }
    public Object getParams() { return params; }
}