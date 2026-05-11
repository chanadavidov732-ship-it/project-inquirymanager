package Shared;

import HandleStoreFiles.HandleFiles;
import HandleStoreFiles.IForSaving;
import business.InquiryManager;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Inquiry implements IForSaving, Serializable {
    private static final long serialVersionUID = 1L;
    public static Integer nextCodeVal = 1;
    private Integer code;
    private String description;
    private LocalDateTime creationDate;
    private Status status;

    public enum Status{
        OPEN,CANCELED,IN_TREATMENT, TREATED
    }
    public Status getStatus(){return status;}
    public void setStatus(Status newStatus){
        InquiryManager manager = new InquiryManager();
        this.status = newStatus;
        if(this.status == Status.CANCELED || this.status == Status.TREATED)
            manager.transferToHistory(this);
    }
    public static Integer getNextCodeVal() {
        return nextCodeVal++;
    }

    public void handling() { }

    public int getCode() {
        if (code==null)
            return  0;
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }
    @Override
    public String getFolderName() {
        if(this.status == Status.CANCELED || this.status == Status.TREATED)
            return "history";
        File folder=new File(getClass().getName());
        if(!folder.exists())
            folder.mkdir();
        return folder.getName();
    }

    @Override
    public String getFileName() {
        return "";
    }

    @Override
    public String getData() {
        return "";
    }

    @Override
    public void parseFromFile(List<String> values) {
        //the order of items:values[1]= type of inquiry,values[1]= code,values[2]= description , values[3]=creationDate ,values[4]=assignedBranch

        String folderN=values.get(0);
        if (this instanceof Request) {
            //or leave the code with .replace("creationDate: ", "") or to delete on function getData()
            ((Request)this).fillDataByUser(Integer.parseInt(values.get(1).replace("creationDate: ", "")), values.get(2).replace("description: ", ""));
        }
        else if (this instanceof Question) {
            ((Question)this).fillDataByUser(Integer.parseInt(values.get(1).replace("creationDate: ", "")), values.get(2).replace("description: ", ""));
        }
        else if (this instanceof Complaint) {
            ((Complaint)this).fillDataByUser(
                    Integer.parseInt(values.get(1).replace("creationDate: ", "")),
                    values.get(2).replace("description: ", ""),
                    values.get(4) );
        }

    }
}
