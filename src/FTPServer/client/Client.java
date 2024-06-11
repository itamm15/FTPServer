package FTPServer.client;

import FTPServer.frame.Frame;
import FTPServer.person.Person;

import java.io.*;
import java.net.Socket;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 3030;
    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;
    private ObjectInputStream objectInputStream;

    public void init() {
        try {
            socket = new Socket(HOST, PORT);
            System.out.println("The client has been connected...");
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (Exception exception) {
            System.out.println("Something went wrong!" + exception);
        }
    }


    public void registerUser(String email, String password, String firstname, String lastname, Frame frame) {
        try {
            System.out.println("CLIENT: Start user creation...");
            String message = String.format("REGISTER;%s;%s;%s;%s", email, password, firstname, lastname);
            output.println(message);

            String feedback = input.readLine();
            if (feedback.contains("SUCCESS")) {
                System.out.println("CLIENT: User created successfully");
                Person person = (Person) objectInputStream.readObject();
                System.out.println("CLIENT: Settling the current user");
                frame.setCurrentUser(person);
                frame.showFileBrowserPanel();
            } else {
                System.out.println("CLIENT: The user could not be created, see errors: " + feedback);
            }
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    public void authorizeUser(String email, String password, Frame frame) {
        try {
            System.out.println("CLIENT: Start user authorization");
            String message = String.format("AUTHORIZE;%s;%s", email, password);
            output.println(message);

            String feedback = input.readLine();
            if (feedback.contains("SUCCESS")) {
                System.out.println("CLIENT: User authorized successfully");
                Person person = (Person) objectInputStream.readObject();
                System.out.println("CLIENT: Settling the current user");
                frame.setCurrentUser(person);
                frame.showFileBrowserPanel();
            } else {
                System.out.println("CLIENT: The user could not be created, see errors: " + feedback);
            }
        } catch (IOException e) {
            System.out.println("CLIENT: Could not get the user!");
        } catch (ClassNotFoundException e) {
            System.out.println("CLIENT: Could not get the user!");
        }
    }
}
