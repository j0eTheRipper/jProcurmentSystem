package procurmentsystem;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import procurmentsystem.Table.*;

public class PurchaseOrder {
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
            //making sure fields are not empty/null
            if (POID == null || requisID == null || itemID == null || price < 0){
                System.out.println("Error: Purchase Order is incomplete.");
                return false;
            }

            String[] newRowPO = {
                POID,
                requisID,
                itemID,
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
        try {
            List<String> row = table.getRow("POID", cell -> cell.equals(POID));
            System.out.println("----- Purchase Order Details -----");
            System.out.println("POID : " + row.get(0));
            System.out.println("Requisition ID : " + row.get(1));
            System.out.println("Items : " + row.get(2));
            System.out.println("Total Price : " + row.get(3));
            System.out.println("Status : " + row.get(4));
        } catch (ValueNotFound e) {
            System.out.println("Error : Purchase Order not found.");
        }
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

    //input
    public void enterPODetails(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the PURCHASE ORDER ID: ");
        this.POID = sc.nextLine();
        System.out.println("Enter REQUISITION ID: ");
        this.requisID = sc.nextLine();
        System.out.println("Enter ITEM ID: ");
        this.itemID = sc.nextLine();
        System.out.println("Enter PRICE: ");
        while (true) {
            try {
                this.price = Double.parseDouble(sc.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a valid price (default : 0.0) : ");
            }
            
        }
        this.status = "Pending"; //default
    }

    //update status
    public void updateStatus(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Current status : " + this.status);
        System.out.println("Enter new status (Input 1 for Approved, 0 for Rejected): ");

        int choice;
        while (true){
            try {
                choice = Integer.parseInt(sc.nextLine());
                if (choice == 1){
                    statusApprove();
                    System.out.println("Status successfully updated to Approved.");
                    break;
                }
                else if(choice == 0){
                    statusReject();
                    System.out.println("Status successfully updated to Rejected.");
                    break;
                }
                else{
                    System.out.println("Error: Invalid input. only input 1 or 0: ");
                }
            } catch (NumberFormatException e) {
            }
        }
    }


    //override to string
    @Override
    public String toString(){
        return "PurchaseOrder{" +
        "POID='" + POID + '\'' +
        ", requisitionID='" +requisID +'\'' +
        ", items='" + itemID + '\'' +
        ", price='" + price +
        ", status='" + status + '\'' +
        '}';
    }

}