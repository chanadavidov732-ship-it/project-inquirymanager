package Shared;

import HandleStoreFiles.HandleFiles;
import HandleStoreFiles.IForSaving;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public abstract class Inquiry implements IForSaving, Serializable {
    private static final long serialVersionUID = 1L;
    public static Integer nextCodeVal = 1;
    protected Integer code;
    protected String description;
    protected LocalDateTime creationDate;
    protected Status status;

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public String getDescription() {
        return description;
    }

    public enum Status{
        OPEN,CANCELED,IN_TREATMENT, TREATED
    }
    public Status getStatus(){return status;}
    public void setStatus(Status newStatus){
        this.status = newStatus;
        if(this.status == Status.CANCELED || this.status == Status.TREATED)
            transferToHistory();
    }
    public void transferToHistory(){
        HandleFiles handleFiles = new HandleFiles();
        File file = new File(this.getClass().getName()+"/"+this.getFileName());
        if(file.delete()) {
            try {
                handleFiles.saveFile(this);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static Integer getNextCodeVal() {
        return nextCodeVal++;
    }


    public int getCode() {
        if (code==null)
            return  0;
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }

//    @Override
//    public String getFolderName() {
//        File folder=new File(getClass().getName());
//        if(!folder.exists())
//            folder.mkdir();
//        return folder.getName();
//    }
    @Override
    public String getFolderName() {
        if(this.status == Status.CANCELED || this.status == Status.TREATED)
            return "history";
        File folder=new File(getClass().getName());
        if(!folder.exists())
            folder.mkdir();
        return folder.getName();
    }

    public abstract void handling() ;
    public abstract String getFileName();
    public abstract String getData() ;
    public abstract void parseFromFile(List<String> values);
    // the order of items:values[0]= type of inquiry
    // values[1]= code,values[2]= description
    // values[3]=creationDate
    // values[4]=assignedBranch
}
