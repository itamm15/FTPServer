/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FTPServer.frame;

import java.awt.GridLayout;
import javax.swing.*;

/**
 *
 * @author mateuszosinski
 */
public class RegistrationPanel extends JFrame {
    private JTextField firstameField;
    private JTextField lastnameField;
    private JTextField emailField;
    private JTextField passwordField;
    private JButton registerButton;
    
    public RegistrationPanel() {
        setLayout(new GridLayout(5, 2));
        
        JLabel firstnameLabel = new JLabel("First name:");
        firstameField = new JTextField();
        JLabel lastnameLabel = new JLabel("Last name:");
        lastnameField = new JTextField();
        JLabel emailLabel = new JLabel("email address:");
        emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JTextField();
        registerButton = new JButton("Register");

        add(firstnameLabel);
        add(firstameField);
        add(lastnameLabel);
        add(lastnameField);
        add(emailLabel);
        add(emailField);
        add(passwordLabel);
        add(passwordField);
        add(registerButton);
    }
}
