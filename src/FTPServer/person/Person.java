/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FTPServer.person;

import java.util.regex.Pattern;

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

    public Person(String email, String password, String firstname, String lastname) throws Exception {
        // Validate
        boolean isEmailValid = EmailValidator.isValid(email);
        boolean isPasswordValid = PasswordValidator.isValid(password);
        boolean isFirstNameValid = NameValidator.isValid(firstname);
        boolean isLastNameValid = NameValidator.isValid(lastname);

        if (isEmailValid && isPasswordValid && isFirstNameValid && isLastNameValid) {
            this.email = email;
            this.password = password;
            this.firstname = firstname;
            this.lastname = lastname;
        } else {
            // TODO: add more specific way of returning an errors
            throw new Exception("Oupsi, something went wrong! Try again!");
        }
    }

    @Override
    public String toString() {
        return "Person{" +
                "email='" + email + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }

    private static class EmailValidator {
        private static final String EMAIL_PATTERN =
                "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

        public static boolean isValid(String email) {
            if (email == null) {
                return false;
            }
            return pattern.matcher(email).matches();
        }
    }

    private static class PasswordValidator {
        private static final int MIN_PASSWORD_LENGTH = 5;
        public static boolean isValid(String password) {
            return password.length() >= MIN_PASSWORD_LENGTH;
        }
    }

    private static class NameValidator {
        public static boolean isValid(String name) {
            return !name.isBlank();
        }
    }
}
