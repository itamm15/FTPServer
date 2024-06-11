package FTPServer.client;

import FTPServer.person.Person;

import javax.security.auth.login.AccountNotFoundException;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

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
                }
                else if (message.startsWith("AUTHORIZE")) {
                    handleAuthorization(message);
                }
                else {
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

            // Create user FTP_FILES directory
            File userDirectory = new File("ftp_files/" + email);
            userDirectory.mkdirs();
            System.out.println("SERVER: User FTP_FILES directory has been created!");

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

    private void handleAuthorization(String message) {
        try {
            Person.loadUsersFromFile();
            ArrayList<Person> people = Person.getPeople();

            String[] parts = message.split(";");
            String email = parts[1];
            String password = parts[2];

            Person currentPerson = null;
            for (Person person: people) {
                if (person.getEmail().equals(email) && person.getPassword().equals(password)) {
                    currentPerson = person;
                    break;
                }
            }

            if (currentPerson != null) {
                System.out.println("SERVER: User has been authenticated");

                output.println("SUCCESS");
                objectOutputStream.writeObject(currentPerson);
                objectOutputStream.flush();
            } else {
                throw new AccountNotFoundException("Could not authenticate the user!");
            }
        } catch (AccountNotFoundException e) {
            System.out.println("SERVER: The user account could not be found!");
            output.println("Could not find user account");
        } catch (IOException e) {
            System.out.println("SERVER: Something went wrong while sending the user");
            output.println("Oupsi, something went wrong!");
        }
    }
}
