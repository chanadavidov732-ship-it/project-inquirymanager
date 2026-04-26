package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class InquiryManagerServer {
    private static final int PORT = 12345;

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                new Thread(new HandleClient(socket)).start();
            }
        } catch (IOException e) {
            System.err.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new InquiryManagerServer().start();
    }
}