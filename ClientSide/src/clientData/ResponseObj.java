package clientData;

import java.io.Serializable;

public class ResponseObj  implements Serializable {
    private static final long serialVersionUID = 1L;

    private int status;
    private String message;
    private Object result;

    public ResponseObj(int status, String message, Object result) {
        this.status = status;
        this.message = message;
        this.result = result;
    }

    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public Object getResult() { return result; }
}

