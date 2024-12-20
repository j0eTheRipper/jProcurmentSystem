package procurmentsystem;

import procurmentsystem.Table.*;

import java.io.FileNotFoundException;
import java.util.List;

public class Sale extends InteractionsWithTable {
    String saleID;
    private String itemCode;
    private int qty;
    private double price;
    private final Table salesTable;
    private final Table itemsTable;

    // Constructor
    public Sale() {
        try {
            this.salesTable = new Table("src/files/sales.csv");
            this.itemsTable = new Table("src/files/Items.csv");
            this.table = salesTable; // Set this.table for generateID to work
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + e.getMessage());
        }
    }

    public Sale(String itemCode, int qty) throws FileNotFoundException {
        this();
        this.saleID = String.valueOf(generateID()); // Call generateID from InteractionsWithTable
        this.itemCode = itemCode;
        this.qty = qty;
        this.price = getPriceFromItem(itemCode); // Fetch price from Items.csv
    }

    // Fetch the price from Items.csv
    private double getPriceFromItem(String itemCode) {
        try {
            List<String> itemRow = itemsTable.getRow("ItemCode", code -> code.equals(itemCode));
            if (itemRow == null) {
                throw new RuntimeException("ItemCode " + itemCode + " not found.");
            }
            return Double.parseDouble(itemRow.get(6)); // Assuming RecommendedSalesPrice is column 6
        } catch (ValueNotFound e) {
            throw new RuntimeException("Error fetching price: " + e.getMessage());
        }
    }
    public void salesReport() {
        try {
            // Get all sales rows
            List<List<String>> salesRows = salesTable.getRows("saleID", id -> !id.isEmpty());

            double totalSales = 0;

            // Iterate through each sale record
            for (List<String> saleRow : salesRows) {
                String itemCode = saleRow.get(1); // Get itemCode from sales.csv
                int qtySold = Integer.parseInt(saleRow.get(2)); // Get qty from sales.csv

                // Fetch the item from Items.csv using the itemCode
                List<String> itemRow = itemsTable.getRow("ItemCode", code -> code.equals(itemCode));
                if (itemRow == null) {
                    System.out.println("Warning: ItemCode " + itemCode + " not found in Items.csv.");
                    continue;
                }

                // Get RecommendedSalesPrice
                double recommendedSalesPrice = Double.parseDouble(itemRow.get(6)); // RecommendedSalesPrice is column 6

                // Calculate total for this sale
                double saleTotal = qtySold * recommendedSalesPrice;
                totalSales += saleTotal;

                // Print individual sale details
                System.out.printf("SaleID: %s | ItemCode: %s | Qty: %d | Unit Price: %.2f | Total: %.2f%n",
                        saleRow.get(0), itemCode, qtySold, recommendedSalesPrice, saleTotal);
            }

            // Print total sales
            System.out.println("=".repeat(50));
            System.out.printf("Total Sales: %.2f%n", totalSales);
            System.out.println("=".repeat(50));

        } catch (ValueNotFound e) {
            System.out.println("Error processing sales report: " + e.getMessage());
        }
    }

    // Add a sale
    public boolean add() {
        try {
            // Check stock availability
            List<String> itemRow = itemsTable.getRow("ItemCode", code -> code.equals(itemCode)); // Correct column capitalization
            if (itemRow == null) {
                System.out.println("Error: ItemCode " + itemCode + " not found.");
                return false;
            }

            // Get the current stock
            int stock = Integer.parseInt(itemRow.get(3)); // Assuming ItemQuantity is column 3
            if (stock < qty) {
                System.out.println("Error: Insufficient stock for ItemCode " + itemCode);
                return false;
            }

            // Deduct stock in Items.csv
            int newStock = stock - qty;
            itemsTable.updateRow(itemsTable.getRowIndex("ItemCode", code -> code.equals(itemCode)), "ItemQuantity", String.valueOf(newStock)); // Correct capitalization

            // Add sale to Sales.csv
            String[] saleData = {saleID, itemCode, String.valueOf(qty), String.valueOf(price)};
            salesTable.addRow(saleData);

            System.out.println("Sale added successfully: SaleID: " + saleID + ", ItemCode: " + itemCode + ", Qty: " + qty + ", Price: " + price);
            return true;
        } catch (ValueNotFound e) {
            System.out.println("Error updating stock: " + e.getMessage());
            return false;
        } catch (IncorrectNumberOfValues e) {
            System.out.println("Error adding sale: " + e.getMessage());
            return false;
        }
    }

    // Edit a sale
    @Override
    public boolean update(String columnName, String oldValue, String newValue) {
        try {
            // Get the sale record
            List<String> saleRow = salesTable.getRow("saleID", id -> id.equals(saleID));
            if (saleRow == null) {
                System.out.println("Error: SaleID " + saleID + " not found.");
                return false; // Return immediately on error
            }

            String itemCode = saleRow.get(1); // Get ItemCode from sales.csv
            int oldQty = Integer.parseInt(saleRow.get(2)); // Get old quantity from sales.csv

            // Update stock in Items.csv
            List<String> itemRow = itemsTable.getRow("ItemCode", code -> code.equals(itemCode));
            if (itemRow == null) {
                System.out.println("Error: ItemCode " + itemCode + " not found in Items.csv.");
                return false; // Return immediately on error
            }

            int stock = Integer.parseInt(itemRow.get(3)); // Assuming ItemQuantity is column 3
            int adjustedStock = stock + oldQty - Integer.parseInt(newValue); // Revert old sale qty and apply new qty
            if (adjustedStock < 0) {
                System.out.println("Error: Insufficient stock for new quantity.");
                return false; // Return immediately on error
            }

            // Update stock
            itemsTable.updateRow(itemsTable.getRowIndex("ItemCode", code -> code.equals(itemCode)), "ItemQuantity", String.valueOf(adjustedStock));

            // Update sale quantity in Sales.csv
            salesTable.updateRow(salesTable.getRowIndex("saleID", id -> id.equals(saleID)), "qty", newValue);

            System.out.println("Sale updated successfully: SaleID: " + saleID + ", New Qty: " + newValue);
            return true; // Return success only when everything works
        } catch (ValueNotFound e) {
            System.out.println("Error updating sale: Sale ID or ItemCode not found.");
            return false; // Return false for the error
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            return false; // Return false for unexpected errors
        }
    }

    // Delete a sale
    @Override
    public boolean delete() {
        try {
            // Get the sale record
            List<String> saleRow = salesTable.getRow("saleID", id -> id.equals(saleID));
            if (saleRow == null) {
                System.out.println("Error: SaleID " + saleID + " not found.");
                return false;
            }

            String itemCode = saleRow.get(1);
            int qty = Integer.parseInt(saleRow.get(2));

            // Restore stock in Items.csv
            List<String> itemRow = itemsTable.getRow("ItemCode", code -> code.equals(itemCode));
            if (itemRow == null) {
                System.out.println("Error: ItemCode " + itemCode + " not found.");
                return false;
            }

            int stock = Integer.parseInt(itemRow.get(3)); // Assuming stock is column 3
            itemsTable.updateRow(itemsTable.getRowIndex("ItemCode", code -> code.equals(itemCode)), "quantity", String.valueOf(stock + qty));

            // Delete sale from Sales.csv
            salesTable.deleteRow(salesTable.getRowIndex("saleID", id -> id.equals(saleID)));

            System.out.println("Sale deleted successfully: SaleID: " + saleID);
            return true;
        } catch (ValueNotFound e) {
            System.out.println("Error deleting sale: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String toString() {
        return String.format("SaleID: %s | ItemCode: %s | Qty: %d | Price: %.2f", saleID, itemCode, qty, price);
    }
}
