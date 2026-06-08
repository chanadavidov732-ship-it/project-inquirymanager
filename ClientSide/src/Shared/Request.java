package Shared;
import java.time.LocalDateTime;
import java.util.List;

public class Request extends Inquiry {

    public Request(){
        this.status = Status.OPEN;
    }

    public Request(String description) {
        this.description = description;
        this.code=getNextCodeVal();
        this.creationDate=LocalDateTime.now();
        this.status = Status.OPEN;
    }

    public Request(Integer code,String description) {
        this.code=code;
        this.description=description;
        this.creationDate=LocalDateTime.now();
        this.status = Status.OPEN;
    }

    public void fillDataByUser(Integer code,String description,LocalDateTime creationDate){
        this.code=code;
        this.description=description;
        this.creationDate=creationDate;
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
        return this.code+","+this.description+","+this.creationDate.toString();
    }

    @Override
    public void parseFromFile(List<String> values) {
        String folderN=values.get(0);
        //or leave the code with .replace("creationDate: ", "") or to delete on function getData()
        (this).fillDataByUser(Integer.parseInt(values.get(1))
                , values.get(2)
                , LocalDateTime.parse(values.get(3))
        );}
    @Override
    public String toString() {
        return "Question{" +
                "code=" + code +
                ", description='" + description + '\'' +
                ", creationDate=" + creationDate +
                ", status=" + status +
                '}';
    }
}
