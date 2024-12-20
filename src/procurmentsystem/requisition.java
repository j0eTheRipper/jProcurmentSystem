package procurmentsystem;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import procurmentsystem.Table.*;

public class requisition extends Order {

    private Table requestedItemsTable;
    private String requisID;
    private String requesterName;
    private String PurchaseOrder;
    private String status;
    private Map<String, Integer> items;

    public requisition() {
        try {
            this.table = new Table("src/files/requisition.csv");
            this.requestedItemsTable = new Table("src/files/requestedItems.csv");
            this.items = new HashMap<>();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }


    public requisition(String requesterName, String purchaseOrderID, String status) {
        this.requesterName= requesterName;
        this.PurchaseOrder= purchaseOrderID;
        this.status= status;
        try {
            requestedItemsTable = new Table("src/files/requestedItems.csv");
            table = new Table("src/files/requisition.csv");
            ID = generateID();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }



    public static requisition get(String value, Function<String, Boolean> filter) {
        try {
            Table table = new Table("src/files/requisition.csv");
            List<String> row = table.getRow(value, filter);

            if (row == null || row.size() < 6) {
                System.out.println("No requisition found matching the criteria.");
                return null;
            }

            String requisID = row.get(4);
            requisition result = requisition.get("requisID", id -> id.equals(requisID));

            if (result == null) {
                System.out.println("Requisition not found for given Requisition ID.");
                return null;
            }

            return new requisition();
        } catch (FileNotFoundException e) {
            System.out.println("File name is incorrect");
            return null;
        } catch (ValueNotFound e) {
            System.out.println("Value not found!");
            return null;
        }
    }

    public static List<requisition> getMultiple(String column, Function<String, Boolean> filter) {
        try {
            // Load requisition.csv
            Table requisitionTable = new Table("src/files/requisition.csv");
            List<List<String>> requisitionRows = requisitionTable.getRows(column, filter);
            List<requisition> requisitions = new ArrayList<>();
            for (List<String> row : requisitionRows) {
                String requisID = row.get(0);
                String requesterName = row.get(1);
                String POID = row.get(2);
                Status status = Status.valueOf(row.get(3));
            }
            return requisitions;

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
            return new ArrayList<>();
        } catch (ValueNotFound e) {
            System.out.println("No requisitions found matching the filter.");
            return new ArrayList<>();
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


    // Save a single item with its quantity to requestedItems.csv
    private boolean saveRequestedItems() {
        try {
            for (Map.Entry<String, Integer> entry : items.entrySet()) {
                String[] requestedItemData = {requisID, entry.getKey(), String.valueOf(entry.getValue())};
                requestedItemsTable.addRow(requestedItemData);
            }
            System.out.println("All requested items saved for Requisition ID: " + requisID);
            return true;
        } catch (IncorrectNumberOfValues e) {
            System.out.println("Error saving requested items: " + e.getMessage());
            return false;
        }
    }

    // Load items for this requisition from requestedItems.csv into HashMap
    public void loadRequestedItems() {
        try {
            List<List<String>> rows = requestedItemsTable.getRows("requisID", id -> id.equals(requisID));
            for (List<String> row : rows) {
                String itemCode = row.get(1);
                int qtyPerID = Integer.parseInt(row.get(2));
                items.put(itemCode, qtyPerID);
            }
            System.out.println("Requested items loaded for Requisition ID: " + requisID);
        } catch (ValueNotFound e) {
            System.out.println("No requested items found for Requisition ID: " + requisID);
        }
    }

    // Add an item and its quantity to the hashmap
    public void addItem(String itemCode, int qtyPerID) {
        if (qtyPerID <= 0) {
            System.out.println("Error: Quantity must be greater than 0.");
            return;
        }
        items.put(itemCode, items.getOrDefault(itemCode, 0) + qtyPerID);
        System.out.println("Item added: ItemCode: " + itemCode + ", Quantity: " + qtyPerID);
    }

    // Remove an item
    public boolean removeItem(String itemCode) {
        if (items.containsKey(itemCode)) {
            items.remove(itemCode);
            System.out.println("Item removed: ItemCode: " + itemCode);
            return true;
        } else {
            System.out.println("Error: ItemCode " + itemCode + " not found in this requisition.");
            return false;
        }
    }

    // Update the quantity for an item
    public boolean updateItemQuantity(String itemCode, int newQtyPerID) {
        if (items.containsKey(itemCode)) {
            items.put(itemCode, newQtyPerID);
            System.out.println("Updated quantity for ItemCode: " + itemCode + " to " + newQtyPerID);
            return true;
        } else {
            System.out.println("Error: ItemCode " + itemCode + " not found in this requisition.");
            return false;
        }
    }




    // Save the requisition to requisition.csv and its items to requestedItems.csv
    @Override
    public boolean add() {
        try {
            String[] requisitionData = {requisID, requesterName, PurchaseOrder, status};
            table.addRow(requisitionData);
            saveRequestedItems(); // Save items to requestedItems.csv
            System.out.println("Requisition saved: ID: " + requisID);
            return true;
        } catch (IncorrectNumberOfValues e) {
            System.out.println("Error saving requisition: " + e.getMessage());
            return false;
        }
    }

    // Delete the requisition and all its items
    @Override
    public boolean delete() {
        try {
            int rowIndex = table.getRowIndex("requisID", id -> id.equals(requisID));
            table.deleteRow(rowIndex);

            List<Integer> itemRows = requestedItemsTable.getRowIndexes("requisID", id -> id.equals(requisID));
            for (int rowIndexToDelete : itemRows) {
                requestedItemsTable.deleteRow(rowIndexToDelete);
            }

            System.out.println("Requisition with ID " + requisID + " and its items have been deleted.");
            return true;
        } catch (ValueNotFound e) {
            System.out.println("Error: Requisition with ID " + requisID + " not found for deletion.");
            return false;
        }
    }

    @Override
    protected boolean update(String columnName, String oldValue, String newValue) {
        try {
            table.updateRow(table.getRowIndex(columnName, value -> value.equals(oldValue)), columnName, newValue);
            System.out.println("Updated " + columnName + " for Requisition ID: " + requisID);
            return true;
        } catch (ValueNotFound e) {
            System.out.println("Error: Value not found for update.");
            return false;
        }
    }

    // Display requisition details and items
    public void displayRequisition() {
        System.out.println("Requisition ID: " + requisID);
        System.out.println("Requester: " + requesterName);
        System.out.println("Purchase Order: " + PurchaseOrder);
        System.out.println("Status: " + status);
        System.out.println("Items:");
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            System.out.println("- ItemCode: " + entry.getKey() + ", Quantity: " + entry.getValue());
        }
    }
    // Override toString method to display requisition details
    @Override
    public String toString() {
        return String.format("%-10s | %-15s | %-12s | %-15s | %-9s | %-5s",
                requisID, requesterName, PurchaseOrder, status);
    }
}