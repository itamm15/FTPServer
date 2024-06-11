package FTPServer.frame;

import FTPServer.client.Client;
import FTPServer.person.Person;

import javax.swing.*;
import java.awt.*;

public class FileBrowserPanel extends JPanel {
    private JLabel emailLabel;
    private JLabel nameLabel;
    private JList<String> fileList;
    private DefaultListModel<String> listModel;
    private Frame frame;
    private Client client;

    public FileBrowserPanel(Frame frame, Client client) {
        this.frame = frame;
        this.client = client;
    }

    public void init() {
        Person currentUser = frame.getCurrentUser();
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

        //      loadFiles();
    }
    
//    private void loadFiles() {
//        File directory = new File("files.txt");
//        File[] files = directory.listFiles();
//
//        if (files != null) {
//            for (File file : files) {
//                listModel.addElement(file.getName());
//            }
//        }
//    }
}
