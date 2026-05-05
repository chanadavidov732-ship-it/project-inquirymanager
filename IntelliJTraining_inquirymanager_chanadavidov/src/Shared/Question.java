package Shared;

import HandleStoreFiles.IForSaving;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Question extends Inquiry implements IForSaving, Serializable {
    private Integer code;
    private String description;
    private LocalDateTime creationDate;

    public Question(){}
    public Question(String description) {
        this.description = description;
        this.code=getNextCodeVal();
        this.creationDate=LocalDateTime.now();
    }
    public Question(Integer code,String description) {
        this.code=code;
        this.description=description;
        //this.creationDate=creationDate;
    }
    public void fillDataByUser(Integer code, String description){
        this.code=code;
        this.description=description;
        //this.creationDate=creationDate;
    }
    @Override
    public void handling(){
        System.out.println("The system is currently processing a question number "+this.code+".......");
    }
    @Override
    public String getFileName() {
        return String.valueOf(code);
    }

    @Override
    public String getData() {
        return "description: "+description+", creationDate: "+creationDate;
    }
    @Override
    public int getCode() {
        return code == null ? 0 : code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
