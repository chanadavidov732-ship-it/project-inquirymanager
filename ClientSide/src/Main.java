import clientData.InquiryManagerClient;

import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try{
            InquiryManagerClient i=new InquiryManagerClient("localhost", 8888);
            i.startConnectClientServer();
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}