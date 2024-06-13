package FTPServer.server;

import FTPServer.client.ClientHandler;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 3030;
    private static ServerSocket serverSocket;

    private static void init() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server is listening on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println(socket + "socket");
                System.out.println("New client connected");

                ClientHandler clientHandler = new ClientHandler(socket);
                new Thread(clientHandler).start();
            }
        } catch (Exception exception) {
            System.out.println("Something went wrong while initializing the server!" + exception);
        }
    }

    public static void main(String[] args) {
        Server.init();
    }
}
