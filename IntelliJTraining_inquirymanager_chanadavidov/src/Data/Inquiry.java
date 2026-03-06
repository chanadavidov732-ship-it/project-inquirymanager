package Data;

import HandleStoreFiles.ForSaving;

public class Inquiry implements ForSaving{
    public static Integer nextCodeVal = 0;

    public static Integer getNextCodeVal() {
        return nextCodeVal;
    }

    public void handling() {
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
}
