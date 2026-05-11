package Shared;
import HandleStoreFiles.IForSaving;
import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public abstract class Inquiry implements IForSaving, Serializable {
    private static final long serialVersionUID = 1L;
    public static Integer nextCodeVal = 1;
    protected Integer code;
    protected String description;
    protected LocalDateTime creationDate;
    protected Status status;

    public enum Status{
        OPEN,CANCELED,IN_TREATMENT,IN_HISTORY
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus(){return status;}

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
        return getClass().getName();
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
