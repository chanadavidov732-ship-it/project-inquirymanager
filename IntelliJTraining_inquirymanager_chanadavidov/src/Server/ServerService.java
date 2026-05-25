package Server;

import HandleStoreFiles.HandleFiles;
import HandleStoreFiles.HandleFilesReflection;
import Shared.*;
import business.InquiryManager;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Queue;

public class ServerService {
    private InquiryManager manager;

    public ServerService() {
        this.manager = new InquiryManager();
    }

    public ResponseObj handleRequest(RequestObj request) {
        try {
            if (request == null) {
                return new ResponseObj(400, "FAILED", "NULL REQUEST");
            }
            switch (request.getAction()) {
                case ADD_INQUIRY:
                    Inquiry inquiry = (Inquiry) request.getParams();
                    HandleFiles im = new HandleFiles();
                    InquiryManager.getQInquiry().add(inquiry);
                    im.saveFile(inquiry);
                    InquiryManager.saveNextValFile();
                    return new ResponseObj(200, "SUCCESS", inquiry.getCode());

                case GET_ALL:
                    List<Inquiry> all = manager.getAllInquiriesForClient();
                    return new ResponseObj(200, "SUCCESS", all);

                case GET_INQUIRY_STATUS:
                    int code = (int) request.getParams();
                    return new ResponseObj(200, "SUCCESS",getStatusForClient(code));

//                case TEST:
//                    return new ResponseObj(200, "SUCCESS", "SERVER_READY");

                case GET_COUNT_BY_MONTH:
                    try {
                        if (request.getParams() == null) {
                            return new ResponseObj(400, "FAILED", "Missing month parameter");
                        }

                        int month;
                        if (request.getParams() instanceof Integer) {
                            month = (Integer) request.getParams();
                        } else {
                            month = Integer.parseInt(request.getParams().toString().trim());
                        }
                        int totalCount = manager.getInquiryCountByMonth(month);

                        return new ResponseObj(200, "SUCCESS", totalCount);
                    } catch (Exception ex) {
                        return new ResponseObj(400, "FAILED", "Invalid month parameter or system error: " + ex.getMessage());
                    }

                case CANCEL_INQUIRY:
                    int id = (int) request.getParams();
                    return manager.cancelInquiry(id);
                    default:
                    return new ResponseObj(400, "FAILED", "Action not supported");

                case AGENT_LOGIN:
                    Representative rep = (Representative) request.getParams();
                    return new ResponseObj(200, "SUCCESS", "Agent logged in successfully");
                case AGENT_LOGOUT:
                    // השרת מוציא את ה-ID ששלחת וממיר אותו ישירות למספר
                    int idToLogout = (Integer) request.getParams();

                    // כאן הקוד שלכן שמנתק את הסוכן, משהו כמו:
                    // manager.agentLogout(idToLogout);

                    return new ResponseObj(200, "SUCCESS", "Agent logged out successfully");
                case ADD_AGENT:
                    Representative rep1 = (Representative) request.getParams();
                    manager.defineRepresentative(rep1.getName(), rep1.getId());
                    return new ResponseObj(200, "SUCCESS", "Agent added successfully");
                case REMOVE_AGENT:
                    int idToRemove = (Integer) request.getParams();
                    manager.deleteRepresentative(idToRemove);
                    return new ResponseObj(200, "SUCCESS", "Agent removed successfully");
            }
        }
        catch (Exception e) {
            return new ResponseObj(500, "FAILED", e.getMessage());
        }
    }

    public String getStatusForClient(int status){
        Queue<Inquiry> qInquiry= InquiryManager.getQInquiry();
        for(Inquiry n:qInquiry) {
            if (n.getCode() == status)
                //לדאוג אכן בכל טיפול בפניה לעדכן למשל this.status = Status.OPEN;
                return n.getStatus().toString();
        }
        return getStatusHistory(status);
    }

    public String getStatusHistory(int status) {
        HandleFilesReflection hfr=new HandleFilesReflection();
        Inquiry o= null;
        try {
            o = (Inquiry) hfr.readCsv(
                    "DATA.HISTORY/" + status);
        } catch (FileNotFoundException e){
           return "inquiry code:"+status+" not found";}
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return o.getStatus().toString();
    }
}