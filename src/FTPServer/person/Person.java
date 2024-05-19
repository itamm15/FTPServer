/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FTPServer.person;

/**
 *
 * @author mateuszosinski
 */
public class Person {
    private String email;
    // TODO: turn it `hashedPassword`
    private String password;
    private String firstname;
    private String lastname;
    
    public Person(String email, String password, String firstname, String lastname) {
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }
}
