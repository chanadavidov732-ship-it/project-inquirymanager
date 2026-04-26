package clientData;

import Data.Inquiry;

import java.io.Serializable;

public class RequestObj  implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Action {
        ADD_INQUIRY, GET_ALL, TEST
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