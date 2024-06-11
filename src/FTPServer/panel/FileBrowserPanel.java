package FTPServer.panel;

import FTPServer.client.Client;
import FTPServer.frame.FileViewerFrame;
import FTPServer.frame.Frame;
import FTPServer.person.Person;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class FileBrowserPanel extends JPanel {
    private JLabel emailLabel;
    private JLabel nameLabel;
    private JList<String> fileList;

    // TODO: Change to ArrayList
    private DefaultListModel<String> listModel;
    private Frame frame;
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

        fileList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = fileList.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        String selectedFile = fileList.getModel().getElementAt(index);
                        openFileViewer(selectedFile);
                    }
                }
            }
        });

        add(userInfoPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton uploadButton = new JButton("Upload File");
        uploadButton.addActionListener(event -> uploadFileByUser());
        buttonPanel.add(uploadButton);

        JButton removeButton = new JButton("Remove File");
        removeButton.addActionListener(event -> removeSelectedFile());
        buttonPanel.add(removeButton);

        add(buttonPanel, BorderLayout.SOUTH);

        loadFiles();
    }

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
        JFileChooser fileChooser = new JFileChooser();
        int chosenOption = fileChooser.showOpenDialog(frame);
        if (chosenOption == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile != null) {
                client.uploadFile(selectedFile, frame.getCurrentUser());
                loadFiles();
            }
        }
    }

    private void openFileViewer(String fileName) {
        String fileContent = client.downloadFile(fileName, frame.getCurrentUser());
        new FileViewerFrame(fileName, fileContent).setVisible(true);
    }

    private void removeSelectedFile() {
        String selectedFile = fileList.getSelectedValue();
        if (selectedFile != null) {
            client.removeFile(selectedFile, frame.getCurrentUser());
            loadFiles();
        }
    }
}
