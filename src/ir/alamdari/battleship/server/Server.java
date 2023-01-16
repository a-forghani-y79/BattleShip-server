package ir.alamdari.battleship.server;

import ir.alamdari.battleship.model.comminucations.Request;
import ir.alamdari.battleship.model.comminucations.Response;
import ir.alamdari.battleship.model.comminucations.Type;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    static ServerSocket serverSocket;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        int port = 9876;
        serverSocket = new ServerSocket(port);
        ObjectOutputStream objectOutputStream;
        ObjectInputStream objectInputStream;

        Socket socket;
        while (true) {
            System.out.println("waiting for th client request");

            socket = serverSocket.accept();
            socket.setKeepAlive(true);

            objectInputStream =
                    new ObjectInputStream(socket.getInputStream());

            Request request = (Request) objectInputStream.readObject();

            System.out.println(request.toString());

            Response response = getResponseForRequest(request);


            objectOutputStream =
                    new ObjectOutputStream(socket.getOutputStream());

            objectOutputStream.writeObject(response);


            objectOutputStream.close();
            objectInputStream.close();
        }

//        System.out.println("ir.alamdari.battleship.server shutdown");
//        serverSocket.close();


    }

    private static Response getResponseForRequest(Request request) {

        Response response
                = new Response();
        response.setTo(request.getFrom());
        response.setResponseType(Type.CONNECTION_CHECKING);
        switch (request.getRequestType().getCode()) {
            case 1:
                response.setData(true);
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                response.setData(null);
        }

        return response;

    }
}
