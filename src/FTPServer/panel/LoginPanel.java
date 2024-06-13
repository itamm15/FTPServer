package FTPServer.panel;

import FTPServer.client.Client;
import FTPServer.frame.Frame;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private JLabel emailErrorLabel;
    private JLabel passwordErrorLabel;

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton goToRegistrationButton;
    private Client client;

    private Frame frame;

    public LoginPanel(Frame frame, Client client) {
        this.frame = frame;
        this.client = client;
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel emailLabel = new JLabel("Email address:");
        emailField = new JTextField(15);
        emailErrorLabel = new JLabel();
        emailErrorLabel.setForeground(Color.RED);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);
        passwordErrorLabel = new JLabel();
        passwordErrorLabel.setForeground(Color.RED);

        loginButton = new JButton("Login");
        goToRegistrationButton = new JButton("Go to registration");

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        emailErrorLabel.setHorizontalAlignment(SwingConstants.LEFT);
        formPanel.add(emailErrorLabel, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        passwordErrorLabel.setHorizontalAlignment(SwingConstants.LEFT);
        formPanel.add(passwordErrorLabel, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(loginButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(goToRegistrationButton, gbc);

        add(formPanel, BorderLayout.CENTER);

        goToRegistrationButton.addActionListener(event -> frame.showRegistrationPage());

        loginButton.addActionListener(event -> loginUser());
    }

    private void loginUser() {
        clearErrorLabels();
        String email = emailField.getText();
        String password = String.valueOf(passwordField.getPassword());

        try {
            client.authorizeUser(email, password, frame);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            System.out.println(errorMessage);
            if (errorMessage.contains("email")) {
                emailErrorLabel.setText(errorMessage);
            } else if (errorMessage.contains("password")) {
                passwordErrorLabel.setText(errorMessage);
            } else {
                emailErrorLabel.setText("Invalid email or password");
            }
        }
    }

    private void clearErrorLabels() {
        emailErrorLabel.setText("");
        passwordErrorLabel.setText("");
    }
}
