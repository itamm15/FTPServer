package FTPServer.frame;

import javax.swing.*;
import java.awt.*;

public class FileViewerFrame extends JFrame {
    public FileViewerFrame(String fileName, String fileContent) {
        setTitle("Viewing: " + fileName);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea textArea = new JTextArea(fileContent);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }
}
