package business;

import Data.Complaint;
import Data.Inquiry;
import Data.Question;
import Data.Request;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import HandleStoreFiles.HandleFiles;
public class InquiryManager {

    final static Queue<Inquiry> QInquiry ;

    static {
        QInquiry = new LinkedList<>();
        try {
            before();
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void before() throws FileNotFoundException {
        String[] folders = {"Data.Request", "Data.Question", "Data.Complaint"};
        HandleFiles handleFiles=new HandleFiles();
        for (String folderName : folders) {
            File folder = new File(folderName);
            if (folder.exists() ) {
                File[] files = folder.listFiles();
                if (files != null) {
                    for (File file : files) {
                        String fileName = file.getName().replace(".txt", "");
                        Inquiry temp = new Inquiry() {
                            @Override public String getFolderName() { return folderName; }
                            @Override public String getFileName() { return fileName; }
                        };
                        handleFiles.readFile(temp);
                    }
                }
            }
        }
        System.out.println("debug");
    }

    public void inquiryCreation() throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("press:\n1 for question\n2 for request \n3 for complaint");
        int x = scanner.nextInt();
        QInquiry.add( switch (x) {
            case 1 -> new Question("dds");
            case 2 -> new Request("jhjj");
            case 3 -> new Complaint("1comp","some");
            default -> throw new IllegalStateException("Unexpected value: " + x);
        });
        HandleFiles im= new HandleFiles();
        im.saveFile(QInquiry.peek());

    }

    public void processInquiryManager(){
        while(this.QInquiry.peek()!=null){
            this.QInquiry.poll().handling();
            try {
                Thread.currentThread().sleep(3*1000);
            } catch (InterruptedException e) {
                System.out.println(e.toString());
            }
        }
    }

    public static Queue<Inquiry> getQInquiry(){
        return QInquiry;
    }
}
