package FTPServer.client;

import FTPServer.person.Person;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    private ObjectOutputStream objectOutputStream;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream()); // Initialize once

            String message;
            while ((message = input.readLine()) != null) {
                System.out.println("Received from client: " + message);

                if (message.startsWith("REGISTER")) {
                    handleRegistration(message);
                } else {
                    output.println("Server received: " + message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleRegistration(String message) {
        try {
            Person.loadUsersFromFile();

            String[] parts = message.split(";");
            String email = parts[1];
            String password = parts[2];
            String firstname = parts[3];
            String lastname = parts[4];

            Person person = new Person(email, password, firstname, lastname);
            System.out.println("SERVER: User created successfully!");
            person.saveUserToFile();

            output.println("SUCCESS");
            objectOutputStream.writeObject(person);
            objectOutputStream.flush();
        } catch (Exception e) {
            System.out.println("SERVER: Could not create the user!");
            try {
                output.println("Registration failed: " + e.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
