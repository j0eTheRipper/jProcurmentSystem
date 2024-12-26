package procurmentsystem.Users;

import procurmentsystem.Table.Table;

import java.io.FileNotFoundException;

public class Admin extends User {

    public Admin(String id, String email, String password, String firstName, String lastName) {
        this.ID = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = Role.Admin;
        try {
            this.table = new Table("src/files/users.csv");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Admin(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = Role.Admin;
        try {
            this.table = new Table("src/files/users.csv");
            this.ID = this.generateID();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}

