package Shared;

import HandleStoreFiles.IForSaving;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Representative  implements IForSaving, Serializable {
    static int RepresentativeCode = 0;

    private int code;
    private String name;
    private int id;

    private List<Integer> handledInquiriesIds;
    public Representative() {
        this.handledInquiriesIds = new ArrayList<>();
        code = Representative.RepresentativeCode++;
    }

    public Representative(String name, int id) {
        code = Representative.RepresentativeCode++;
        this.name = name;
        this.id = id;
        this.handledInquiriesIds = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getFolderName() {
        return "";
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

    }
    public List<Integer> getHandledInquiriesIds() {
        return handledInquiriesIds;
    }

    public void setHandledInquiriesIds(List<Integer> handledInquiriesIds) {
        this.handledInquiriesIds = handledInquiriesIds;
    }

    public void addHandledInquiryId(int inquiryId) {
        if (this.handledInquiriesIds == null) {
            this.handledInquiriesIds = new ArrayList<>();
        }
        this.handledInquiriesIds.add(inquiryId);
    }
}
