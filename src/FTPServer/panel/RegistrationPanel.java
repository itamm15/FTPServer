package FTPServer.panel;

import FTPServer.client.Client;
import FTPServer.frame.Frame;

import javax.swing.*;
import java.awt.*;

public class RegistrationPanel extends JPanel {
    private JLabel emailErrorLabel;
    private JLabel passwordErrorLabel;
    private JLabel firstnameErrorLabel;
    private JLabel lastnameErrorLabel;

    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField firstnameField;
    private JTextField lastnameField;
    private JButton registerButton;
    private JButton goToLoginButton;
    private Client client;

    private FTPServer.frame.Frame frame;

    public RegistrationPanel(Frame frame, Client client) {
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

        JLabel firstnameLabel = new JLabel("First name:");
        firstnameField = new JTextField(15);
        firstnameErrorLabel = new JLabel();
        firstnameErrorLabel.setForeground(Color.RED);

        JLabel lastnameLabel = new JLabel("Last name:");
        lastnameField = new JTextField(15);
        lastnameErrorLabel = new JLabel();
        lastnameErrorLabel.setForeground(Color.RED);

        registerButton = new JButton("Register");
        goToLoginButton = new JButton("Go to login");

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
        formPanel.add(firstnameLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(firstnameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        firstnameErrorLabel.setHorizontalAlignment(SwingConstants.LEFT);
        formPanel.add(firstnameErrorLabel, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(lastnameLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(lastnameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        lastnameErrorLabel.setHorizontalAlignment(SwingConstants.LEFT);
        formPanel.add(lastnameErrorLabel, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        formPanel.add(registerButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        formPanel.add(goToLoginButton, gbc);

        add(formPanel, BorderLayout.CENTER);

        goToLoginButton.addActionListener(event -> {
            frame.showLoginPanel();
        });

        registerButton.addActionListener(event -> {
            registerUser();
        });
    }

    private void registerUser() {
        clearErrorLabels();
        String email = emailField.getText();
        String firstname = firstnameField.getText();
        String lastname = lastnameField.getText();
        String password = String.valueOf(passwordField.getPassword());

        try {
            client.registerUser(email, password, firstname, lastname, frame);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (errorMessage.contains("email")) {
                emailErrorLabel.setText(errorMessage);
            } else if (errorMessage.contains("Password")) {
                passwordErrorLabel.setText(errorMessage);
            } else if (errorMessage.contains("First name")) {
                firstnameErrorLabel.setText(errorMessage);
            } else if (errorMessage.contains("Last name")) {
                lastnameErrorLabel.setText(errorMessage);
            } else {
                JOptionPane.showMessageDialog(this, "Registration Error - \"All of the fields need to be filled!\"");
            }
        }
    }

    private void clearErrorLabels() {
        emailErrorLabel.setText("");
        passwordErrorLabel.setText("");
        firstnameErrorLabel.setText("");
        lastnameErrorLabel.setText("");
    }
}
