package Shared;
import java.time.LocalDateTime;
import java.util.List;

public class Question extends Inquiry{
    public Question(){}
    public Question(String description) {
        this.description = description;
        this.code=getNextCodeVal();
        this.creationDate=LocalDateTime.now();
        this.status = Status.OPEN;
    }
    public Question(Integer code, String description) {
        this.code=code;
        this.description=description;
        this.creationDate=LocalDateTime.now();
        this.status = Status.OPEN;
    }
    public void fillDataByUser(Integer code, String description,LocalDateTime creationDate){
        this.code=code;
        this.description=description;
        this.creationDate=creationDate;
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
        return this.code+","+this.description+","+this.creationDate.toString();
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
                , LocalDateTime.parse(values.get(3))
        );
    }
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
