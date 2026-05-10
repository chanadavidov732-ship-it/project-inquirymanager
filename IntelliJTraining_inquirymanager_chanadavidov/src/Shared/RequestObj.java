package Shared;
import java.io.Serializable;

//public class RequestObj implements Serializable {
//    private static final long serialVersionUID = 1L;
//
//    public enum Action {
//        ADD_INQUIRY, GET_ALL, TEST
//    }
//
//    private Action action;
//    private Object params;
//
//    public RequestObj(Action action, Object params) {
//        this.action = action;
//        this.params = params;
//    }
//
//    public Action getAction() { return action; }
//    public Object getParams() { return params; }
//}

public class RequestObj  implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Action {
        ADD_INQUIRY, GET_ALL,GET_INQUIRY_STATUS, GET_COUNT_BY_MONTH
    }

    private Action action;
    private Inquiry object;

    public RequestObj(Action action, Inquiry obj) {
        this.action = action;
        this.object = obj;
    }

    public Action getAction() { return action; }
    public Inquiry getParams() { return object; }
}