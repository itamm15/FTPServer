/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FTPServer.frame;

import FTPServer.person.Person;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author mateuszosinski
 */

public class Frame extends JFrame {
    private final int WIDTH = 600;
    private final int HEIGHT = 400;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    private static Person currentUser;

    public Frame() {
        super("FTPServer");
    }
    
    
    public void init() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        RegistrationPanel registrationPanel = new RegistrationPanel(this);
        LoginPanel loginPanel = new LoginPanel(this);

        cardPanel.add(registrationPanel, "RegistrationPanel");
        cardPanel.add(loginPanel, "LoginPanel");

        getContentPane().add(cardPanel);
        
        // Actions to be kept on the bottom
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void showLoginPanel() {
        cardLayout.show(cardPanel, "LoginPanel");
    }

    public void showRegistrationPage() {
        cardLayout.show(cardPanel, "RegistrationPanel");
    }

    public static Person getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(Person currentUser) {
        Frame.currentUser = currentUser;
    }
}
