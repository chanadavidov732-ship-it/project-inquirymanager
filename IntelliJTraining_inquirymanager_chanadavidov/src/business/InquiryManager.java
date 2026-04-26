package business;

import Shared.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
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
            throw new RuntimeException(e);
        }

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
        String[] folders = {"Data.Request", "Data.Question", "Data.Complaint"};
        HandleFiles handleFiles = new HandleFiles();
        for (String fn : folders) {
            File folder = new File(fn);
            if (folder.exists()) {
                File[] files = folder.listFiles();
                if (files != null) {
                    for (File file : files) {
                        String fileName = file.getName().replace(".txt", "");
                        Inquiry temp = new Inquiry() {
                            @Override
                            public String getFolderName() {
                                return fn;
                            }

                            @Override
                            public String getFileName() {
                                return fileName;
                            }
                        };
                        handleFiles.readFile(temp);
                    }
                }
            }
        }
    }

    public void inquiryCreation() throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("press:\n1 for question\n2 for request \n3 for complaint");
        int x = scanner.nextInt();
        QInquiry.add(switch (x) {
            case 1 -> new Question("dds");
            case 2 -> new Request("jhjj");
            case 3 -> new Complaint("1comp", "some");
            default -> throw new IllegalStateException("Unexpected value: " + x);
        });
        HandleFiles im = new HandleFiles();
        im.saveFile(QInquiry.peek());

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

    public static Queue<Inquiry> getQInquiry() {
        return QInquiry;
    }
}
