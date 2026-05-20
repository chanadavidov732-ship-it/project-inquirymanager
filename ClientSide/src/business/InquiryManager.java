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
import java.util.concurrent.ConcurrentLinkedQueue;

import HandleStoreFiles.HandleFiles;
import HandleStoreFiles.HandleFilesReflection;

public class InquiryManager {

    static java.util.Queue<Inquiry> QInquiry = new java.util.concurrent.ConcurrentLinkedQueue<>();

    static LinkedList<Representative> QRepresentative = new LinkedList<>();

    static {
        try {
            before();
        } catch (FileNotFoundException e) {
            System.err.println("שגיאה בטעינת פניות: " + e.getMessage());
        }

//        try {
//            List<Representative> loadedReps = loadRepresentativesReflection();
//            if (loadedReps != null) {
//                QRepresentative.addAll(loadedReps);
//            }
//        } catch (Exception e) {
//            System.err.println("שגיאה בטעינת נציגים באמצעות רפלקשן: " + e.getMessage());
//        }

        try {
            beforeNextVal();
        } catch (FileNotFoundException e) {
            System.err.println("קובץ nextVal לא נמצא: " + e.getMessage());
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
                    //hfr.deleteCsv("Representative/" + fileName);
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

    @Deprecated
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
        in.setStatus(Inquiry.Status.OPEN);
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
        while (!QInquiry.isEmpty()) {
            Thread t = new Thread(() -> {
                QInquiry.poll().handling();
            });
            t.start();
            try {
                Thread.sleep(3 * 1000);
                t.join();
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

    public List<Inquiry> getAllInquiriesForClient() throws FileNotFoundException {
        HandleFiles handleFiles = new HandleFiles();
        return handleFiles.readAllInquiries();
    }

    public static Representative findRepresentativeById(int id) {
        for (Representative rep : QRepresentative) {
            if (rep.getId() == id) {
                return rep;
            }
        }
        return null;
    }


    public static ResponseObj deleteRepresentative(int id) {
        Representative toDelete = null;

        for (Representative rep : QRepresentative) {
            if (rep.getId() == id) {
                toDelete = rep;
                break;
            }
        }

        if (toDelete != null) {
            QRepresentative.remove(toDelete);

            File file = new File("Representative/" + toDelete.getCode() + ".txt");
            if (file.exists()) {
                boolean deleted = file.delete();
                if (deleted) {
                    return new ResponseObj(200, "Representative deleted successfully from server", true);
                } else {
                    return new ResponseObj(500, "Failed to delete representative file from disk", false);
                }
            }
            return new ResponseObj(200, "Representative removed from memory, but file was not found", true);
        }

        return new ResponseObj(404, "Representative not found", false);
    }


}
