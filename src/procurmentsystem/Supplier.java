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
import java.util.regex.Pattern;

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
            return new Supplier(row.get(0), row.get(1), row.get(2));
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
               suppliers.add(new Supplier(row.get(0), row.get(1), row.get(2)));
            }
            return suppliers;
        } catch (FileNotFoundException e) {
            System.out.println("File name is incorrect");
            return null;
        }
    }

    public Supplier(String supplierName, String supplierContact){
        this.supplierName = supplierName;
        this.supplierContact = supplierContact;
        try {
            table = new Table("src/files/supplier.csv");
            ID = generateID();
        }
        catch(FileNotFoundException e){
            System.out.println("File not found");
        }
    }

    public Supplier(String ID, String supplierName, String supplierContact) {
        this.ID = ID;
        this.supplierName = supplierName;
        this.supplierContact = supplierContact;
        try {
            table = new Table("src/files/supplier.csv");
        }
        catch(FileNotFoundException e){
            System.out.println("File not found");
        }
    }

    static void displayAllSuppliers(Scanner scanner) {
        System.out.println("==============Current Suppliers=============");
        System.out.println("Supplier ID, Supplier Name, Supplier Contact");
        List<Supplier> allSuppliers =  getMultiple("supplierID", (x) -> !x.isEmpty());
        for (Supplier x : allSuppliers) {
            System.out.println(x);
        }
        System.out.println("============================================");

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

    public boolean setSupplierContact(String supplierContact) {
        if(!supplierContact.matches("[0-9]{10}"))
            return false;
        this.update("supplierContact", this.supplierContact, supplierContact);
        this.supplierContact = supplierContact;
        return true;
    }

    public boolean add() {
        try {
            String[] values = {ID, supplierName, supplierContact};
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
    protected boolean update(String columnName, String oldValue, String newValue) {
        try {
            table.updateRow(table.getRowIndex(columnName, (x) -> x.equals(oldValue)), columnName, newValue);
            return true;
        } catch(ValueNotFound e){
            System.out.println("ID not found.");
            return false;
        }
    }

    @Override
    public String toString() {
        int IDLen = ID.length();
        int nameLen = supplierName.length();
        int IDColLen = 11 - IDLen;
        int NameCollen = 13 - nameLen;
        String IDspaces = new String(new char[IDColLen / 2]).replace("\0", " ");
        String Namespaces = new String(new char[NameCollen / 2]).replace("\0", " ");


        return
                IDspaces + ID + IDspaces +
                "| "+ Namespaces + supplierName + Namespaces +
                "|  " + supplierContact;

    }

}