package procurmentsystem;

import java.io.FileNotFoundException;

public class saleManager extends User {

    // Constructor - Initializes SalesManager without needing superclass constructor
    public saleManager() {
        // No additional setup required
    }

    // 1. View Items
    public void viewItems() {
        System.out.println("Viewing all items...");
        // Placeholder for implementation
    }

    // 2. Add Sales Entry
    public void addSalesEntry(String itemCode, String date, int quantitySold) {
        System.out.println("Adding sales entry...");
        // Placeholder for implementation
    }

    // 3. Edit Sales Entry
    public void editSalesEntry(String saleID, int newQuantitySold) {
        System.out.println("Editing sales entry...");
        // Placeholder for implementation
    }

    // 4. Delete Sales Entry
    public void deleteSalesEntry(String saleID) {
        System.out.println("Deleting sales entry...");
        // Placeholder for implementation
    }

    // 5. Create Requisition
    public void createRequisition(String requisID, String itemCode, int quantity) {
        System.out.println("Creating requisition...");
        // Placeholder for implementation
    }

    // 6. View Purchase Orders
    public void viewPurchaseOrders() {
        System.out.println("Viewing purchase orders...");
        // Placeholder for implementation
    }
}
