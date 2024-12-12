package procurmentsystem.Table;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.regex.Pattern;

public abstract class InteractionsWithTable {
    protected String ID;
    protected Table table;


    abstract public String toString();
    abstract protected boolean add();
    abstract protected boolean delete();
    abstract protected boolean update(String columnName, String oldValue, String newValue);
    protected String generateID() throws FileNotFoundException {
        List<String> lastRow = table.getLastRow();
        String id = lastRow.get(0);
        int newID = Integer.parseInt(id) + 1;
        return String.valueOf(newID);
    }

}
