package Data;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public class Inquiry {
    public static Integer nextCodeVal = 1;
    private Integer code;
    private String description;
    private LocalDateTime creationDate;

    public static Integer getNextCodeVal() {
        return nextCodeVal;
    }

    public void handling() { }


}
