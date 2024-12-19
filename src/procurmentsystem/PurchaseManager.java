package procurmentsystem;

import procurmentsystem.Table.Roles;
import procurmentsystem.Table.Table;
import procurmentsystem.Table.ValueNotFound;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.function.Function;

public class PurchaseManager extends User {

    public PurchaseManager(String ID, String firstName, String lastName, String password, String email) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.role = Roles.PurchaseManager;
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    protected boolean add() {
        return false;
    }

    @Override
    protected boolean delete() {
        return false;
    }

    @Override
    protected boolean update(String columnName, String oldValue, String newValue) {
        return false;
    }
}
