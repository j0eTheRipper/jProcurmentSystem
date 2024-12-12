package procurmentsystem;
import java.io.*;
import java.util.*;

public class PurchaseOrder {
    private String orderID;
    private String requisitionID;
    private String itemID;
    private int price;
    private boolean isApproved;
    private UserManager ApprovedStaff;

    //constructor
    public PurchaseOrder(String orderID, String requisitionID, String itemID, int price){
        this.orderID = orderID;
        this.requisitionID = requisitionID;
        this.itemID = itemID;
        this.price = price;
        this.isApproved = false; //by default is false to avoid auto approval.
    }

    //methods
    //approve or reject PO
    public void approvePO(){
        this.isApproved = true;
    }

    public void rejectPO(){
        this.isApproved = false;
    }
    //add items to purchase order list
    public void additemsPO(String requisitionID, String itemID, int price){

    }
    //save generate PO 
    public void savePOform()  throws IOException{
        //save file things, write
    }



    //getters
    public String getOrderID() {
        return orderID;
    }

    public String getRequisitionID() {
        return requisitionID;
    }

    public String getItemID() {
        return itemID;
    }

    public int getPrice() {
        return price;
    }

    public boolean isApproved() {
        return isApproved;
    }
}
