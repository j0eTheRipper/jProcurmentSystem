package procurmentsystem;

public class PurchaseManager extends User {

    public PurchaseManager(String ID, String firstName, String lastName, String password, String email) {

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
