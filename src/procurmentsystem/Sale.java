package procurmentsystem;

import procurmentsystem.Table.*;

import java.io.FileNotFoundException;
import java.util.List;

public class Sale extends InteractionsWithTable {
    private String saleID;
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
            List<String> itemRow = itemsTable.getRow("itemCode", code -> code.equals(itemCode));
            if (itemRow == null) {
                throw new RuntimeException("ItemCode " + itemCode + " not found.");
            }
            return Double.parseDouble(itemRow.get(4)); // Assuming pricePerUnit is column 4
        } catch (ValueNotFound e) {
            throw new RuntimeException("Error fetching price: " + e.getMessage());
        }
    }

    // Add a sale
    @Override
    public boolean add() {
        try {
            // Check stock availability
            List<String> itemRow = itemsTable.getRow("itemCode", code -> code.equals(itemCode));
            if (itemRow == null) {
                System.out.println("Error: ItemCode " + itemCode + " not found.");
                return false;
            }

            int stock = Integer.parseInt(itemRow.get(3)); // Assuming stock is column 3
            if (stock < qty) {
                System.out.println("Error: Insufficient stock for ItemCode " + itemCode);
                return false;
            }

            // Deduct stock in Items.csv
            int newStock = stock - qty;
            itemsTable.updateRow(itemsTable.getRowIndex("itemCode", code -> code.equals(itemCode)), "quantity", String.valueOf(newStock));

            // Add sale to Sales.csv
            String[] saleData = {saleID, itemCode, String.valueOf(qty), String.valueOf(price)};
            salesTable.addRow(saleData);

            System.out.println("Sale added successfully: SaleID: " + saleID + ", ItemCode: " + itemCode + ", Qty: " + qty + ", Price: " + price);
            return true;
        } catch (IncorrectNumberOfValues | ValueNotFound e) {
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
                return false;
            }

            String itemCode = saleRow.get(1);
            int oldQty = Integer.parseInt(saleRow.get(2));

            // Update stock in Items.csv
            List<String> itemRow = itemsTable.getRow("itemCode", code -> code.equals(itemCode));
            if (itemRow == null) {
                System.out.println("Error: ItemCode " + itemCode + " not found.");
                return false;
            }

            int stock = Integer.parseInt(itemRow.get(3)); // Assuming stock is column 3
            int adjustedStock = stock + oldQty - Integer.parseInt(newValue);
            if (adjustedStock < 0) {
                System.out.println("Error: Insufficient stock for new quantity.");
                return false;
            }

            itemsTable.updateRow(itemsTable.getRowIndex("itemCode", code -> code.equals(itemCode)), "quantity", String.valueOf(adjustedStock));

            // Update sale quantity in Sales.csv
            salesTable.updateRow(salesTable.getRowIndex("saleID", id -> id.equals(saleID)), columnName, newValue);

            System.out.println("Sale updated successfully: SaleID: " + saleID + ", New Qty: " + newValue);
            return true;
        } catch (ValueNotFound e) {
            System.out.println("Error updating sale: " + e.getMessage());
            return false;
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
            List<String> itemRow = itemsTable.getRow("itemCode", code -> code.equals(itemCode));
            if (itemRow == null) {
                System.out.println("Error: ItemCode " + itemCode + " not found.");
                return false;
            }

            int stock = Integer.parseInt(itemRow.get(3)); // Assuming stock is column 3
            itemsTable.updateRow(itemsTable.getRowIndex("itemCode", code -> code.equals(itemCode)), "quantity", String.valueOf(stock + qty));

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
