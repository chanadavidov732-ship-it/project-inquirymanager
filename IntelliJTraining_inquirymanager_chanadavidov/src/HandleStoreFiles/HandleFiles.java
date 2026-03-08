package HandleStoreFiles;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class HandleFiles {

    public void saveFile(ForSaving forSaving) throws IOException {

        File folder = new File(forSaving.getFolderName());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File dataFile = new File(folder, forSaving.getFileName() + ".txt");
        FileWriter writer = new FileWriter(dataFile, false);
        writer.write("inquiry type: "+forSaving.getFolderName()+", description: "+forSaving.getData()+", number inquiry: "+forSaving.getFileName());
        writer.close();
        System.out.println("saveFile     "+forSaving.getFileName());
    }

    public void deleteFile(ForSaving forSaving){
        File dataFile = new File(forSaving.getFolderName(), forSaving.getFileName() + ".txt");
        if (dataFile.exists()) {
            dataFile.delete();
        }
        System.out.println("deleteFile      "+forSaving.getFileName());

    }

    public void updateFile(ForSaving forSaving) throws IOException {
       saveFile(forSaving);
        System.out.println("updateFile    "+forSaving.getFileName());

    }

    public void saveFiles(List<ForSaving> forSavingList) throws IOException {
        for(ForSaving i:forSavingList)
            saveFile(i);
        System.out.println("saveFiles");

    }
}
