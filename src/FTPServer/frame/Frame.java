/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FTPServer.frame;

import FTPServer.client.Client;
import FTPServer.panel.AdminPanel;
import FTPServer.panel.FileBrowserPanel;
import FTPServer.panel.LoginPanel;
import FTPServer.panel.RegistrationPanel;
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
    private Client client;
    private Person currentUser;

    // PANELS
    private RegistrationPanel registrationPanel;
    private LoginPanel loginPanel;
    private FileBrowserPanel fileBrowserPanel;
    private AdminPanel adminPanel;

    public Frame(Client client) {
        super("FTPServer");

        this.client = client;
        System.out.println("Frame created");
    }

    public void init() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        registrationPanel = new RegistrationPanel(this, this.client);
        loginPanel = new LoginPanel(this, this.client);
        fileBrowserPanel = new FileBrowserPanel(this, this.client);
        adminPanel = new AdminPanel(this, this.client);

        cardPanel.add(registrationPanel, "RegistrationPanel");
        cardPanel.add(loginPanel, "LoginPanel");
        cardPanel.add(fileBrowserPanel, "FileBrowserPanel");
        cardPanel.add(adminPanel, "AdminPanel");

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

    public void showFileBrowserPanel() {
        fileBrowserPanel.init();
        cardLayout.show(cardPanel, "FileBrowserPanel");
    }

    public void showAdminPanel() {
        adminPanel.init();
        cardLayout.show(cardPanel, "AdminPanel");
    }

    public Person getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Person currentUser) {
        this.currentUser = currentUser;
    }
}
