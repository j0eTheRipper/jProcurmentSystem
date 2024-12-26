package procurmentsystem.Users;

import procurmentsystem.Table.IncorrectNumberOfValues;
import procurmentsystem.Table.InteractionsWithTable;
import procurmentsystem.Table.Table;
import procurmentsystem.Table.ValueNotFound;

import java.io.*;
import java.util.*;
import java.util.function.Function;

public abstract class User extends InteractionsWithTable {
    protected String firstName;
    protected String lastName;
    protected String password;
    protected String email;
    protected Role role;

    public static List<User> getMultiple(String column, Function<String, Boolean> filter) throws FileNotFoundException, ValueNotFound {
        Table table = new Table("src/files/users.csv");
        List<List<String>> rows = table.getRows(column, filter);
        List<User> users = new ArrayList<>();
        for (List<String> userRecord : rows) {
            String role = userRecord.get(4);
            switch (role) {
                case "FinancialManager":
                    FinancialManager fm = new FinancialManager(
                            userRecord.get(0),
                            userRecord.get(5),
                            userRecord.get(3),
                            userRecord.get(1),
                            userRecord.get(2)
                    );
                    users.add(fm);
                    break;
                case "SalesManager":
                    SalesManager sm = new SalesManager(
                            userRecord.get(0),
                            userRecord.get(5),
                            userRecord.get(3),
                            userRecord.get(1),
                            userRecord.get(2)
                    );
                    users.add(sm);
                    break;
                case "InventoryManager":
                    InventoryManager im = new InventoryManager(
                            userRecord.get(0),
                            userRecord.get(5),
                            userRecord.get(3),
                            userRecord.get(1),
                            userRecord.get(2)
                    );
                    users.add(im);
                    break;
                case "PurchaseManager":
                    PurchaseManager PM = new PurchaseManager(
                            userRecord.get(0),
                            userRecord.get(5),
                            userRecord.get(3),
                            userRecord.get(1),
                            userRecord.get(2)
                    );
                    users.add(PM);
                    break;
                case "Admin":
                    Admin ad = new Admin(
                            userRecord.get(0),
                            userRecord.get(5),
                            userRecord.get(3),
                            userRecord.get(1),
                            userRecord.get(2)
                    );
                    users.add(ad);
                    break;
            }
        }
        return users;
    }

    public static User get(String column, Function<String, Boolean> filter) {
        try {
            Table table = new Table("src/files/users.csv");
            List<String> userRecord = table.getRow(column, filter);
            switch (userRecord.get(4)) {
                case "Admin":
                    return new Admin(
                            userRecord.get(0),
                            userRecord.get(5),
                            userRecord.get(3),
                            userRecord.get(1),
                            userRecord.get(2)
                    );
                case "FinancialManager":
                    return new FinancialManager(
                            userRecord.get(0),
                            userRecord.get(5),
                            userRecord.get(3),
                            userRecord.get(1),
                            userRecord.get(2)
                    );
                case "SalesManager":
                    return new SalesManager(
                            userRecord.get(0),
                            userRecord.get(5),
                            userRecord.get(3),
                            userRecord.get(1),
                            userRecord.get(2)
                    );
                case "PurchaseManager":
                    return new PurchaseManager(
                            userRecord.get(0),
                            userRecord.get(5),
                            userRecord.get(3),
                            userRecord.get(1),
                            userRecord.get(2)
                    );
                case "InventoryManager":
                    return new InventoryManager(
                            userRecord.get(0),
                            userRecord.get(5),
                            userRecord.get(3),
                            userRecord.get(1),
                            userRecord.get(2)
                    );
            }
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            return null;
        } catch (ValueNotFound e) {
            return null;
        }
        return null;
    }

    public static User login(String email, String password) {
        User user = User.get("email", x -> x.equals(email));
        if (user == null) return null;
        if (!user.getPassword().equals(password)) return null;
        return user;
    }

    public Role getRole() {
        return role;
    }

    public String getFirstName() {
        return firstName;
    }

    public boolean setFirstName(String newFirstName) {
        boolean updateStatus = newFirstName.matches("[A-z]+");
        if (updateStatus) {
            updateStatus = this.update("firstName", newFirstName);
            if (updateStatus) this.firstName = newFirstName;
        }
        return updateStatus;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean setLastName(String newLastName) {
        boolean updateStatus = newLastName.matches("[A-z]+");
        if (updateStatus) {
            updateStatus = this.update("lastName", newLastName);
            if (updateStatus) this.lastName = newLastName;
        }
        return updateStatus;
    }

    public String getPassword() {
        return this.password;
    }

    public boolean setPassword(String newPassword) {
        boolean updateStatus = this.update("password", newPassword);
        if (updateStatus) this.password = newPassword;
        return updateStatus;
    }

    public String getEmail() {
        return email;
    }

    public boolean setEmail(String newEmail) {
        boolean updateStatus = newEmail.matches(".+@.+\\..+");
        if (updateStatus) {
            updateStatus = update("email", newEmail);
            if (updateStatus) email = newEmail;
        }
        return updateStatus;
    }

    public void setRole(Role newRole) {
        update("role", String.valueOf(newRole));
        this.role = newRole;
    }

    public String getID() {
        return ID;
    }

    @Override
    public boolean delete() {
        try {
            int id = table.getRowIndex("id", x -> x.equals(ID));
            table.deleteRow(id);
            return true;
        } catch (ValueNotFound e) {
            System.out.println("ID Not Found.");
            return false;
        }
    }

    @Override
    public boolean add() {
        try {
            String[] profile = {ID, firstName, lastName, password, String.valueOf(role), email};
            table.addRow(profile);
        } catch (IncorrectNumberOfValues e) {
            System.out.println("something is wrong!");
        }

        return false;
    }

    @Override
    public String toString() {
        return ID + ". " + this.firstName + " " + this.lastName + ": " + this.role;
    }

}


