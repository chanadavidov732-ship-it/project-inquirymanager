package Shared;

import HandleStoreFiles.IForSaving;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Complaint extends Inquiry  implements IForSaving, Serializable {
    private Integer code;
    private String description;
    private LocalDateTime creationDate;
    private String assignedBranch;

    public Complaint(){}
    public Complaint(String assignedBranch, String description) {
        this.code=nextCodeVal++;
        this.creationDate=LocalDateTime.now();
        this.assignedBranch = assignedBranch;
        this.description = description;
    }
    public Complaint(Integer code,String description,String assignedBranch) {
        this.code=code;
        this.description=description;
        this.assignedBranch = assignedBranch;
        //this.creationDate=creationDate;
    }

    public void fillDataByUser(Integer code, String description, String assignedBranch){
        this.code=code;
        this.description=description;
        //this.creationDate= creationDate;
        this.assignedBranch=assignedBranch;
    }

    @Override
    public void handling(){
        System.out.println("The system is currently processing a complaint number "+this.code+".......");
    }

    @Override
    public String getFileName() {
        return code.toString();
    }

    @Override
    public String getData() {
        return "description: "+description+", creationDate: "+creationDate+", assignedBranch: "+assignedBranch;
    }

    public String getAssignedBranch() {
        return assignedBranch;
    }

    public void setAssignedBranch(String assignedBranch) {
        this.assignedBranch = assignedBranch;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
