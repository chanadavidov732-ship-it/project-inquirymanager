package HandleStoreFiles;

import Shared.Inquiry;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

//    public synchronized int getNextIdAndIncrement() {
//        int currentId = 1;
//        File idFile = new File("id_counter.txt");
//
//        try {
//            if (idFile.exists()) {
//                Scanner scanner = new Scanner(idFile);
//                if (scanner.hasNextInt()) {
//                    currentId = scanner.nextInt();
//                }
//                scanner.close();
//            }
//
//            FileWriter writer = new FileWriter(idFile, false);
//            writer.write(String.valueOf(currentId + 1));
//            writer.close();
//        } catch (IOException e) {
//            System.out.println("Error managing ID file: " + e.getMessage());
//        }
//        return currentId;
//    }

    public List<Inquiry> readAllInquiries() {
        List<Inquiry> allInquiries = new ArrayList<>();
        String[] folders = {"Shared.Request", "Shared.Question", "Shared.Complaint"};

        for (String folderPath : folders) {
            File folder = new File(folderPath);
            if (folder.exists() && folder.isDirectory()) {
                File[] files = folder.listFiles();
                if (files != null) {
                    for (File file : files) {
                        Inquiry inq = loadInquiryFromFile(file);
                        if (inq != null) {
                            allInquiries.add(inq);
                        }
                    }
                }
            }
        }
        return allInquiries;
    }

    private Inquiry loadInquiryFromFile(File file) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Inquiry) ois.readObject();
        } catch (Exception e) {
            System.err.println("Error reading file " + file.getName() + ": " + e.getMessage());
            return null;
        }
    }


}
