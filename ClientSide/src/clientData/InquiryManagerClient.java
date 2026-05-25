package clientData;

import Shared.*;

import java.io.*;
import java.net.Socket;
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
        objectOutputStream.flush();
        ObjectInputStream in = new ObjectInputStream(connectToServer.getInputStream());
        ResponseObj responseObj;

        Scanner scanner = new Scanner(System.in);
        System.out.println("press:\n1 - get all inquiries\n2 - create new inquiry\n3 - get inquiry`s status\n4 - get monthly count\n5 - cancel inquiry\n6 - Agent Login\n7 - Agent Logout\n8 - Add New Agent\n9 - Remove Agent\n10 - exit");
        x = scanner.nextInt();
        while(x!=10) {
            ro=execut(x);
            objectOutputStream.writeObject(ro);
            objectOutputStream.flush();

            printServerAnswer((ResponseObj)in.readObject()) ;
            System.out.println("press:\n1 - get all inquiries\n2 - create new inquiry\n3 - get inquiry`s status\n4 - get monthly count\n5 - cancel inquiry\n6 - Agent Login\n7 - Agent Logout\n8 - Add New Agent\n9 - Remove Agent\n10 - exit");
            x = scanner.nextInt();
        }
        connectToServer.close();

    }

    public RequestObj execut(int x)  {
        RequestObj ro=null;
        Scanner scanner = new Scanner(System.in);
        int y;
        switch (x) {
            case 1 -> ro=new RequestObj(RequestObj.Action.GET_ALL, null);
            case 2 -> ro=new RequestObj(RequestObj.Action.ADD_INQUIRY,createNewInquiry());
            case 3 -> {
                System.out.println("enter inquiry code");
                y=  scanner.nextInt();
                ro=new RequestObj(RequestObj.Action.GET_INQUIRY_STATUS,y);
            }
            case 4 -> {
                Scanner sc = new Scanner(System.in);
                System.out.println("Enter month (1-12):");
                int m = sc.nextInt();
                System.out.println("Enter year (e.g. 2024):");
                y = sc.nextInt();

                //Inquiry info = new Inquiry();
                ro = new RequestObj(RequestObj.Action.GET_COUNT_BY_MONTH, m);
            }
            case 5-> {
                System.out.println("Enter Inquiry Code to cancel:");
                int code = scanner.nextInt();
                ro = new RequestObj(RequestObj.Action.CANCEL_INQUIRY, code);

            }
            case 6 -> {
                System.out.println("Enter Agent Name:");
                String loginName = scanner.nextLine();
                System.out.println("Enter Agent ID:");
                int loginId = scanner.nextInt();

                Representative agentToLogin = new Representative(loginName, loginId);
                ro = new RequestObj(RequestObj.Action.AGENT_LOGIN, (Inquiry)(Object)agentToLogin);
            }
            case 7 -> {
                System.out.println("Enter Agent ID to Logout:");
                int logoutId = scanner.nextInt();

                ro = new RequestObj(RequestObj.Action.AGENT_LOGOUT, (Inquiry)(Object)logoutId);
            }
            case 8 -> {
                System.out.println("Enter New Agent Name to register:");
                String newAgentName = scanner.nextLine();
                System.out.println("Enter New Agent ID:");
                int newAgentId = scanner.nextInt();

                Representative agentToAdd = new Representative(newAgentName, newAgentId);
                ro = new RequestObj(RequestObj.Action.ADD_AGENT, (Inquiry)(Object)agentToAdd);
            }
            case 9 -> {
                System.out.println("Enter Agent ID to remove from system:");
                int removeId = scanner.nextInt();

                ro = new RequestObj(RequestObj.Action.REMOVE_AGENT, (Inquiry)(Object)removeId);
            }
        }
        return ro;
    }

    public Inquiry createNewInquiry(){
        Scanner scanner = new Scanner(System.in);
        Inquiry inquiry=null;
        System.out.println("press:\n1 - create complaint inquiry\n2 - create question inquiry\n3 - create request inquiry");
        int x = scanner.nextInt();
        scanner.nextLine();
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
    public void printServerAnswer(ResponseObj respo){
        if(respo.getStatus()==200)
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
}
