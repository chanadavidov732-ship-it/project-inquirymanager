package Shared;

import HandleStoreFiles.IForSaving;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Request extends Inquiry implements IForSaving, Serializable {
    private Integer code;
    private String description;
    private LocalDateTime creationDate;

    public Request(){}

    public Request(String description) {
        this.description = description;
        this.code=getNextCodeVal();
        this.creationDate=LocalDateTime.now();
    }
    public Request(Integer code,String description) {
        this.code=code;
        this.description=description;
        //this.creationDate=creationDate;
    }
    public void fillDataByUser(Integer code,String description){//,LocalDateTime creationDate){
        this.code=code;
        this.description=description;
        //this.creationDate=creationDate;
    }
    @Override
    public int getCode() {
        return code == null ? 0 : code;
    }

    @Override
    public void handling(){
        System.out.println("The system is currently processing a request number "+this.code+".......");
    }
    @Override
    public String getFileName() {
        return String.valueOf(code);
    }

    @Override
    public String getData() {
        return "description: "+description+", creationDate: "+creationDate;
    }
}
