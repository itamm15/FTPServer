package FTPServer.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 2020;

    private static void init() {
        try {
            Socket socket = new Socket(HOST, PORT);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

                String userInput;
                while ((userInput = consoleInput.readLine()) != null) {
                    output.println(userInput);
                    System.out.println("Server response: " + input.readLine());
                }
        } catch (Exception exception) {
                System.out.println("Something went wrong!" + exception);
        }
    }

    public static void main(String[] args) {
        Client.init();
    }
}
