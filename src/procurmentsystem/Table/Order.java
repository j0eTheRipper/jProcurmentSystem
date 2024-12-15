package procurmentsystem.Table;

import procurmentsystem.Item;
import procurmentsystem.User;

import java.util.List;

public abstract class Order extends InteractionsWithTable{
    protected List<Item> items;
    protected User placer;
    protected User approvedBy;
    protected Status status;
    protected double totalPrice;
}
