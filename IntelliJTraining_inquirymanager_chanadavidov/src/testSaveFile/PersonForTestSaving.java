package testSaveFile;

import HandleStoreFiles.ForSaving;

public class PersonForTestSaving implements ForSaving {

    String id;
    String name;

    public PersonForTestSaving(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getFolderName() {
        return getClass().getPackageName();
    }

    public String getFileName() {
        return getClass().getSimpleName() + id;
    }

    public String getData() {
        return id + "," + name;
    }
}
