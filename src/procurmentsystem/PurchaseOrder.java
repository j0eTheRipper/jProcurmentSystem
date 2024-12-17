package procurmentsystem;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import procurmentsystem.Table.IncorrectNumberOfValues;
import procurmentsystem.Table.Status;
import procurmentsystem.Table.Table;
import procurmentsystem.Table.ValueNotFound;

public class PurchaseOrder extends Order {
    private Requisition requisition;
    public PurchaseOrder(String ID, List<Item> items, PurchaseManager placer, FinancialManager approvedBy, Status status, int totalPrice)  {
        this.ID = ID;
        this.placer = placer;
        this.approvedBy = approvedBy;
        this.status = status;
        this.totalPrice = totalPrice;
        this.items = items;
        try {
            this.table = new Table("src/files/purchaseOrders.csv");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public PurchaseOrder(String ID, Requisition requisition, PurchaseManager placer, FinancialManager approvedBy, Status status, int totalPrice)  {
        this.ID = ID;
        this.placer = placer;
        this.approvedBy = approvedBy;
        this.status = status;
        this.totalPrice = totalPrice;
        this.requisition = requisition;
        try {
            this.table = new Table("src/files/purchaseOrders.csv");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public PurchaseOrder(List<Item> items, PurchaseManager placer, FinancialManager approvedBy, Status status, int totalPrice) {
        this.placer = placer;
        this.approvedBy = approvedBy;
        this.status = status;
        this.totalPrice = totalPrice;
        this.items = items;
        try {
            this.table = new Table("src/files/purchaseOrders.csv");
            this.ID = generateID();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static PurchaseOrder get(String column, Function<String, Boolean> filter) {
        try {
            Table table = new Table("src/files/purchaseOrders.csv");
            List<String> PurchaseOrderData = table.getRow(column, filter);
            POData data = getPoData(PurchaseOrderData);
            return new PurchaseOrder(
                    PurchaseOrderData.get(0),
                    Requisition.get("requisID", (x) -> x.equals(PurchaseOrderData.get(1))),
                    data.purchaseManager(),
                    data.financialManager(),
                    data.status(),
                    Integer.parseInt(PurchaseOrderData.get(2))
            );
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            return null;
        } catch (ValueNotFound e) {
            return null;
        }
    }

    public static List<PurchaseOrder> getMultiple(String column, Function<String, Boolean> filter) {
        try {
            Table table = new Table("src/files/purchaseOrders.csv");
            List<List<String>> purchaseOrders= table.getRows(column, filter);
            List<PurchaseOrder> result = new ArrayList<>();
            for (List<String> po : purchaseOrders) {
                POData data = getPoData(po);
                result.add( new PurchaseOrder(
                        po.get(0),
                        Requisition.get("requisID", (x) -> x.equals(po.get(1))),
                        data.purchaseManager(),
                        data.financialManager(),
                        data.status(),
                        Integer.parseInt(po.get(2))
                ));
            }
            return result;
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            return null;
        } catch (ValueNotFound e) {
            return null;
        }
    }

    public PurchaseOrder() throws IOException{
        this.table = new Table("src/files/purchaseOrders.csv");
    }


    //
    public void readPOFile(){

    }

    //approve, reject, paid status
    public String statusApprove(){
        return this.status = "Approved";
    }
    public String statusReject(){
        return this.status = "Rejected";
    }

    //getter
    public String getPOID(){
        return POID;
    }
    public String getStatus(){
        return status;
    }
    public double getPrice(){
        return price;
    }

    //setter
    public void setPOID(String POID) throws IncorrectNumberOfValues{
        if (POID == null || POID.isBlank()){
            System.out.println("Please set a purchase order ID.");
        }
        else{
            String[] newPOID = {POID};
            table.addRow(newPOID);
        }
    }

    public void setPrice(double price) throws IncorrectNumberOfValues{
        if (price <= 0.0){
            System.out.println("Please set a price for more than a 0.0");
        }
        else{

        }
    }
    private static POData getPoData(List<String> PurchaseOrderData) {
        PurchaseManager purchaseManager = (PurchaseManager) User.get("id", (x) -> x.equals(PurchaseOrderData.get(4)));
        FinancialManager financialManager = (FinancialManager) User.get("id", (x) -> x.equals(PurchaseOrderData.get(5)));
        Status status = switch (PurchaseOrderData.get(3)) {
            case "Pending" -> Status.PENDING;
            case "Paid" -> Status.PAID;
            case "Approved" -> Status.APPROVED;
            case "Rejected" -> Status.REJECTED;
            default -> null;
        };
        return new POData(purchaseManager, financialManager, status);
    }

    private record POData(PurchaseManager purchaseManager, FinancialManager financialManager, Status status) {
    }

    public void setItemID(String itemID){
        if (Item.itemCode != null){
            String[] newItemCode = {itemID};
            table.updateRow(rowIndex, "ItemID", newItemCode);
        }
        else{
            System.out.println("Item does not exist.");
        }
    }


    //methods
    //generate purchase order with status pending as default
    public void generatePurchaseOrder(String POID, String requisID, String itemID, double price, String status){

    }

    //change status of the purchase order
    public void updatePOStatus(Scanner sc, String POID, String status){

    }

    //view one purchase order
    public void viewPurchaseOrder(){
        System.out.println("Purchase Order ID : "+ POID);
        System.out.println("Requisition ID : "+ requisID);
        System.out.println("Item ID : "+ itemID);
        System.out.println("Price : "+ price);
        System.out.println("Status of approval : "+ status);
    }

    //view list of purchase orders by ID
    public void viewAllPO(){
        System.out.println("----------Purchase Orders by ID----------");

    }
}