package FTPServer.client;

import FTPServer.frame.Frame;
import FTPServer.person.Person;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

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

            System.out.println("CLIENT: Streams initialized");
        } catch (Exception exception) {
            System.out.println("Something went wrong!" + exception);
        }
    }

    public void closeConnections() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            if (output != null) {
                output.close();
            }
            if (input != null) {
                input.close();
            }
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            System.out.println("Connections closed successfully");
        } catch (Exception exception) {
            System.out.println("Could not close the connections: " + exception.getMessage());
        }
    }

    public BufferedReader getInput() {
        return input;
    }

    public PrintWriter getOutput() {
        return output;
    }


    /**
     * Sends a request to the server to register a new user with the provided details.
     * If registration is successful, the current user is set and the file browser panel is shown.
     *
     * @param email the email address of the new user
     * @param password the password for the new user
     * @param firstname the first name of the new user
     * @param lastname the last name of the new user
     * @param frame the main application frame
     * @throws Exception if an error occurs during registration
     */
    public void registerUser(String email, String password, String firstname, String lastname, Frame frame) throws Exception {
        try {
            System.out.println("CLIENT: Start user creation...");
            String message = String.format("REGISTER;%s;%s;%s;%s", email, password, firstname, lastname);
            output.println(message);
            output.flush();

            String feedback = input.readLine();
            if (feedback.contains("SUCCESS")) {
                System.out.println("CLIENT: User created successfully");
                Person person = (Person) objectInputStream.readObject();
                System.out.println("CLIENT: Settling the current user");
                frame.setCurrentUser(person);
                frame.showFileBrowserPanel();
            } else {
                throw new Exception(feedback);
            }
        } catch (IOException | ClassNotFoundException exception) {
            throw new Exception("An error occurred during registration.");
        }
    }

    /**
     * Attempts to authorize a user with the given email and password.
     * If the user is successfully authorized, the appropriate panel is displayed
     * depending on whether the user is an admin or a regular user.
     *
     * @param email the email address of the user
     * @param password the password of the user
     * @param frame the main application frame
     * @throws Exception if there is an error during authorization or communication with the server
     */
    public void authorizeUser(String email, String password, Frame frame) throws Exception {
        closeConnections();
        init();
        try {
            System.out.println("CLIENT: Start user authorization");
            String message = String.format("AUTHORIZE;%s;%s", email, password);
            output.println(message);
            output.flush();

            String feedback = input.readLine();
            if (feedback.contains("SUCCESS")) {
                String[] data = feedback.split(";");
                String currentPersonEmail = data[1];
                System.out.println("CLIENT: User authorized successfully");

                if (currentPersonEmail.equals("admin@admin.com")) {
                    System.out.println("CLIENT: Admin logged in");

                    frame.setCurrentUser(new Person("admin@admin.com", "admin123", "admin", "user" ));
                    frame.showAdminPanel();
                } else {
                    Person.loadUsersFromFile();
                    ArrayList<Person> people = Person.getPeople();
                    Person currentPerson = null;

                    for (Person person : people) {
                        if (person.getEmail().equals(currentPersonEmail)) currentPerson = person;
                    }

                    System.out.println("CLIENT: Settling the current user");

                    frame.setCurrentUser(currentPerson);
                    System.out.println("CLIENT: Initializing the FileBrowserPanel");
                    frame.showFileBrowserPanel();
                }
            } else {
                if (feedback.contains("ERROR")) {
                    throw new Exception(feedback);
                }
                System.out.println("CLIENT: The user could not be authorized, see errors: " + feedback);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("CLIENT: Could not get the user!");
            throw new Exception("Error communicating with server");
        }
    }

    /**
     * Uploads a file to the server for the specified user.
     * The file is read in chunks using a buffer and sent to the server.
     *
     * @param file the file to be uploaded
     * @param currentUser the user who is uploading the file
     */
    public void uploadFile(File file, Person currentUser) {
        System.out.println("CLIENT: Uploading file to the server");
        try {
            output.println("UPLOAD;" + currentUser.getEmail() + ";" + file.getName());
            output.flush();

            byte[] buffer = new byte[4096];
            FileInputStream fis = new FileInputStream(file);
            OutputStream os = socket.getOutputStream();

            int read;
            while ((read = fis.read(buffer)) != -1) {
                os.write(buffer, 0, read);
            }

            os.flush();
            fis.close();

            String serverResponse;
            if ((serverResponse = input.readLine()) != null) {
                if (serverResponse.equals("UPLOAD_SUCCESS")) {
                    System.out.println("CLIENT: File uploaded successfully.");
                } else {
                    System.out.println("CLIENT: File upload failed: " + serverResponse);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Downloads a file from the server for the specified user.
     * The file content is read line by line and returned as a string.
     *
     * @param fileName the name of the file to be downloaded
     * @param currentUser the user who is downloading the file
     * @return the content of the file as a string, or an error message if the file could not be loaded
     */
    public String downloadFile(String fileName, Person currentUser) {
        try {
            output.println("DOWNLOAD;" + currentUser.getEmail() + ";" + fileName);
            output.flush();

            StringBuilder fileContent = new StringBuilder();
            String line;
            while ((line = input.readLine()) != null && !line.equals("END_OF_FILE")) {
                fileContent.append(line).append("\n");
            }

            return fileContent.toString();
        } catch (IOException e) {
            System.out.println("CLIENT: Oupsi, the file content could not be loaded!");
            return "Error loading file content.";
        }
    }


    /**
     * Sends a request to the server to remove a file for the specified user.
     *
     * @param fileName the name of the file to be removed
     * @param currentUser the user who is requesting the file removal
     */
    public void removeFile(String fileName, Person currentUser) {
        try {
            output.println("REMOVE_FILE;" + currentUser.getEmail() + ";" + fileName);
            output.flush();

            String serverResponse = input.readLine();
            if (serverResponse.equals("REMOVE_SUCCESS")) {
                System.out.println("CLIENT: File removed successfully.");
            } else {
                System.out.println("CLIENT: File removal failed: " + serverResponse);
            }
        } catch (IOException exception) {
            System.out.println("CLIENT: File removal failed: " + exception);
        }
    }

    /**
     * Sends a request to the server to share a file with another user.
     *
     * @param fileName the name of the file to be shared
     * @param recipientEmail the email of the recipient user
     * @param currentUser the user who is sharing the file
     */
    public void shareFile(String fileName, String recipientEmail, Person currentUser) {
        System.out.println("CLIENT: Sharing file with another user");
        try {
            output.println("SHARE_FILE;" + currentUser.getEmail() + ";" + recipientEmail + ";" + fileName);
            output.flush();

            String serverResponse = input.readLine();
            if (serverResponse.equals("SHARE_SUCCESS")) {
                System.out.println("CLIENT: File shared successfully.");
            } else {
                System.out.println("CLIENT: File sharing failed: " + serverResponse);
            }
        } catch (IOException exception) {
            System.out.println("CLIENT: File sharing failed: " + exception);
        }
    }
}
