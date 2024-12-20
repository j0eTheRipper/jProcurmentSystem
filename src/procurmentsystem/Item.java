package procurmentsystem;

import procurmentsystem.Table.IncorrectNumberOfValues;
import procurmentsystem.Table.Table;
import procurmentsystem.Table.ValueNotFound;
import procurmentsystem.Table.InteractionsWithTable;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class Item extends InteractionsWithTable {
    private String itemName;
    private String itemDesc;
    private double pricePerUnit;
    private int moq; // Minimum Order Quantity
    private Supplier supplier;
    private double recommendedSalesPrice;
    private int itemQuantity;
    public Item() {
        try {
            table = new Table("src/files/items.csv");
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    public Item(String itemName, String itemDesc, double pricePerUnit, int moq, Supplier supplier, double recommendedSalesPrice,int itemQuantity) {
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.pricePerUnit = pricePerUnit;
        this.moq = moq;
        this.supplier = supplier;
        this.recommendedSalesPrice = recommendedSalesPrice;
        this.itemQuantity = itemQuantity;
        try {
            table = new Table("src/files/items.csv");
            ID = generateID();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    public Item(String itemCode, String itemName, String itemDesc, String itemQuantity,
                String pricePerUnit, String moq, Supplier supplier, String recommendedSalesPrice) {
        this.ID = itemCode;
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.itemQuantity = Integer.parseInt(itemQuantity);
        this.pricePerUnit = Double.parseDouble(pricePerUnit);
        this.moq = Integer.parseInt(moq);
        this.supplier = supplier;
        this.recommendedSalesPrice = Double.parseDouble(recommendedSalesPrice);
        try {
            table = new Table("src/files/items.csv");
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    public static Item get(String value, Function<String, Boolean> filter) {
        try {
            Table table = new Table("src/files/items.csv");
            List<String> row = table.getRow(value, filter);

            if (row == null || row.size() < 8) {
                System.out.println("No item found matching the criteria.");
                return null;
            }

            // Resolve supplierID and create a Supplier object
            String supplierID = row.get(6);
            Supplier supplier = Supplier.get("supplierID", id -> id.equals(supplierID));
            if (supplier == null) {
                System.out.println("Supplier not found for the given supplier ID.");
                return null;
            }

            return new Item(
                    row.get(0),  // ItemCode
                    row.get(1),  // ItemName
                    row.get(2),  // ItemDesc
                    row.get(3),  // ItemQuantity
                    row.get(4),  // PricePerUnit
                    row.get(5),  // MOQ
                    supplier,
                    row.get(7)   // RecommendedSalesPrice
            );

        } catch (FileNotFoundException e) {
            System.out.println("File name is incorrect");
            return null;
        } catch (ValueNotFound e) {
            System.out.println("Value not found!");
            return null;
        }
    }

    public static List<Item> getMultiple(String value, Function<String, Boolean> filter) {
        try {
            Table table = new Table("src/files/items.csv");
            List<List<String>> rows = table.getRows(value, filter);
            List<Item> items = new ArrayList<>();

            for (List<String> row : rows) {
                if (row == null || row.size() < 8) {
                    System.out.println("Skipping invalid row");
                    continue;
                }

                // Resolve supplierID into Supplier object
                String supplierID = row.get(6);
                Supplier supplier = Supplier.get("supplierID", id -> id.equals(supplierID));
                if (supplier == null) {
                    System.out.println("Skipping row: Supplier not found for ID: " + supplierID);
                    continue;
                }

                items.add(new Item(
                        row.get(0),  // ItemCode
                        row.get(1),  // ItemName
                        row.get(2),  // ItemDesc
                        row.get(3),  // ItemQuantity
                        row.get(4),  // PricePerUnit
                        row.get(5),  // MOQ
                        supplier,
                        row.get(7)   // RecommendedSalesPrice
                ));
            }
            return items;

        } catch (FileNotFoundException e) {
            System.out.println("File name is incorrect");
            return new ArrayList<>();
        } catch (ValueNotFound e) {
            throw new RuntimeException(e);
        }
    }

    public static void displayAllItems(Scanner scanner) {
        System.out.println("=".repeat(42) + " Current Items " + "=".repeat(42));
        System.out.println("Code | Item Name        | Description         | Quantity | Price/Unit | MOQ | Supplier   | Sales Price");
        System.out.println("=".repeat(100));
        List<Item> allItems = getMultiple("ItemCode", (x) -> !x.isEmpty());
        for (Item x : allItems) {
            System.out.println(x);
        }
        System.out.println("=".repeat(100));
    }

    // Getters
    public String getItemName() { return itemName; }
    public String getItemDesc() { return itemDesc; }
    public double getPricePerUnit() { return pricePerUnit; }
    public int getMoq() { return moq; }
    public Supplier getSupplier() { return supplier; }
    public double getRecommendedSalesPrice() { return recommendedSalesPrice; }
    public int getItemQuantity(){return itemQuantity;}

    // Setters with validation
    public boolean setItemName(String itemName) {
        if (!itemName.matches("[A-Za-z ]+"))
            return false;
        this.update("ItemName", this.itemName, itemName);
        this.itemName = itemName;
        return true;
    }

    public boolean setItemDesc(String itemDesc) {
        if (!itemDesc.matches("[A-Za-z0-9 ]+"))
            return false;
        this.update("ItemDesc", this.itemDesc, itemDesc);
        this.itemDesc = itemDesc;
        return true;
    }

    public boolean setPricePerUnit(double pricePerUnit) {
        if (pricePerUnit <= 0)
            return false;
        this.update("PricePerUnit", String.valueOf(this.pricePerUnit), String.valueOf(pricePerUnit));
        this.pricePerUnit = pricePerUnit;
        return true;
    }

    public boolean setMoq(int moq) {
        if (moq < 0)
            return false;
        this.update("MOQ", String.valueOf(this.moq), String.valueOf(moq));
        this.moq = moq;
        return true;
    }

    public boolean setSupplier(Supplier supplier) {
        if (supplier == null)
            return false;
        this.update("SupplierID", this.supplier.getsupplierID(), supplier.getsupplierID());
        this.supplier = supplier;
        return true;
    }

    public boolean setRecommendedSalesPrice(double recommendedSalesPrice) {
        if (recommendedSalesPrice <= 0)
            return false;
        this.update("RecommendedSalesPrice", String.valueOf(this.recommendedSalesPrice), String.valueOf(recommendedSalesPrice));
        this.recommendedSalesPrice = recommendedSalesPrice;
        return true;
    }
    public boolean increaseItemQuantity(int quantity) {
        if (quantity < 0)
            return false;
        int newQuantity = this.itemQuantity + quantity;
        this.update("ItemQuantity", String.valueOf(this.itemQuantity), String.valueOf(newQuantity));
        this.itemQuantity = newQuantity;
        return true;
    }

    public boolean add() {
        try {
            String[] values = {
                    ID,             // ItemCode
                    itemName,       // ItemName
                    itemDesc,       // ItemDesc
                    String.valueOf(itemQuantity),  // ItemQuantity
                    String.valueOf(pricePerUnit),  // PricePerUnit
                    String.valueOf(moq),           // MOQ
                    supplier.getsupplierID(),      // SupplierID
                    String.valueOf(recommendedSalesPrice)  // RecommendedSalesPrice
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
            int rowIndex = table.getRowIndex("ItemCode", id -> id.equals(ID));
            table.deleteRow(rowIndex);
            System.out.println("Item with Code " + ID + " has been deleted.");
            return true;
        } catch (ValueNotFound e) {
            System.out.println("Item with Code " + ID + " was not found in the file.");
            return false;
        }
    }

    @Override
    protected boolean update(String columnName, String oldValue, String newValue) {
        try {
            table.updateRow(table.getRowIndex(columnName, (x) -> x.equals(oldValue)), columnName, newValue);
            return true;
        } catch (ValueNotFound e) {
            System.out.println("ID not found.");
            return false;
        }
    }

    public String getID() {
        return ID;
    }

    @Override
    public String toString() {
        return String.format("%-5s | %-15s | %-20s | %,6d | %8.2f | %3d | %-10s | %8.2f",
                ID, itemName, itemDesc, itemQuantity, pricePerUnit, moq,
                supplier.getSupplierName(),
                recommendedSalesPrice) + "\n";
    }
}