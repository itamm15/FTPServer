package FTPServer.panel;

import FTPServer.client.Client;
import FTPServer.frame.Frame;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class AdminPanel extends JPanel {
    private JLabel emailLabel;
    private JList<String> fileList;

    private DefaultListModel<String> listModel;
    private Frame frame;
    private Client client;

    public AdminPanel(Frame frame, Client client) {
        this.frame = frame;
        this.client = client;
    }

    public void init() {
        System.out.println("AdminPanel initialization");
        setLayout(new BorderLayout());

        emailLabel = new JLabel("Admin Panel");

        listModel = new DefaultListModel<>();
        fileList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(fileList);

        add(emailLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadFiles();
    }

    private void loadFiles() {
        System.out.println("CLIENT: Loading all user files");
        try {
            PrintWriter output = client.getOutput();
            BufferedReader input = client.getInput();

            output.println("LIST_FILES;admin@admin.com");
            listModel.clear();
            String fileName;
            while ((fileName = input.readLine()) != null && !fileName.equals("END_OF_LIST")) {
                listModel.addElement(fileName);
            }

            System.out.println("CLIENT: Added all user files!");
        } catch (IOException e) {
            System.out.println("CLIENT: Could not load the files!" + e);
        }
    }
}
