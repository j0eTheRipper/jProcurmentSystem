package procurmentsystem;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.io.File;
import java.util.function.Function;

public class Supplier {
    private String supplierID;
    private String supplierName;
    private String supplierContact;
     Table table;

    public Supplier(){
        try {
            table = new Table("src/files/supplier.csv");
        }
        catch(FileNotFoundException e){
            System.out.println("File not found");
        }
    }
    public Supplier(String supplierID, String supplierName, String supplierContact, String fileName) {
        this.supplierID = supplierID;
        this.supplierName = supplierName;
        this.supplierContact = supplierContact;
        try {
            table = new Table("src/files/supplier.csv");
        }
        catch(FileNotFoundException e){
            System.out.println("File not found");
        }
    }
    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierContact() {
        return supplierContact;
    }

    public void setSupplierContact(String supplierContact) {
        this.supplierContact = supplierContact;
    }

    public void writeToCSV() {
        try {
            String[] values = {supplierID, supplierName, supplierContact};
            table.addRow(values);
        }
        catch(IncorrectNumberOfValues e) {
            System.out.println("Incorrect Input");

        }
    }

    public static void deleteFromCSV(String fileName, String supplierID) {
        try {
            Table table = new Table(fileName);

            // Find the row index for the supplierID
            int rowIndex = table.getRowIndex("supplierID", id -> id.equals(supplierID));

            // Delete the row at the found index
            table.deleteRow(rowIndex);
            System.out.println("Supplier with ID " + supplierID + " has been deleted.");
        } catch (FileNotFoundException e) {
            System.out.println("The file " + fileName + " was not found.");
        } catch (ValueNotFound e) {
            System.out.println("Supplier with ID " + supplierID + " was not found in the file.");
        }
    }
    @Override
    public String toString() {
        return "Supplier{" +
                "supplierID='" + supplierID + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", supplierContact='" + supplierContact + '\'' +
                '}';
    }
}
