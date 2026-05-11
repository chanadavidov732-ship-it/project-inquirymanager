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

        try (
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            while (true) {
                RequestObj request = (RequestObj) in.readObject();

                if (request == null) break;

                ResponseObj response = service.handleRequest(request);

                out.writeObject(response);
                out.flush();
            }

        }

        catch (Exception e) {
            System.out.println("Client disconnected or error: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}