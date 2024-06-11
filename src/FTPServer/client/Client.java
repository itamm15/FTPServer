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

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getInput() {
        return input;
    }

    public PrintWriter getOutput() {
        return output;
    }

    public ObjectInputStream getObjectInputStream() {
        return objectInputStream;
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
                System.out.println("CLIENT: Initializing the FileBrowserPanel");
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

    public void uploadFile(File file, Person currentUser) {
        System.out.println("CLIENT: Uploading file to the server");
        try {
            output.println("UPLOAD;" + currentUser.getEmail() + ";" + file.getName());

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
}
