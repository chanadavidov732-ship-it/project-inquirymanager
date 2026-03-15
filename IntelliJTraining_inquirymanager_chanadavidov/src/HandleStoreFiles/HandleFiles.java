package HandleStoreFiles;

import Data.Complaint;
import Data.Question;
import Data.Request;
import testSaveFile.PersonForTestSaving;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static business.InquiryManager.getQInquiry;

public class HandleFiles {

    public void readFile(IForSaving iForSaving) throws FileNotFoundException {
        List<String> values=new ArrayList<>();
        File dataFile = new File(iForSaving.getFolderName(),iForSaving.getFileName());
        if(!dataFile.exists())
            return;
        Scanner scanner =new Scanner(dataFile);
        int x;
        while(scanner.hasNextLine()){
            String data=scanner.nextLine();
            String[] parts = data.split(",");
            for (String part : parts) {
                 values.add(part.trim());
            }
        }
        iForSaving.parseFromFile(values);
    }

    public void saveFile(IForSaving iForSaving) throws IOException {
        File folder = new File(iForSaving.getFolderName());
        if (!folder.exists())
            folder.mkdirs();
        File dataFile = new File(folder, iForSaving.getFileName() + ".txt");
        FileWriter writer = new FileWriter(dataFile, false);
        writer.write("inquiry type: "+ iForSaving.getFolderName()+
                ", number inquiry: "+ iForSaving.getFileName()+
                ", description: "+ iForSaving.getData());
        writer.flush();
        writer.close();
        System.out.println("finish saveFile");
    }

    public void deleteFile(IForSaving iForSaving){
        File dataFile = new File(iForSaving.getFolderName(), iForSaving.getFileName() + ".txt");
        if (dataFile.exists()) {
            dataFile.delete();
        }
        System.out.println("deleteFile      "+ iForSaving.getFileName());
    }

    public void updateFile(IForSaving iForSaving) throws IOException {
       saveFile(iForSaving);
        System.out.println("updateFile    "+ iForSaving.getFileName());
    }

    public void saveFiles(List<IForSaving> IForSavingList) throws IOException {
        for(IForSaving i: IForSavingList)
            saveFile(i);
        System.out.println("saveFiles");
    }

}
