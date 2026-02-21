package Data;

import java.time.LocalDateTime;

public class Question extends Inquiry{
    private Integer code;
    private String description;
    private LocalDateTime creationDate;


    public Question(String description) {
        this.description = description;
        this.code=nextCodeVal++;
        this.creationDate=LocalDateTime.now();
    }

    public void fillDataByUser(Integer code, String description){
        this.code=code;
        this.description=description;
        this.creationDate=LocalDateTime.now();
    }
    @Override
    public void handling(){
        System.out.println("The system is currently processing a question number "+this.code+".......");
    }


    public Integer getCode() {
        return code;
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
