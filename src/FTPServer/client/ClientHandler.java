package FTPServer.client;

import FTPServer.file.CustomFile;
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
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            String message;
            while ((message = input.readLine()) != null) {
                System.out.println("Received from client: " + message);

                if (message.startsWith("REGISTER")) {
                    handleRegistration(message);
                }
                else if (message.startsWith("AUTHORIZE")) {
                    handleAuthorization(message);
                }
                else if (message.startsWith("UPLOAD")) {
                    handleFileUpload(message);
                }
                else if (message.startsWith("LIST_FILES")) {
                    handleListFiles(message);
                }
                else if(message.startsWith("DOWNLOAD")) {
                    handleDownloadFile(message);
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

    private void handleFileUpload(String message) {
        System.out.println("SERVER: Processing the user files");
        try {
            String[] parts = message.split(";");
            String email = parts[1];
            String fileName = parts[2];

            CustomFile userDirectory = new CustomFile("ftp_files/" + email);
            if (!userDirectory.exists()) {
                output.println("UPLOAD_FAILED: User directory does not exist.");
                return;
            }

            System.out.println("SERVER: Created userDirectory");

            CustomFile file = new CustomFile(userDirectory.getPath() + "/" + fileName);
            if (!file.createNewFile()) {
                System.out.println("SERVER: Could not create the file!");
                output.println("UPLOAD_FAILED: Failed to create file.");
                return;
            }

            System.out.println("SERVER: Created user file");
            System.out.println("SERVER: Start processing the file");

            FileOutputStream fos = new FileOutputStream(file.getPath());
            InputStream is = socket.getInputStream();
            byte[] buffer = new byte[4096];

            int read;
            while ((read = is.read(buffer)) != -1) {
                fos.write(buffer, 0, read);

                if(read < buffer.length) break;
            }

            System.out.println("SERVER: File uploaded successfully!");
            output.println("UPLOAD_SUCCESS");

            fos.close();
        } catch (IOException e) {
            System.out.println("SERVER: something went wrong!");
            output.println("UPLOAD_FAILED: " + e.getMessage());
        }
    }

    private void handleListFiles(String message) {
        try {
            String[] parts = message.split(";");
            String email = parts[1];

            File userDirectory = new File("ftp_files/" + email);
            if (!userDirectory.exists()) {
                output.println("User directory does not exist.");
                return;
            }

            File[] files = userDirectory.listFiles();
            if (files != null) {
                for (File file : files) {
                    output.println(file.getName());
                }
            }
            output.println("END_OF_LIST");
        } catch (Exception e) {
            output.println("Failed to list files: " + e.getMessage());
        }
    }

    private void handleDownloadFile(String message) {
        System.out.println("SERVER: Start downloading the file!");
        try {
            String[] parts = message.split(";");
            String email = parts[1];
            String fileName = parts[2];

            File file = new File("ftp_files/" + email + "/" + fileName);
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    output.println(line);
                }
                output.println("END_OF_FILE");
                reader.close();
            } else {
                output.println("FILE_NOT_FOUND");
            }
        } catch (IOException e) {
            output.println("ERROR_READING_FILE");
        }
    }
}
