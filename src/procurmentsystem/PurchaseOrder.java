package procurmentsystem;

import java.io.IOException;
import java.util.List;
import procurmentsystem.Table.IncorrectNumberOfValues;
import procurmentsystem.Table.Table;
import java.util.Scanner;

public class PurchaseOrder{
    //attribute
    private Table table;
    private String requisID;
    private String itemID;
    private List<Item> items;
    private String POID;
    private String status;
    private boolean statusApproval;
    private double price;

    //constructor
    public PurchaseOrder(String POID,String status,String requisID, String itemID,double price, boolean statusApproval){
        this.POID = POID; //Purchase order ID
        this.status = "Pending";//defaulted to pending, waiting approve/reject
        this.requisID = requisID;
        this.itemID = itemID;
        this.price = price;//total price
        this.statusApproval = false; //default for approval

    }

    public PurchaseOrder() throws IOException{
        this.table = new Table("src/files/purchaseOrders.csv");
    }


    //
    public void readPOFile(){

    }

    //approve, reject, paid status
    public String statusApprove(){
        return this.status = "Approved";
    }
    public String statusReject(){
        return this.status = "Rejected";
    }

    //getter
    public String getPOID(){
        return POID;
    }
    public String getStatus(){
        return status;
    }
    public double getPrice(){
        return price;
    }

    //setter
    public void setPOID(String POID) throws IncorrectNumberOfValues{
        if (POID == null || POID.isBlank()){
            System.out.println("Please set a purchase order ID.");
        }
        else{
            String[] newPOID = {POID};
            table.addRow(newPOID);
        }
    }

    public void setPrice(double price) throws IncorrectNumberOfValues{
        if (price <= 0.0){
            System.out.println("Please set a price for more than a 0.0");
        }
        else{
            
        }
    }

    public String setStatus(boolean statusApproval){
        if (statusApproval = true){
            return statusApprove();
        }
        else{
            return  statusReject();
        }
    }

    public void setItemID(String itemID){
        if (Item.itemCode != null){
            String[] newItemCode = {itemID};
            table.updateRow(rowIndex, "ItemID", newItemCode);
        }
        else{
            System.out.println("Item does not exist.");
        }
    }


    //methods
    //generate purchase order with status pending as default
    public void generatePurchaseOrder(String POID, String requisID, String itemID, double price, String status){

    }

    //change status of the purchase order
    public void updatePOStatus(Scanner sc, String POID, String status){

    }

    //view one purchase order
    public void viewPurchaseOrder(){
        System.out.println("Purchase Order ID : "+ POID);
        System.out.println("Requisition ID : "+ requisID);
        System.out.println("Item ID : "+ itemID);
        System.out.println("Price : "+ price);
        System.out.println("Status of approval : "+ status);
    }

    //view list of purchase orders by ID
    public void viewAllPO(){
        System.out.println("----------Purchase Orders by ID----------");
        
    }
}