package Server;

import Shared.*;
import HandleStoreFiles.HandleFiles;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class HandleClient extends Thread {
    private Socket clientSocket;

    public HandleClient(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        handleClientRequest();
    }

    private void handleClientRequest() {
        try (ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream())) {

            RequestObj request = (RequestObj) input.readObject();
            ResponseObj response;
            HandleFiles fileHandler = new HandleFiles();

            try {
                switch (request.getAction()) {
                    case ADD_INQUIRY:
                        Inquiry newInquiry = (Inquiry) request.getParams();
                        int nextId = fileHandler.getNextIdAndIncrement();
                        newInquiry.setCode(nextId);

                        fileHandler.saveFile(newInquiry);
                        response = new ResponseObj(200, "SUCCESS", nextId);
                        break;

                    case GET_ALL:
                        List<Inquiry> allInquiries = fileHandler.readAllInquiries();
                        response = new ResponseObj(200, "SUCCESS", allInquiries);
                        break;

                    case TEST:
                        response = new ResponseObj(200, "SUCCESS", "SERVER_READY");
                        break;

                    default:
                        response = new ResponseObj(400, "FAILED", "Unknown action");
                }
            } catch (Exception e) {
                response = new ResponseObj(500, "FAILED", "Error: " + e.getMessage());
            }

            output.writeObject(response);
            output.flush();

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Communication error: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}