package FTPServer.person;

import java.io.Serializable;

public class AdminPerson extends Person implements Serializable {
    private static final String ADMIN_EMAIL = "admin@admin.com";
    private static final String ADMIN_PASSWORD = "admin123";

    public AdminPerson(String email, String password, String firstname, String lastname) throws Exception {
        super(email, password, firstname, lastname);
    }

    public static boolean isAdmin(String email, String password) {
        return ADMIN_EMAIL.equals(email) && ADMIN_PASSWORD.equals(password);
    }

    public static String getAdminEmail() {
        return ADMIN_EMAIL;
    }
}
