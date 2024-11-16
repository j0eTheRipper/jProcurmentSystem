package procurmentsystem.Orders;

import procurmentsystem.Inventory;
import procurmentsystem.Table;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.regex.Pattern;

public abstract class Order {
    protected String ID;
    protected List<Inventory> itemsList;
    protected Table storage;
    protected Status status;

    abstract public void saveOrder();
    abstract public void setOrderStatus(Status newStatus);
    abstract public String getOrderStatus();
    abstract public String showOrderSummary();

    protected String generateOrderID() throws FileNotFoundException {
        List<String> lastRow = storage.getLastRow();
        String id = lastRow.get(0);
        Pattern idGetter = Pattern.compile("\\d+");
        return idGetter.matcher(id).group(0);
    }

}
