package Shared;

import java.time.LocalDateTime;
import java.util.List;

public class Complaint extends Inquiry  {

    private String assignedBranch;

    public Complaint(){}
    public Complaint(String assignedBranch, String description) {
        this.code=getNextCodeVal();
        this.creationDate=LocalDateTime.now();
        this.assignedBranch = assignedBranch;
        this.description = description;
        this.status = Status.OPEN;
    }
    public Complaint(Integer code,String description,String assignedBranch) {
        this.code=code;
        this.description=description;
        this.assignedBranch = assignedBranch;
        this.creationDate=LocalDateTime.now();
        this.status = Status.OPEN;
    }

    public void fillDataByUser(Integer code, String description, String assignedBranch,LocalDateTime creationDate){
        this.code=code;
        this.description=description;
        this.creationDate= creationDate;
        this.assignedBranch=assignedBranch;
    }

    @Override
    public void handling(){
        System.out.println("The system is currently processing a complaint number "+this.code+".......");
    }

    @Override
    public String getFileName() {
        return String.valueOf(code);
    }

    @Override
    public String getData() {
        return this.code+","+this.description+","+this.creationDate.toString()+","+this.assignedBranch;
    }

    @Override
    public int getCode() {
        return code == null ? 0 : code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void parseFromFile(List<String> values) {
        String folderN=values.get(0);
        (this).fillDataByUser(Integer.parseInt(values.get(1))
                , values.get(2)
                , values.get(3)
                , LocalDateTime.parse(values.get(3))
        );
    }
}
