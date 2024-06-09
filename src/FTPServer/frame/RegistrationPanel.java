/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FTPServer.frame;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author mateuszosinski
 */
public class RegistrationPanel extends JPanel {
    private JTextField firstnameField;
    private JTextField lastnameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton registerButton;

    public RegistrationPanel() {
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Marginesy wokół panelu

        JLabel firstnameLabel = new JLabel("First name:");
        firstnameField = new JTextField(15);
        JLabel lastnameLabel = new JLabel("Last name:");
        lastnameField = new JTextField(15);
        JLabel emailLabel = new JLabel("Email address:");
        emailField = new JTextField(15);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);
        registerButton = new JButton("Register");

        firstnameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        firstnameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        lastnameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        lastnameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        formPanel.add(firstnameLabel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(firstnameField);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(lastnameLabel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(lastnameField);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(emailLabel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(emailField);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(registerButton);

        add(formPanel, BorderLayout.CENTER);
    }
}
