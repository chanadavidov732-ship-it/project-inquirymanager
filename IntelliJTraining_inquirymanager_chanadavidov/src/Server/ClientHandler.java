package Server;

import Shared.RequestObj;
import Shared.ResponseObj;
import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {

            RequestObj request = (RequestObj) input.readObject();
            System.out.println("Received request: " + request.getAction());

            ResponseObj response;
            if (request.getAction() == RequestObj.Action.TEST) {
                response = new ResponseObj(200, "Success", "SERVER_READY");
            } else {
                response = new ResponseObj(400, "Action not implemented yet", null);
            }

            output.writeObject(response);
            output.flush();

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Handler exception: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}