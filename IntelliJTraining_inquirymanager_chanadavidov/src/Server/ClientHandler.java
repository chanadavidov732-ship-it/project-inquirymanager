package Server;

import Shared.*;
import HandleStoreFiles.HandleFiles;
import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private HandleFiles fileHandler;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        this.fileHandler = new HandleFiles();
    }

    @Override
    public void run() {
        try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {

            RequestObj request = (RequestObj) input.readObject();
            ResponseObj response;

            switch (request.getAction()) {
                case TEST:
                    response = new ResponseObj(200, "Success", "SERVER_READY");
                    break;

                case ADD_INQUIRY:
                    Inquiry newInquiry = (Inquiry) request.getParams();

                    int nextId = fileHandler.getNextIdAndIncrement();

                    newInquiry.setCode(nextId);

                    fileHandler.saveFile(newInquiry);

                    response = new ResponseObj(200, "Inquiry added successfully", nextId);
                    break;

                default:
                    response = new ResponseObj(400, "Unknown Action", null);
            }

            output.writeObject(response);
            output.flush();

        } catch (Exception e) {
            System.err.println("Error handling client: " + e.getMessage());
        }
    }
}