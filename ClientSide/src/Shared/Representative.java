package Shared;

import HandleStoreFiles.IForSaving;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Representative  implements IForSaving, Serializable {
    private static final long serialVersionUID = 1L;
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
        return "Representatives";
    }

    @Override
    public String getFileName() {
        return String.valueOf(this.id);
    }

    @Override
    public String getData() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.code).append(",")
                .append(this.name).append(",")
                .append(this.id);

        if (this.handledInquiriesIds != null && !this.handledInquiriesIds.isEmpty()) {
            for (Integer inquiryId : this.handledInquiriesIds) {
                sb.append(",").append(inquiryId);
            }
        }
        return sb.toString();
    }
    @Override
    public void parseFromFile(List<String> values) {
        if (values == null || values.size() < 4) return;

        this.code = Integer.parseInt(values.get(1));
        this.name = values.get(2);
        this.id = Integer.parseInt(values.get(3));

        if (this.handledInquiriesIds == null) {
            this.handledInquiriesIds = new ArrayList<>();
        } else {
            this.handledInquiriesIds.clear();
        }

        for (int i = 4; i < values.size(); i++) {
            this.handledInquiriesIds.add(Integer.parseInt(values.get(i)));
        }
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

