package testSaveFile;

import HandleStoreFiles.IForSaving;

import java.util.List;

public class PersonForTestSaving implements IForSaving {

    String id;
    String name;

    public PersonForTestSaving(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
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

    @Override
    public void parseFromFile(List<String> values) {

    }
}
