package clientData;

import Data.Complaint;
import Data.Inquiry;
import Data.Question;
import Data.Request;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class InquiryManagerClient {
    Socket connectToServer;

    public InquiryManagerClient() {
        connectToServer=new Socket();
    }

    public InquiryManagerClient(String host,int port ) throws IOException {
        connectToServer=new Socket(host,port);
        System.out.println("client success connect to server");
    }

    public void startConnectClientServer() throws IOException, ClassNotFoundException {
        RequestObj ro;
        int x;
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(connectToServer.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(connectToServer.getInputStream());
        ResponseObj responseObj;

        Scanner scanner = new Scanner(System.in);
        System.out.println("press:\n1 - get all inquiries\n2 - create new inquiry\n3 - exit");
        x = scanner.nextInt();
        while(x!=3) {
            ro=execut(x);
            objectOutputStream.writeObject(ro);
            objectOutputStream.flush();

            printServerAnswer((ResponseObj)in.readObject()) ;
            System.out.println("press:\n1 - get all inquiries\n2 - create new inquiry\n3 - exit");
            x = scanner.nextInt();
        }
        connectToServer.close();
    }

    public void printServerAnswer(ResponseObj respo){
        if(respo.getStatus()==1)
            System.out.println("your request over successfully");
        else
            System.out.println("your request doesnt over");
        System.out.println("the server massage is :\n"+respo.getMessage());

        //in case the client ask all inquiries the respo is list
        if (respo.getResult() instanceof List<?> list) {
            for (Inquiry i :(List<Inquiry>) respo.getResult()) {
                System.out.println(i.toString());
            }
        }

        //in case the client create inquiry the respo is string
        else
            System.out.println(respo.getResult());

    }

    public RequestObj execut(int x)  {
        RequestObj ro=null;
        switch (x) {
            case 1 -> ro=new RequestObj(RequestObj.Action.GET_ALL,new Inquiry());
            case 2 -> ro=new RequestObj(RequestObj.Action.ADD_INQUIRY,createNewInquiry());
        }
        return ro;
    }

    public Inquiry createNewInquiry(){
        Scanner scanner = new Scanner(System.in);
        Inquiry inquiry=null;

        System.out.println("press:\n1 - create complaint inquiry\n2 - create question inquiry\n3 - create request inquiry");
        int x = scanner.nextInt();

        System.out.println("enter description");
        String d = scanner.nextLine();
        String a;

        switch (x) {
            case 1 -> {
                System.out.println("enter assignedBranch");
                a = scanner.nextLine();
                inquiry=new Complaint(a,d);
            }
            case 2 -> inquiry=new Question(d);
            case 3 -> inquiry=new Request(d);
        }
        return inquiry;
    }
}
