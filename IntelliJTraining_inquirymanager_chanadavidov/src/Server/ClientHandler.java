package Server;

import Shared.*;
import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private ServerService service;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        this.service = new ServerService();
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

            RequestObj request = (RequestObj) in.readObject();

            ResponseObj response = service.handleRequest(request);

            out.writeObject(response);
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}