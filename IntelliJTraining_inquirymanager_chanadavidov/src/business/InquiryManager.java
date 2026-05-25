package business;

import Shared.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;

import HandleStoreFiles.HandleFiles;
import HandleStoreFiles.HandleFilesReflection;

public class InquiryManager {

    final static Queue<Inquiry> QInquiry = new LinkedList<>();
    final static Queue<Representative> QActiveRepresentative = new LinkedList<>();
    final static Queue<Representative> QRepresentative = new LinkedList<>();
    final static Queue<InquiryHandlingTask> QInquiryMergeAgent =new ConcurrentLinkedQueue<>();

    static {
        try {
            before();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("שגיאה בטעינת פניות: " + e.getMessage(), e);
        }

        try {
            QRepresentative.addAll(loadRepresentativesReflection());        }
        catch (Exception e) {
            throw new RuntimeException("שגיאה בטעינת נציגים באמצעות רפלקשן: " + e.getMessage(), e);
        }

        try {
            beforeNextVal();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("קובץ nextVal לא נמצא: " + e.getMessage(), e);
        }


        mergingInquiriesToAgent();
    }

    private static void mergingInquiriesToAgent() {
        Thread thread=new Thread(()->{
            while(true){
                if (!QActiveRepresentative.isEmpty() && !QInquiry.isEmpty())
                {
                    InquiryHandlingTask iht=new InquiryHandlingTask(QActiveRepresentative.poll(),QInquiry.poll());
                    //option 1-
                    //QInquiryMergeAgent.add(iht);
                    agentsTreatInquiries(iht);
                }
            }
        } );
        thread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static void agentsTreatInquiries(InquiryHandlingTask iht) {
        //option 1-

//        Thread thread=new Thread(()->{
//            while(true) {
//                if (!QInquiryMergeAgent.isEmpty())
//                    QInquiryMergeAgent.poll().getMergeInquiry().handling();
//        }});
//
//        thread.start();
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        //option 2-

        Thread thread = new Thread(iht);

        thread.start();
    }

    public Representative findRepresentativeById(int id) {
        for (Representative rep : QRepresentative) {
            if (rep.getId() == id) {
                return rep;
            }
        }

        File file = new File("Representative/" + id + ".txt");
        if (file.exists() && file.isFile()) {
            try {
                HandleFilesReflection hfr = new HandleFilesReflection();
                Object obj = hfr.readCsv(file.getPath());

                if (obj instanceof Representative) {
                    Representative rep = (Representative) obj;
                    QRepresentative.add(rep);
                    return rep;
                }
            } catch (Exception e) {
                System.err.println("שגיאה בקריאת נציג באמצעות רפלקשן: " + e.getMessage());
            }
        }
        return null;
    }


    public boolean deleteRepresentative(int id) {
        Representative toRemove = null;
        for (Representative rep : QRepresentative) {
            if (rep.getId() == id) {
                toRemove = rep;
                break;
            }
        }
        if (toRemove != null) {
            QRepresentative.remove(toRemove);
        }

        File file = new File("Representative/" + id + ".txt");
        if (file.exists()) {
            return file.delete();
        }

        return toRemove != null;
    }
    public void defineRepresentative(Representative rep) throws IOException, IllegalAccessException {
//        Scanner scanner = new Scanner(System.in);
//        int x = 1, id;
//        String v;

//        while (x == 1) {
//            System.out.println("enter a name");
//            v = scanner.next();
//            System.out.println("enter id");
//            id = scanner.nextInt();

           // Representative rep = new Representative(v, id);
            QRepresentative.add(rep);
            HandleFilesReflection hfr = new HandleFilesReflection();
            hfr.saveCSVObject(rep, "Representative/" + String.valueOf(rep.getId()));
            //QRepresentative.add(new Representative(v, id));
//           HandleFilesReflection hfr = new HandleFilesReflection();
//           hfr.saveCSVObject(QRepresentative.peek(), "Representative/"
//                    + String.valueOf(QRepresentative.peek().getCode()));

//            System.out.println("to add representative press 1 to exit enter any key");
//            x = scanner.nextInt();
//        }
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

    public static void saveNextValFile() throws IOException, IllegalAccessException {
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

    public long getTotalInquiryCountByMonth(int month, int year) {
        HandleFiles handleFiles = new HandleFiles();
        try {
            List<Inquiry> currentInquiries = handleFiles.readAllInquiries();
            List<Inquiry> historyInquiries = handleFiles.readHistoryInquiries();

            return Stream.concat(currentInquiries.stream(), historyInquiries.stream())
                    .filter(inq -> isValidInquiry(inq, month, year))
                    .count();
        } catch (FileNotFoundException e) {
            System.err.println("Error: One of the files was not found.");
            return 0;
        }
    }

    public int getInquiryCountByMonth(int month) {
        int count = 0;

        for (Inquiry inquiry : QInquiry) {
            if (inquiry.getCreationDate() != null && inquiry.getCreationDate().getMonthValue() == month) {
                count++;
            }
        }
        try {
            HandleFiles handleFiles = new HandleFiles();
            List<Inquiry> historyInquiries = handleFiles.readHistoryInquiries();
            if (historyInquiries != null) {
                for (Inquiry inquiry : historyInquiries) {
                    if (inquiry.getCreationDate() != null && inquiry.getCreationDate().getMonthValue() == month) {
                        count++;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error scanning history files for monthly count: " + e.getMessage());
        }

        return count;
    }

    private boolean isValidInquiry(Inquiry inq, int month, int year) {
        return inq != null &&
                inq.getCreationDate() != null &&
                inq.getCreationDate().getMonthValue() == month &&
                inq.getCreationDate().getYear() == year;
    }
    public ResponseObj cancelInquiry(int code) throws IOException {
        Inquiry toCancel = null;

        // 1. חיפוש הפנייה בתור לפי הקוד שהתקבל
        for (Inquiry inq : QInquiry) {
            if (inq.getCode() == code) {
                toCancel = inq;
                break;
            }
        }

        // 2. אם הפנייה נמצאה
        if (toCancel != null) {
            // עדכון הסטטוס ל-CANCELED
            toCancel.setStatus(Inquiry.Status.CANCELED);

            HandleFiles hf = new HandleFiles();

            // 3. שמירת הפנייה (הפעם עם הסטטוס המעודכן)
            // אם ב-saveFile הוספת לוגיקה שבודקת את הסטטוס ושומרת ב-History
            hf.saveFile(toCancel);

            // 4. מחיקת הקובץ מהתיקייה המקורית (Shared.Request וכדומה)
            hf.deleteInquiryFile(toCancel);

            // 5. הסרת הפנייה מהתור שבזיכרון
            QInquiry.remove(toCancel);

            return new ResponseObj(200, "Inquiry canceled successfully", true);
        }

        // אם לא נמצאה פנייה עם קוד כזה
        return new ResponseObj(404, "Inquiry not found", false);
    }

    public static List<Representative> loadRepresentativesReflection() {
        List<Representative> representatives = new ArrayList<>();

        try {
            String className = "Shared.Representative";
            Class<?> clazz = Class.forName(className);

            File repoFolder = new File("Representative");
            if (!repoFolder.exists() || !repoFolder.isDirectory()) {
                return representatives;
            }

            File[] files = repoFolder.listFiles();
            if (files == null) return representatives;

            HandleFilesReflection hfr = new HandleFilesReflection();

            for (File file : files) {
                if (file.isFile()) {
                    Object obj = hfr.readCsv(file.getPath());
                    if (clazz.isInstance(obj)) {
                        representatives.add((Representative) obj);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading representatives via Reflection: " + e.getMessage());
        }

        return representatives;
    }

    public void registerAgent(Representative loginAgent){
        QActiveRepresentative.add(loginAgent);
    }
    public void disconnectAgent(int logoutAgentId){
        Iterator<Representative> it = QActiveRepresentative.iterator();

        while (it.hasNext()) {
            Representative r = it.next();
            if (r.getId() == logoutAgentId) {
                it.remove();
                break;
            }
        }
    }
}

