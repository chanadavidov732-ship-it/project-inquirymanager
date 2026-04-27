import Server.InquiryManagerServer;
import business.InquiryManager;

import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException, IllegalAccessException {
//        InquiryHandling handling1 = new InquiryHandling();
//        Thread handling2 = new InquiryHandling();
//        InquiryHandling handling3 = new InquiryHandling();
//        InquiryHandling handling4 = new InquiryHandling();
//        handling1.createInquiry();
//        ((InquiryHandling) handling2).createInquiry();
//        handling3.createInquiry();
//        handling4.createInquiry();

        InquiryManager inquiryManager = new InquiryManager();
//        inquiryManager.defineRepresentative();

         inquiryManager.inquiryCreation();
         inquiryManager.inquiryCreation();
       //  inquiryManager.processInquiryManager();

        InquiryManagerServer server = new InquiryManagerServer();
        server.startServer();
        // first running
//        handling1.start();
//        handling2.start();
//        handling3.start();
//        handling4.start();
        // second running
//		handling1.run();
//		handling2.run();
//		handling3.run();
//		handling4.run();

    }
}