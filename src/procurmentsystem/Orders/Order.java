package procurmentsystem.Orders;

import procurmentsystem.Item;
import procurmentsystem.Table.Table;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.regex.Pattern;

public abstract class Order {
    protected String ID;
    protected List<Item> itemsList;
    protected Table storage;
    protected Status status;

    abstract public void setOrderStatus(Status newStatus);
    abstract public String getOrderStatus();
    abstract public String toString();

    protected String generateOrderID() throws FileNotFoundException {
        List<String> lastRow = storage.getLastRow();
        String id = lastRow.get(0);
        Pattern idGetter = Pattern.compile("\\d+");
        int newID = Integer.parseInt(idGetter.matcher(id).group(0)) + 1;
        return String.valueOf(newID);
    }

}
