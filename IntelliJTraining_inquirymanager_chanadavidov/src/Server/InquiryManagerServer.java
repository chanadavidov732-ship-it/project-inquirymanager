package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class InquiryManagerServer {
    private ServerSocket myServer;
    private static final int PORT = 8888;
    private boolean isRunning = false;

    public InquiryManagerServer() {
        try {
            myServer = new ServerSocket(PORT);
        } catch (IOException e) {
            System.err.println("Could not listen on port " + PORT);
        }
    }

    public void startServer() {
        isRunning = true;
        System.out.println("Inquiry Manager Server started on port " + PORT);

        while (isRunning) {
            try {
                Socket clientSocket = myServer.accept();
                ClientHandler handler = new ClientHandler(clientSocket);
                new Thread(handler).start();
            } catch (IOException e) {
                if (isRunning) {
                    System.err.println("Accept failed: " + e.getMessage());
                }
            }
        }
    }

    public void stop() {
        isRunning = false;
        try {
            if (myServer != null) {
                myServer.close();
            }
            System.out.println("Server stopped.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        InquiryManagerServer server = new InquiryManagerServer();
        server.startServer();
    }
}