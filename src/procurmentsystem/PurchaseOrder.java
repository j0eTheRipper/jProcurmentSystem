package procurmentsystem;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import procurmentsystem.Table.*;

public class PurchaseOrder extends Order{
    //attribute
    private requisition requisition;

    //file constructor
    public PurchaseOrder(){
        try {
            this.table = new Table("src/files/purchaseOrders.csv");
        } catch (FileNotFoundException e) {
        System.out.println("Error: File not found.");
        }
    }

    //constructor
    public PurchaseOrder(String POID, requisition requisition){
        this.ID = POID;
        this.requisition = requisition;
        try {
            this.table = new Table("src/files/purchaseOrders.csv");
            this.ID = generateID();
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
        }
    }

    //getter
    public static PurchaseOrder get(String value, Function<String, Boolean> filter){
        try {
            Table table = new Table("src/files/purchaseOrders.csv");
            List<String> row = table.getRow(value, filter);

            if(row == null || row.size() < 2){
                System.out.println("No purchase Order found with matching criteria.");
                return null;
            }

            String POID = row.get(0);
            String requisID = row.get(1);

            requisition requis = requisition.get("ReqID", id -> id.equals(requisID));

            if (requis == null){
                System.out.println("Requisition not found with the given Purchase Order ID.");
                return null;
            }

            return new PurchaseOrder(POID, requis);
        } catch (FileNotFoundException e) {
            System.out.println("Error: File name is incorrect.");
            return null;
        }catch (ValueNotFound e){
            System.out.println("Error: Value not found.");
            return null;
        }
    }

    public static List<requisition> getRequisitions(String value, Function<String, Boolean> filter, boolean returnList){
        try {
            Table table = new Table("src/files/purchaseOrders.csv");
            List<List<String>> rows = table.getRows(value, filter);

            if (rows == null || rows.isEmpty()){
                System.out.println("No requisition found with matching filter.");
                return null;
            }

            List<requisition> requisitions = new ArrayList<>();
            for ( List<String> row : rows){
                String requisID = row.get(1);
                requisition req = requisition.get("ReqID", id -> id.equals(requisID));
                if(req != null){
                    requisition.add(req);
                }
            }
            return requisitions;
        } catch (FileNotFoundException e) {
            System.out.println("Error: File name is incorrect");
            return null;
        }catch (ValueNotFound e){
            System.out.println("Error: Value not found.");
            return null;
        }
    }

    public String getPOID(){
        return ID;
    }
    public requisition getRequisition(){
        return requisition;
    }

    //setters
    public boolean setPOID(String POID){
        if(POID == null || !POID.matches("PO[0-9]+")){
            return false;
        }
        this.update("POID", this.ID, POID);
        this.ID = POID;
        return true;
    }

    public boolean setRequisition(requisition requisition){
        if(requisition == null){
            return false;
        }
        this.update("ReqID", this.requisition.getRequisID(), requisition.getRequisID());
        this.requisition = requisition;
        return true;
    }

    //add
    @Override
    public boolean add(){
        try {
            String[] values = {
                ID, //Purchase Order ID
                requisition.getRequisID() //requisition ID
            };
            table.addRow(values);
            return true;
            
        } catch (IncorrectNumberOfValues e) {
            System.out.println("Incorrect input: " + e.getMessage());
            return false;
        }
    }
    //delete 
    @Override
    public boolean delete(){
        try {
            int rowIndex = table.getRowIndex("POID", id -> id.equals(ID));
            table.deleteRow(rowIndex);
            System.out.println("The Purchase Order ID : "+ ID + "has been successfully deleted.");
            return true;
        } catch (ValueNotFound e) {
            System.out.println("The Purchase Order ID : " + ID + "is not found.");
            return false;
        }
    }

    @Override
    protected boolean update(String columnName, String oldValue, String newValue){
        try {
            table.updateRow(table.getRowIndex(columnName, (x) -> x.equals(oldValue)), columnName, newValue);
            return true;
        } catch (ValueNotFound e) {
            System.out.println("Error: Purchase Order ID not found.");
            return false;
        }
    }

    @Override
    public String toString(){
        return String.format("%-15s | %-10s", ID, requisition.getRequisID());
    }
}