import clientData.InquiryManagerClient;

import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try{
<<<<<<< HEAD
            InquiryManagerClient i=new InquiryManagerClient("localhost", 8888);
=======
            InquiryManagerClient i=new InquiryManagerClient("Localhost", 8888);
>>>>>>> 9fe057149fd554ec3c4acb4d84bcffd193a8c6f3
            i.startConnectClientServer();
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}