package Server;

import Shared.*;
import business.InquiryManager;
import java.util.List;

public class ServerService {
    private InquiryManager manager;

    public ServerService() {
        this.manager = new InquiryManager();
    }

    public ResponseObj handleRequest(RequestObj request) {
        try {
            switch (request.getAction()) {
                case ADD_INQUIRY:
                    Inquiry inquiry = (Inquiry) request.getParams();
                    // שימוש בפונקציה החדשה שיצרנו
                    manager.addInquiryFromClient(inquiry);
                    return new ResponseObj(200, "SUCCESS", inquiry.getCode());

                case GET_ALL:
                    List<Inquiry> all = manager.getAllInquiriesForClient();
                    return new ResponseObj(200, "SUCCESS", all);

                case TEST:
                    return new ResponseObj(200, "SUCCESS", "SERVER_READY");

                default:
                    return new ResponseObj(400, "FAILED", "Action not supported");
            }
        } catch (Exception e) {
            return new ResponseObj(500, "FAILED", e.getMessage());
        }
    }
}