package procurmentsystem.Table;

import procurmentsystem.Item;
import procurmentsystem.Users.User;

import java.util.HashMap;

public abstract class Order extends InteractionsWithTable{
    protected HashMap<Item, Integer> items;
    protected User placer;
    protected User approvedBy;
    protected Status status;
    protected double totalPrice;
}
