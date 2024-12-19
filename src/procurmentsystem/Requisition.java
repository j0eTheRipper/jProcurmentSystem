package procurmentsystem;

import procurmentsystem.Table.*;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

public class Requisition extends Order {
    private String requisID;
    private String requesterName;
    private String PurchaseOrder;
    private String status;
    private String itemCode;
    private Integer qtyPerID;
    public Requisition(){
        try {
            this.table = new Table("src/files/requisition.csv");
        }
        catch (FileNotFoundException e){
            System.out.println("File not found");
        }
    }
    // Constructor
    public Requisition(String requisID, String requesterName, String PurchaseOrder, String status, Date dateCreated, String itemCode, Integer qtyPerID) {
        this.requisID= requisID;
        this.requesterName= requesterName;
        this.PurchaseOrder= PurchaseOrder;
        this.status= status;
        this.itemCode= itemCode;
        this.qtyPerID=qtyPerID;
        try {
            table = new Table("src/files/requisition.csv");
            ID = generateID();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    public Requisition(String requisID, String requesterName, String PurchaseOrder, String status, String itemCode, String qtyPerID) {
        this.requisID= requisID;
        this.requesterName= requesterName;
        this.PurchaseOrder= PurchaseOrder;
        this.status= status;
        this.itemCode= itemCode;
        this.qtyPerID= Integer.parseInt(qtyPerID);
        try {
            table = new Table("src/files/requisition.csv");
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    // Getters and Setters
    public static Requisition get(String value, Function<String, Boolean> filter) {
        try {
            Table table = new Table("src/files/requisition.csv");
            List<String> row = table.getRow(value, filter);

            if (row == null || row.size() < 8) {
                System.out.println("No requisition found matching the criteria.");
                return null;
            }

            String requisID = row.get(6);
            Requisition result = Requisition.get("requisID", id -> id.equals(requisID));

            if (result == null) {
                System.out.println("Requisition not found for given Requisition ID.");
                return null;
            }

            return new Requisition(
                    row.get(0),  // requisID
                    row.get(1),  // requesterName
                    row.get(2),  // purchaseOrderID
                    row.get(3),  // approvalStatus
                    row.get(4),  // itemCode
                    row.get(5)   // qtyPerId
            );
        } catch (FileNotFoundException e) {
            System.out.println("File name is incorrect");
            return null;
        } catch (ValueNotFound e) {
            System.out.println("Value not found!");
            return null;
        }
    }

    public String getRequisID() {
        return requisID;
    }
    public String getRequesterName() {
        return requesterName;
    }
    public String getPurchaseOrder() {
        return PurchaseOrder;
    }
    public String getStatus() {
        return status;
    }
    public String getItemCode() {
        return itemCode;
    }
    public Integer getQtyPerID() {
        return qtyPerID;
    }
    // Setters with validation
    public boolean setRequisID(String requisID) {
        if (requisID == null || !requisID.matches("[0-9]+"))
            return false;
        this.update("requisID", this.requisID, requisID);
        this.requisID = requisID;
        return true;
    }

    public boolean setRequesterName(String requesterName) {
        if (requesterName == null || !requesterName.matches("[A-Za-z ]+"))
            return false;
        this.update("requesterName", this.requesterName, requesterName);
        this.requesterName = requesterName;
        return true;
    }

    public boolean setPurchaseOrder(String purchaseOrder) {
        if (purchaseOrder == null || purchaseOrder.isEmpty())
            return false;
        this.update("PurchaseOrder", this.PurchaseOrder, purchaseOrder);
        this.PurchaseOrder = purchaseOrder;
        return true;
    }

    public boolean setStatus(String status) {
        if (status == null || !(status.equals("Pending") || status.equals("Approved") || status.equals("Rejected")))
            return false;
        this.update("status", this.status, status);
        this.status = status;
        return true;
    }

    public boolean setItemCode(String itemCode) {
        if (itemCode == null || !itemCode.matches("ITEM[0-9]+"))
            return false;
        this.update("itemCode", this.itemCode, itemCode);
        this.itemCode = itemCode;
        return true;
    }

    public boolean setQtyPerID(Integer qtyPerID) {
        if (qtyPerID == null || qtyPerID <= 0)
            return false;
        this.update("qtyPerID", String.valueOf(this.qtyPerID), String.valueOf(qtyPerID));
        this.qtyPerID = qtyPerID;
        return true;
    }

    public boolean add() {
        try {
            String[] values = {
                    requisID,             // RequisID
                    requesterName,        // RequesterName
                    PurchaseOrder,        // PurchaseOrder
                    status,               // Status
                    itemCode,             // ItemCode
                    String.valueOf(qtyPerID)  // QtyPerID
            };

            table.addRow(values);
            return true;
        } catch (IncorrectNumberOfValues e) {
            System.out.println("Incorrect Input: " + e.getMessage());
            return false;
        }
    }


    public boolean delete() {
        try {
            int rowIndex = table.getRowIndex("requisID", id -> id.equals(requisID));
            table.deleteRow(rowIndex);
            System.out.println("Requisition with ID " + requisID + " has been deleted.");
            return true;
        } catch (ValueNotFound e) {
            System.out.println("Requisition with ID " + requisID + " was not found in the file.");
            return false;
        }
    }


    @Override
    protected boolean update(String columnName, String oldValue, String newValue) {
        try {
            table.updateRow(table.getRowIndex(columnName, (x) -> x.equals(oldValue)), columnName, newValue);
            return true;
        } catch (ValueNotFound e) {
            System.out.println("Requisition ID not found.");
            return false;
        }
    }

    // Override toString method to display requisition details
    @Override
    public String toString() {
        return String.format("%-10s | %-15s | %-12s | %-15s | %-9s | %-5s",
                requisID, requesterName, PurchaseOrder, status, itemCode, qtyPerID);
    }
}