package FTPServer.panel;

import FTPServer.client.Client;
import FTPServer.frame.Frame;
import FTPServer.person.Person;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class FileBrowserPanel extends JPanel {
    private JLabel emailLabel;
    private JLabel nameLabel;
    private JList<String> fileList;
    // TODO: change to ArrayList<>
    private DefaultListModel<String> listModel;
    private FTPServer.frame.Frame frame;
    private Client client;

    public FileBrowserPanel(Frame frame, Client client) {
        this.frame = frame;
        this.client = client;
    }

    public void init() {
        System.out.println("FileBrowserPanel initialization");
        Person currentUser = this.frame.getCurrentUser();
        setLayout(new BorderLayout());

        JPanel userInfoPanel = new JPanel(new BorderLayout());
        emailLabel = new JLabel(currentUser.getEmail());
        nameLabel = new JLabel(currentUser.getFirstname() + " " + currentUser.getLastname());

        userInfoPanel.add(emailLabel, BorderLayout.WEST);
        userInfoPanel.add(nameLabel, BorderLayout.EAST);

        listModel = new DefaultListModel<>();
        fileList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(fileList);

        add(userInfoPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        JButton uploadButton = new JButton("Upload File");
        uploadButton.addActionListener(event -> uploadFileByUser());
        add(uploadButton, BorderLayout.SOUTH);

        loadFiles();
    }

    // TODO: move to client
    private void loadFiles() {
        System.out.println("CLIENT: Loading user files");
        try {
            PrintWriter output = this.client.getOutput();
            BufferedReader input = this.client.getInput();
            Person currentUser = this.frame.getCurrentUser();

            output.println("LIST_FILES;" + currentUser.getEmail());
            listModel.clear();
            String fileName;
            while ((fileName = input.readLine()) != null && !fileName.equals("END_OF_LIST")) {
                listModel.addElement(fileName);
            }

            System.out.println("CLIENT: Added user files!");
        } catch (IOException e) {
            System.out.println("CLIENT: Could not load the files!" + e);
        }
    }

    private void uploadFileByUser() {
        this.client.uploadFile(this.frame.getCurrentUser());
        loadFiles();
    }
}
