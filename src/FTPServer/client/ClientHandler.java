package FTPServer.client;

import FTPServer.file.CustomFile;
import FTPServer.person.AdminPerson;
import FTPServer.person.Person;

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
                else if (message.startsWith("REMOVE_FILE")) {
                    handleRemoveFile(message);
                }
                else if (message.startsWith("SHARE_FILE")) {
                    handleShareFile(message);
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

            if (email.equals(AdminPerson.getAdminEmail())) throw new IllegalAccessException("This email address is already taken!");

            Person person = new Person(email, password, firstname, lastname);
            System.out.println("SERVER: User created successfully!");
            person.saveUserToFile();

            // Create user FTP_FILES directory
            File userDirectory = new File("ftp_files/" + email);
            userDirectory.mkdirs();
            System.out.println("SERVER: User FTP_FILES directory has been created!");

            output.println("SUCCESS");
            output.flush();
            objectOutputStream.writeObject(person);
            objectOutputStream.flush();
        } catch (Exception e) {
            System.out.println("SERVER: Could not create the user!");
            try {
                output.println("Registration failed: " + e.getMessage());
                output.flush();
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
            AdminPerson currentAdmin = null;

            if (AdminPerson.isAdmin(email, password)) {
                currentAdmin = new AdminPerson(email, password, "Admin", "User");
            } else {
                for (Person person : people) {
                    if (person.getEmail().equals(email) && person.getPassword().equals(password)) {
                        currentPerson = person;
                        break;
                    }
                }
            }

            if (currentPerson != null) {
                System.out.println("SERVER: User has been authenticated");
                String feedbackMessage = String.format("SUCCESS;%s", currentPerson.getEmail());
                output.println(feedbackMessage);
                output.flush();
            } else if (currentAdmin != null) {
                System.out.println("SERVER: Admin has been authenticated");
                output.println("SUCCESS;admin@admin.com");
                output.flush();
            } else {
                output.println("ERROR: Invalid email or password");
                output.flush();
            }
        } catch (Exception exception) {
            System.out.println("Something went wrong! " + exception);
            output.println("ERROR: The user could not be authorized, please try again later.");
            output.flush();
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
                output.flush();
                return;
            }

            System.out.println("SERVER: Created userDirectory");

            CustomFile file = new CustomFile(userDirectory.getPath() + "/" + fileName);
            if (!file.createNewFile()) {
                System.out.println("SERVER: Could not create the file!");
                output.println("UPLOAD_FAILED: Failed to create file.");
                output.flush();
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
            output.flush();

            fos.close();
        } catch (IOException e) {
            System.out.println("SERVER: something went wrong!");
            output.println("UPLOAD_FAILED: " + e.getMessage());
            output.flush();
        }
    }

    /**
     * Handles the request to list files. If the request is from an admin, lists all files from all users.
     * If the request is from a regular user, lists only the files in their directory.
     *
     * @param message the message containing the request details, including the email of the requester
     */
    private void handleListFiles(String message) {
        try {
            String[] parts = message.split(";");
            String email = parts[1];

            if (AdminPerson.isAdmin(email, "admin123")) {
                File directory = new File("ftp_files/");
                File[] userDirectories = directory.listFiles();
                if (userDirectories != null) {
                    for (File userDirectory : userDirectories) {
                        if (userDirectory.isDirectory()) {
                            File[] files = userDirectory.listFiles();
                            if (files != null) {
                                for (File file : files) {
                                    output.println(userDirectory.getName() + ": " + file.getName());
                                    output.flush();
                                }
                            }
                        }
                    }
                }
            } else {
                File userDirectory = new File("ftp_files/" + email);
                if (!userDirectory.exists()) {
                    output.println("User directory does not exist.");
                    output.flush();
                    return;
                }

                File[] files = userDirectory.listFiles();
                if (files != null) {
                    for (File file : files) {
                        output.println(file.getName());
                        output.flush();
                    }
                }
            }
            output.println("END_OF_LIST");
            output.flush();
        } catch (Exception e) {
            output.println("Failed to list files: " + e.getMessage());
            output.flush();
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

    private void handleRemoveFile(String message) {
        try {
            String[] parts = message.split(";");
            String email = parts[1];
            String fileName = parts[2];

            File file = new File("ftp_files/" + email + "/" + fileName);
            if (file.exists() && file.delete()) {
                output.println("REMOVE_SUCCESS");
            } else {
                output.println("REMOVE_FAILED");
            }
        } catch (Exception e) {
            output.println("REMOVE_FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleShareFile(String message) {
        try {
            String[] parts = message.split(";");
            String senderEmail = parts[1];
            String recipientEmail = parts[2];
            String fileName = parts[3];

            CustomFile senderDirectory = new CustomFile("ftp_files/" + senderEmail);
            CustomFile recipientDirectory = new CustomFile("ftp_files/" + recipientEmail);

            if (!senderDirectory.exists() || !recipientDirectory.exists()) {
                output.println("SHARE_FAILED: One or both user directories do not exist.");
                return;
            }

            CustomFile fileToShare = new CustomFile(senderDirectory.getPath() + "/" + fileName);
            CustomFile sharedFile = new CustomFile(recipientDirectory.getPath() + "/" + fileName);

            try (FileInputStream fis = new FileInputStream(fileToShare.getPath());
                 FileOutputStream fos = new FileOutputStream(sharedFile.getPath())) {

                byte[] buffer = new byte[4096];
                int read;
                while ((read = fis.read(buffer)) != -1) {
                    fos.write(buffer, 0, read);
                }
            }

            output.println("SHARE_SUCCESS");
        } catch (IOException e) {
            output.println("SHARE_FAILED: " + e.getMessage());
        }
    }
}
