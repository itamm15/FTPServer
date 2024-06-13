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

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getInput() {
        return input;
    }

    public PrintWriter getOutput() {
        return output;
    }

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
                    System.out.println("File uploaded successfully.");
                } else {
                    System.out.println("File upload failed: " + serverResponse);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public void removeFile(String fileName, Person currentUser) {
        try {
            output.println("REMOVE_FILE;" + currentUser.getEmail() + ";" + fileName);
            output.flush();

            String serverResponse = input.readLine();
            if (serverResponse.equals("REMOVE_SUCCESS")) {
                System.out.println("File removed successfully.");
            } else {
                System.out.println("File removal failed: " + serverResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shareFile(String fileName, String recipientEmail, Person currentUser) {
        System.out.println("CLIENT: Sharing file with another user");
        try {
            output.println("SHARE_FILE;" + currentUser.getEmail() + ";" + recipientEmail + ";" + fileName);
            output.flush();

            String serverResponse = input.readLine();
            if (serverResponse.equals("SHARE_SUCCESS")) {
                System.out.println("File shared successfully.");
            } else {
                System.out.println("File sharing failed: " + serverResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
