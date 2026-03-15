package Data;

import java.io.File;
import java.time.LocalDateTime;

public class Request extends Inquiry{
    private Integer code;
    private String description;
    private LocalDateTime creationDate;

    public Request(){}

    public Request(String description) {
        this.description = description;
        this.code=nextCodeVal++;
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
    public void handling(){
        System.out.println("The system is currently processing a request number "+this.code+".......");
    }
    @Override
    public String getFileName() {
        return code.toString();
    }

    @Override
    public String getData() {
        return "description: "+description+", creationDate: "+creationDate;
    }
}
