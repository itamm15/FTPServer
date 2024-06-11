package FTPServer.panel;

import FTPServer.client.Client;
import FTPServer.frame.Frame;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton goToRegistrationButton;
    private FTPServer.frame.Frame frame;
    private Client client;

    public LoginPanel(Frame frame, Client client) {
        this.frame = frame;
        this.client = client;
        setLayout(new BorderLayout());

        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Marginesy wokół panelu

        JLabel emailLabel = new JLabel("Email address:");
        emailField = new JTextField(15);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Login");
        goToRegistrationButton = new JButton("Go to registration");

        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        goToRegistrationButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        formPanel.add(emailLabel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(emailField);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(loginButton);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(goToRegistrationButton);

        add(formPanel, BorderLayout.CENTER);

        // actions listeners
        goToRegistrationButton.addActionListener(event -> {
            frame.showRegistrationPage();
        });

        loginButton.addActionListener(event -> {
            loginUser();
        });
    }

    private void loginUser() {
        try {
            String email = emailField.getText();
            String password = String.valueOf(passwordField.getPassword());

            client.authorizeUser(email, password, frame);
        } catch (Exception exception) {
            System.out.println("Oupsi, something went wrong!" + exception);
        }
    }
}