package procurmentsystem;
import java.util.*;
import java.io.*;

public class PurchaseManager {
    private List<PurchaseOrder> purchaseOrders = new ArrayList<>(); //List the purchase orders?? have to add viewing.

    //show user purchase manager, name and uuid
    //view the purchase order list
    //create a purchase order
    public void generatePurchaseOrder(String orderID, String requisitionID, String itemID, int price){
        PurchaseOrder po = new PurchaseOrder(orderID, requisitionID, itemID, price);
        purchaseOrders.add(po); //TBA
    }
}
