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

public class Supplier extends InteractionsWithTable{

    private String supplierName;
    private String supplierContact;
    private double dueAmount;

    public Supplier(){
        try {
            table = new Table("src/files/supplier.csv");
        }
        catch(FileNotFoundException e){
            System.out.println("File not found");
        }
    }

    public static Supplier get(String value, Function<String,Boolean> filter){
        try {
            Table table = new Table("src/files/supplier.csv");
            List<String> row = table.getRow(value, filter);
            return new Supplier(row.get(0), row.get(1), row.get(2), Double.parseDouble(row.get(3)));
        } catch (FileNotFoundException e) {
            System.out.println("File name is incorrect");
            return null;
        } catch (ValueNotFound e) {
            System.out.println("Value not found!");
            return null;
        }
    }

    public static List<Supplier> getMultiple(String value, Function<String,Boolean> filter) {
        try {
            Table table = new Table("src/files/supplier.csv");
            List<List<String>> rows = table.getRows(value, filter);
            List<Supplier> suppliers = new ArrayList<>();
            for (List<String> row : rows) {
               suppliers.add(new Supplier(row.get(0), row.get(1), row.get(2), Double.parseDouble(row.get(3))));
            }
            return suppliers;
        } catch (FileNotFoundException e) {
            System.out.println("File name is incorrect");
            return null;
        } catch (ValueNotFound e) {
            throw new RuntimeException(e);
        }
    }

    public Supplier(String supplierName, String supplierContact){
        this.supplierName = supplierName;
        this.supplierContact = supplierContact;
        this.dueAmount = 0;
        try {
            table = new Table("src/files/supplier.csv");
            ID = generateID();
        }
        catch(FileNotFoundException e){
            System.out.println("File not found");
        }
    }

    public Supplier(String ID, String supplierName, String supplierContact, double dueAmount) {
        this.ID = ID;
        this.supplierName = supplierName;
        this.supplierContact = supplierContact;
        this.dueAmount = dueAmount;
        try {
            table = new Table("src/files/supplier.csv");
        }
        catch(FileNotFoundException e){
            System.out.println("File not found");
        }
    }

    static void displayAllSuppliers(Scanner scanner) {
        System.out.println("============== Current Suppliers =============");
        System.out.printf("%-10s | %-12s | %-15s%n",
                "ID", "Name", "Contact");
        System.out.println("=".repeat(40));
        List<Supplier> allSuppliers = getMultiple("supplierID", (x) -> !x.isEmpty());
        for (Supplier x : allSuppliers) {
            System.out.println(x);
        }
        System.out.println("============================================");
    }

    public String getsupplierID() {
        try {
            Table table = new Table("src/files/supplier.csv");
            List<String> row = table.getRow("supplierName", name -> name.equals(this.supplierName));
            if (row != null && !row.isEmpty()) {
                return row.get(0);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Supplier file not found.");
        } catch (ValueNotFound e) {
            System.out.println("Supplier ID not found.");
        }
        return null;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public boolean setSupplierName(String supplierName) {
        if(!supplierName.matches("[A-z]+"))
            return false;
        this.update("supplierName", this.supplierName, supplierName);
        this.supplierName = supplierName;
        return true;
    }

    public String getSupplierContact() {
        return supplierContact;
    }
    public double getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(double newAmount) {
        this.update("dueAmount", String.valueOf(dueAmount), String.valueOf(newAmount));
        this.dueAmount = newAmount;
    }
    public boolean setSupplierContact(String supplierContact) {
        if(!supplierContact.matches("[0-9]{10}"))
            return false;
        this.update("supplierContact", this.supplierContact, supplierContact);
        this.supplierContact = supplierContact;
        return true;
    }

    public boolean add() {
        try {
            String[] values = {ID, supplierName, supplierContact, String.valueOf(dueAmount)};
            table.addRow(values);
            return true;
        }
        catch(IncorrectNumberOfValues e) {
            System.out.println("Incorrect Input");
            return false;

        }
    }

    public boolean delete() {
        try {

            // Find the row index for the supplierID
            int rowIndex = table.getRowIndex("supplierID", id -> id.equals(ID));

            // Delete the row at the found index
            table.deleteRow(rowIndex);
            System.out.println("Supplier with ID " + ID + " has been deleted.");
            return true;

        } catch (ValueNotFound e) {
            System.out.println("Supplier with ID " + ID + " was not found in the file.");
            return false;
        }
    }

    @Override
    public String toString() {
        return String.format("%-10s | %-12s | %-15s",
                ID, supplierName, supplierContact);
    }

}