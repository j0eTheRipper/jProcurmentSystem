package procurmentsystem;
import procurmentsystem.Table.Table;

import java.util.List;
import java.util.function.Function;

public class requisition {
    private final Table table;
    private String requisID; // Unique identifier for the requisition
    private List<Item> items; // List of items in the requisition
    private PurchaseOrder purchaseOrder; // Associated PurchaseOrder (if any)
    // Constructor
    public requisition(String requisID, List<Item> items) {
        this.table = new Table("src/files/requisition.csv");
        this.requisID = requisID;
        this.items = items;
        this.purchaseOrder = null; // No PO linked initially
    }

    // Getters and Setters
    public String getRequisID() {
        return requisID;
    }

    public void setRequisID(String requisID) {
        this.requisID = requisID;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }


    // Method to display requisition details
    public void displayRequisition() {
        System.out.println("Requisition ID: " + requisID);
        System.out.println("Items in Requisition:");
        for (Item item : items) {
            System.out.println("- " + item.getItemName() + " (Code: " + item.getItemCode() + ")");
        }

        }
    public void createRequisition(String requisID, String requester, String purchaseOrderID, String approvalStatus, String dateCreated, String itemID, String qtyPerID) throws Table.IncorrectNumberOfValues {
        String[] newRequisition = {requisID, requester, purchaseOrderID, approvalStatus, dateCreated, itemID, qtyPerID};
        table.addRow(newRequisition);
    }
    // Read a single requisition by ID
    public List<String> readRequisition(String requisID) {
        return table.getRow("requisID", id -> id.equals(requisID));
    }

    // Read all requisitions with a filter (e.g., by id)
    public List<List<String>> readRequisitions(String column, Function<String, Boolean> filter) {
        return table.getRows(column, filter);
    }

    // Update an existing requisition by ID
    public void updateRequisition(String requisID, String columnToEdit, String newValue) throws Table.ValueNotFound {
        int rowIndex = table.getRowIndex("requisID", id -> id.equals(requisID));
        table.updateRow(rowIndex, columnToEdit, newValue);
    }

    // Delete a requisition by ID
    public void deleteRequisition(String requisID) throws Table.ValueNotFound {
        int rowIndex = table.getRowIndex("requisID", id -> id.equals(requisID));
        table.deleteRow(rowIndex);
    }
}