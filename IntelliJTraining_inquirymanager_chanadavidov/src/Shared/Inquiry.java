package Shared;

import HandleStoreFiles.IForSaving;
import business.InquiryManager;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Inquiry implements IForSaving, Serializable {
    public static Integer nextCodeVal = 1;
    private Integer code;
    private String description;
    private LocalDateTime creationDate;

    public static Integer getNextCodeVal() {
        return nextCodeVal++;
    }

    public void handling() { }

    public int getCode() {
        return 0;
    }

    @Override
    public String getFolderName() {
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
        //the order of items:values[1]= type of inquiry,values[1]= code,values[2]= dsceription , values[3]=creationDate ,values[4]=assignedBranch

        String folderN=values.get(0);

        //i adds an empty constructor on all inquirys
        if(folderN.contains("Request")) {
            Request newIn=new Request();
            newIn.fillDataByUser(Integer.parseInt(values.get(1)),values.get(2));
            InquiryManager.getQInquiry().add(newIn);
        }
        else if(folderN.contains("Question")){
            Question newIn=new Question();
            newIn.fillDataByUser(Integer.parseInt(values.get(1)),values.get(2));
            InquiryManager.getQInquiry().add(newIn);
        }
        else if (folderN.contains("Complaint")){
            Complaint newIn=new Complaint();
            newIn.fillDataByUser(Integer.parseInt(values.get(1)),values.get(2),values.get(4));
            InquiryManager.getQInquiry().add(newIn);
        }

    }
}
