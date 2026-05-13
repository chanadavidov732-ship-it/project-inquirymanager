package business;

import Shared.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import HandleStoreFiles.HandleFiles;
import HandleStoreFiles.HandleFilesReflection;

public class InquiryManager {

    final static Queue<Inquiry> QInquiry;
    final static LinkedList<Representative> QRepresentative;

    static {
        QInquiry = new LinkedList<>();
        try {
            before();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);}

        QRepresentative = new LinkedList<>();
        try {
            beforeRepresentative();

        } catch (ClassNotFoundException |
                 InvocationTargetException |
                 NoSuchMethodException |
                 InstantiationException |
                 IllegalAccessException |
                 IOException e) {
            throw new RuntimeException(e);
        }
        try {
            beforeNextVal();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void defineRepresentative() throws IOException, IllegalAccessException {
        Scanner scanner = new Scanner(System.in);
        int x = 1, id;
        String v;

        while (x == 1) {
            System.out.println("enter a name");
            v = scanner.next();
            System.out.println("enter id");
            id = scanner.nextInt();
            QRepresentative.add(new Representative(v, id));

            HandleFilesReflection hfr = new HandleFilesReflection();
            hfr.saveCSVObject(QRepresentative.peekLast(), "Representative/"
                    + String.valueOf(QRepresentative.peekLast().getCode()));

            System.out.println("to add representative press 1 to exit enter any key");
            x = scanner.nextInt();
        }
    }

    public static void beforeNextVal() throws FileNotFoundException {
        File dataFile = new File("nextVal.txt");
        if (!dataFile.exists()|| dataFile.length() == 0) {
            return;
        }
        Scanner scanner = new Scanner(dataFile);
        if (scanner.hasNext()) {
           Inquiry.nextCodeVal = Integer.parseInt(scanner.next());
        }
        scanner.close();
    }

    public static void beforeRepresentative() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        HandleFilesReflection hfr = new HandleFilesReflection();
        File folder = new File("Representative");
        if (folder.exists()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName().replace(".txt", "");
                    System.out.println(fileName);
                    QRepresentative.add((Representative) hfr.readCsv("Representative/" + fileName));
                    hfr.deleteCsv("Representative/" + fileName);
                }
            }
        }
    }

    public static void before() throws FileNotFoundException {
        String[] folders = {"Shared.Request", "Shared.Question", "Shared.Complaint"};
        HandleFiles handleFiles = new HandleFiles();
        for (String fn : folders) {
            File folder = new File(fn);
            if (folder.exists()) {
                File[] files = folder.listFiles();
                if (files != null) {
                    for (File file : files) {
                        String fileName = file.getName().replace(".txt", "");

                        Inquiry temp;
                        switch (fn) {
                            case "Shared.Question":
                                temp = new Question();
                                break;
                            case "Shared.Request":
                                temp = new Request();
                                break;
                            case "Shared.Complaint":
                                temp = new Complaint();
                                break;
                            default:
                                throw new IllegalStateException("Unexpected value: " + fn);
                        }
                        temp.setCode(Integer.parseInt(fileName));
                        handleFiles.readFile(temp);
                        QInquiry.add(temp);
                    }
                }
            }
        }
    }

    public void inquiryCreation() throws IOException, IllegalAccessException {
        HandleFiles im = new HandleFiles();
        Scanner scanner = new Scanner(System.in);
        System.out.println("press:\n1 for question\n2 for request \n3 for complaint");
        int x = scanner.nextInt();
        Inquiry in= (switch (x) {
            case 1 -> new Question("dds");
            case 2 -> new Request("jhjj");
            case 3 -> new Complaint("1comp", "some");
            default -> throw new IllegalStateException("Unexpected value: " + x);
        });
        QInquiry.add(in);
        im.saveFile(in);
        saveNextValFile();
    }

    public void saveNextValFile() throws IOException, IllegalAccessException {
        File dataFile = new File( "nextVal.txt");
        if (dataFile.getParentFile() != null) {
            dataFile.getParentFile().mkdirs();
        }
        FileWriter writer = new FileWriter(dataFile, false);

        writer.write( String.valueOf(Inquiry.nextCodeVal));
        writer.flush();
        writer.close();
        System.out.println("finish saveFile: nextVal");
    }

    public void processInquiryManager() {
        while (this.QInquiry.peek() != null) {
            this.QInquiry.poll().handling();
            try {
                Thread.currentThread().sleep(3 * 1000);
            } catch (InterruptedException e) {
                System.out.println(e.toString());
            }
        }
    }


    public static Queue<Inquiry> getQInquiry(){
        return QInquiry;
    }

    public void addInquiryFromClient(Inquiry inquiry) throws IOException {
        HandleFiles handleFiles = new HandleFiles();

        int nextId = Inquiry.getNextCodeVal();
//        inquiry.setCode(nextId);

        handleFiles.saveFile(inquiry);

        QInquiry.add(inquiry);
    }

    public List<Inquiry> getAllInquiriesForClient() {
        HandleFiles handleFiles = new HandleFiles();
        return handleFiles.readAllInquiries();
    }

}
