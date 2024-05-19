/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FTPServer.frame;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 *
 * @author mateuszosinski
 */

public class Frame extends JFrame {
    private final int WIDTH = 500;
    private final int HEIGHT = 300;
    
    public Frame() {
        super("FTPServer");
    }
    
    
    public void init() { 
     // Actions to be kept on the bottom
     setSize(WIDTH, HEIGHT);
     setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
     setVisible(true);
    }
}
