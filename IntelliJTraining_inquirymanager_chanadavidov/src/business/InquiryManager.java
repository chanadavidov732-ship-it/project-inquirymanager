package business;

import Data.Complaint;
import Data.Inquiry;
import Data.Question;
import Data.Request;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class InquiryManager {

    Queue<Inquiry> QInquiry = new LinkedList<>();

    public void inquiryCreation(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("press:\n1 for question\n2 for request \n3 for complaint");
        int x = scanner.nextInt();
        QInquiry.add( switch (x) {
            case 1 -> new Question("dds");
            case 2 -> new Request("jhjj");
            case 3 -> new Complaint("1comp","some");
            default -> throw new IllegalStateException("Unexpected value: " + x);
        });
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

//    public InquiryManager() {
//        this.QInquiry = new Queue<Inquiry>() {
//        };
//    }

//    public Queue<Inquiry> getQInquiry() {
//        return QInquiry;
//    }
//
//    public void setQInquiry(Queue<Inquiry> QInquiry) {
//        this.QInquiry = QInquiry;
//    }
}
