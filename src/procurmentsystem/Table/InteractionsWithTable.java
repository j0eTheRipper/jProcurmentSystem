package procurmentsystem.Table;

import java.io.FileNotFoundException;
import java.util.List;

public abstract class InteractionsWithTable {
    protected String ID;
    protected Table table;


    abstract public String toString();
    abstract protected boolean add();
    abstract protected boolean delete();

    protected boolean update(String columnName, String oldValue, String newValue) {
        try {
            table.updateRow(table.getRowIndex(columnName, (x) -> x.equals(oldValue)), columnName, newValue);
            return true;
        } catch(ValueNotFound e){
            System.out.println("ID not found.");
            return false;
        }
    }

    protected String generateID() throws FileNotFoundException {
        List<String> lastRow = table.getLastRow();

        int newID = 1;

        if (lastRow != null && !lastRow.isEmpty()) {
            try {
                String id = lastRow.get(0);
                if (id != null && !id.isEmpty()) {
                    newID = Integer.parseInt(id) + 1;
                } else {
                    System.out.println("Warning: ID in last row is empty. Starting from 1.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Warning: Invalid ID format in last row. Starting from 1.");
            }
        } else {
            System.out.println("No rows found in the table. Starting ID from 1.");
        }

        return String.valueOf(newID);
    }


}
