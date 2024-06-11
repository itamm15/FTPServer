package FTPServer.frame;

import FTPServer.client.Client;

import javax.swing.*;
import java.awt.*;

public class RegistrationPanel extends JPanel {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField firstnameField;
    private JTextField lastnameField;
    private JButton registerButton;
    private JButton goToLoginButton;
    private Client client;

    private Frame frame;

    public RegistrationPanel(Frame frame, Client client) {
        this.frame = frame;
        this.client = client;
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel emailLabel = new JLabel("Email address:");
        emailField = new JTextField(15);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);
        JLabel firstnameLabel = new JLabel("First name:");
        firstnameField = new JTextField(15);
        JLabel lastnameLabel = new JLabel("Last name:");
        lastnameField = new JTextField(15);
        registerButton = new JButton("Register");
        goToLoginButton = new JButton("Go to login");

        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        firstnameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        firstnameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        lastnameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        lastnameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        goToLoginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        formPanel.add(emailLabel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(emailField);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(firstnameLabel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(firstnameField);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(lastnameLabel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(lastnameField);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(registerButton);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(goToLoginButton);

        add(formPanel, BorderLayout.CENTER);

        goToLoginButton.addActionListener(event -> {
            frame.showLoginPanel();
        });

        registerButton.addActionListener(event -> {
            registerUser();
        });
    }

    private void registerUser() {
        String email = emailField.getText();
        String firstname = firstnameField.getText();
        String lastname = lastnameField.getText();
        String password = String.valueOf(passwordField.getPassword());

        client.registerUser(email, password, firstname, lastname, frame);
    }
}
