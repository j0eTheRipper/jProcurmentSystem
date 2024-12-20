

package procurmentsystem;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.function.Function;
import procurmentsystem.Table.InteractionsWithTable;
import procurmentsystem.Table.Roles;
import procurmentsystem.Table.Table;
import procurmentsystem.Table.ValueNotFound;

public abstract class User extends InteractionsWithTable {
    private static final String FILE_PATH = "C:\\Users\\elect\\OneDrive\\Documents\\NetBeansProjects\\AdminMY\\src\\adminmy\\users.csv";  // Adjust path if needed
    protected Roles role;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected String email;

    public static User get(String column, Function<String, Boolean> filter) {
        try {
            Table table = new Table("src/files/users.csv");
            List<String> userData =  table.getRow(column, filter);
            switch (userData.get(4)) {
                case "admin":
                    break;
                case "finance manager":
                    return new FinancialManager(
                            userData.get(0),
                            userData.get(1),
                            userData.get(2),
                            userData.get(3),
                            userData.get(4)
                    );
                case "sales manager":
                    break;
                case "purchase manager":
                    return new PurchaseManager(
                            userData.get(0),
                            userData.get(1),
                            userData.get(2),
                            userData.get(3),
                            userData.get(4)
                    );
                case "inventory manager":
                    break;
            }
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            return null;
        } catch (ValueNotFound e) {
            return null;
        }
        return null;
    }

    // Login method to validate username and password
    public static User login(String email, String password) throws FileNotFoundException {
        Table table = new Table(FILE_PATH);

        try {
            List<String> row = table.getRow("email", (x) -> x.equals(email));
            switch (row.get(4)) {
                case "Financial Manager":
                    return new FinancialManager(row.get(0), row.get(1), row.get(2), row.get(3), row.get(5));

                case "Sales Manager ":
//                    return new SalesManager(row.get(0), row.get(1), row.get(2), row.get(3), row.get(5));
                    break;

                case "Purchase Manager":
                    return new PurchaseManager(row.get(0), row.get(1), row.get(2), row.get(3), row.get(5));

                case "Inventory Manager":
//                    return new InventoryManager(row.get(0), row.get(1), row.get(2), row.get(3), row.get(5));
                    break;

                case "Admin":
//                    return new Admin(row.get(0), row.get(1), row.get(2), row.get(3), row.get(5));
                    break;
            }
        } catch (ValueNotFound ex) {
            return null;
        }
        return null;
    }

    public String getID() {
        return ID;
    }
}
