package procurmentsystem;

import java.io.IOException;
import procurmentsystem.Table.*;

public class PurchaseOrder extends requisition{
    //attribute
    private Table table;
    private String POID;
    private String requisID;
    private String itemID;
    private String status;
    private double price;

    //constructor
    public PurchaseOrder(String POID, String requisID, String itemID, double price){
        this.POID = POID;           //purchase order id
        this.requisID = requisID;   //requisition id
        this.itemID = itemID;       //item id
        this.price = price;         //price for purchase order
        this.status = "Pending";    //default the status to pending
    }

    public PurchaseOrder() throws IOException{
        this.table = new Table("src/files/purchaseOrders.csv");
    }


    //approve, reject status
    public String statusApprove(){
        return this.status = "Approved";
    }
    public String statusReject(){
        return this.status = "Rejected";
    }

    //Getter 
    public String POID(){
        return POID;
    }
    public String requisID(){
        return requisID;
    }
    public String itemID(){
        return itemID;
    }
    public double price(){
        return price;
    }
    public String status(){
        return status;
    }


    //methods
    //generate purchase order with status pending as default
    public boolean generatePO(){
        try{
            String[] newRowPO = {
                POID,
                getRequisID(),
                String.join(";", getItemCode()),
                String.valueOf(price),
                status
            };
            table.addRow(newRowPO);
            System.out.println("Purchase Order generated.");
            return true;
        }catch(IncorrectNumberOfValues e){
            System.out.println("Failed to generate Purchase Order : Incorrect values.");
        }
        return false;
    }

    //update Purchase Order by POID
    public boolean updatePOitem(String columnName, String newValue){
        try {
            int rowIndex = table.getRowIndex("POID", cell -> cell.equals(POID));
            table.updateRow(rowIndex, columnName, newValue);
            System.out.println("Updated successfully.");
            return true;
        } catch (ValueNotFound e) {
            System.out.println("Error: Purchase Order not found.");
        }
        return false;
    }

    //view one purchase order
    public void viewPO(){
        System.out.println("Purchase Order ID : "+ POID);
        System.out.println("Requisition ID : "+ requisID);
        System.out.println("Item ID : "+ itemID);
        System.out.println("Price : "+ price);
        System.out.println("Status of approval : "+ status);
    }

    //delete purchase order by ID
    public boolean deletePO(){
        try {
            int rowIndex = table.getRowIndex("POID", cell -> cell.equals(POID));
            table.deleteRow(rowIndex);
            System.out.println("Purchase Order deleted.");
            return true;
        } catch (ValueNotFound e) {
            System.out.println("Error: Purchase Order not found.");
        }
        return false;
    }

}