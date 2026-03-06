package Data;

import java.io.File;
import java.time.LocalDateTime;

public class Request extends Inquiry{
    private Integer code;
    private String description;
    private LocalDateTime creationDate;

    public Request(String description) {
        this.description = description;
        this.code=nextCodeVal++;
        this.creationDate=LocalDateTime.now();
    }

    public void fillDataByUser(Integer code,String description){
        this.code=code;
        this.description=description;
        this.creationDate=LocalDateTime.now();
    }

    @Override
    public void handling(){
        System.out.println("The system is currently processing a request number "+this.code+".......");
    }

    @Override
    public String getFolderName() {
        File folder=new File("request");
        if(!folder.exists())
            folder.mkdir();
        return folder.getName();
    }

    @Override
    public String getFileName() {
        return this.code.toString();
    }

    @Override
    public String getData() {
        return this.description;
    }
}
