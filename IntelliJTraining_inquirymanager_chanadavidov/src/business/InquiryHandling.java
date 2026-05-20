package business;

import Shared.*;
import java.util.Scanner;

public class InquiryHandling extends Thread {
    private Inquiry currentInquiry;

    public InquiryHandling(Inquiry currentInquiry) {
        this.currentInquiry = currentInquiry;
    }

    @Deprecated//יש להשתמש ב InquiryManager.inquiryCreation()
    public void createInquiry(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("press:\n1 for question\n2 for request \n3 for complaint");
        int x = scanner.nextInt();

        currentInquiry= switch (x) {
            case 1 -> new Question("dds");
            case 2 -> new Request("jhjj");
            case 3 -> new Complaint("1comp","some");
            default -> throw new IllegalStateException("Unexpected value: " + x);
        };
    }

    @Override
    public void run() {
        if(currentInquiry instanceof Question)
            Thread.currentThread().setPriority(10);
        currentInquiry.handling();
        try {
            if(currentInquiry instanceof Request )
                sleep(3*1000);
            else
                sleep(5*1000);
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }
    public Inquiry getCurrentInquiry() {
        return currentInquiry;
    }
    public void setCurrentInquiry(Inquiry currentInquiry) {
        this.currentInquiry = currentInquiry;
    }
}