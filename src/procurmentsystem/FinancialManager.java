
package procurmentsystem;


import procurmentsystem.Table.Roles;

public class FinancialManager extends User {
  
    public FinancialManager(String ID, String firstName, String lastName, String password, String email) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.role = Roles.FinancialManager;
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
