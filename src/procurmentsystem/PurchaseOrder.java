package procurmentsystem;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import procurmentsystem.Table.*;

public class PurchaseOrder extends Order{
    private requisition requisition;
    private Table bridge;
    private Supplier supplier;

    public PurchaseOrder(String ID, HashMap<Item, Integer> items, PurchaseManager placer, FinancialManager approvedBy, Status status)  {
        this.ID = ID;
        this.placer = placer;
        this.approvedBy = approvedBy;
        this.status = status;
        this.totalPrice = 0;
        this.items = items;

        items.forEach((item, quantity) -> {
            totalPrice += item.getPricePerUnit() * quantity;
        });
        supplier = items.keySet().iterator().next().getSupplier();

        try {
            this.table = new Table("src/files/purchaseOrders.csv");
            this.bridge = new Table("src/files/PurchasedItems.csv");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public PurchaseOrder(String ID, requisition requisition, PurchaseManager placer, FinancialManager approvedBy, Status status, int totalPrice)  {
        this.ID = ID;
        this.placer = placer;
        this.approvedBy = approvedBy;
        this.status = status;
        this.requisition = requisition;
        this.totalPrice = totalPrice;
        try {
            this.table = new Table("src/files/purchaseOrders.csv");
            this.bridge = new Table("src/files/PurchasedItems.csv");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public PurchaseOrder(HashMap<Item, Integer> items, PurchaseManager placer, FinancialManager approvedBy, Status status) {
        this.placer = placer;
        this.approvedBy = approvedBy;
        this.status = status;
        this.items = items;
        this.totalPrice = 0;
        items.forEach((item, quantity) -> {
           totalPrice += item.getPricePerUnit() * quantity;
        });
        supplier = items.keySet().iterator().next().getSupplier();


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

            HashMap<Item, Integer> items = getItemsFromBridge(PurchaseOrderData);

            return new PurchaseOrder(
                    PurchaseOrderData.get(0),
                    items,
                    data.purchaseManager(),
                    data.financialManager(),
                    data.status()
            );
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            return null;
        } catch (ValueNotFound e) {
            return null;
        }
    }

    private static HashMap<Item, Integer> getItemsFromBridge(List<String> PurchaseOrderData) throws FileNotFoundException, ValueNotFound {
        Table bridge = new Table("src/files/PurchasedItems.csv");
        List<List<String>> allItemsForPurchase = bridge.getRows("POID", (x) -> x.equals(PurchaseOrderData.get(0)));
        HashMap<Item, Integer> items = new HashMap<>();
        for (List<String> row : allItemsForPurchase) {
            Item tempItem = Item.get("ItemCode", x -> x.equals(row.get(1)));
            int quantity = Integer.parseInt(row.get(2));
            items.put(tempItem, quantity);
        }
        return items;
    }

    public static List<PurchaseOrder> getMultiple(String column, Function<String, Boolean> filter) {
        try {
            Table table = new Table("src/files/purchaseOrders.csv");
            List<List<String>> purchaseOrders= table.getRows(column, filter);
            List<PurchaseOrder> result = new ArrayList<>();
            for (List<String> po : purchaseOrders) {
                POData data = getPoData(po);
                HashMap<Item, Integer> itemsFromBridge = getItemsFromBridge(po);
                result.add(new PurchaseOrder(
                        po.get(0),
                        itemsFromBridge,
                        data.purchaseManager(),
                        data.financialManager(),
                        data.status()
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

    private static POData getPoData(List<String> PurchaseOrderData) {
        PurchaseManager purchaseManager = (PurchaseManager) User.get("id", (x) -> x.equals(PurchaseOrderData.get(5)));
        FinancialManager financialManager = (FinancialManager) User.get("id", (x) -> x.equals(PurchaseOrderData.get(4)));
        Status status = switch (PurchaseOrderData.get(3)) {
            case "PENDING" -> Status.PENDING;
            case "PAID" -> Status.PAID;
            case "APPROVED" -> Status.APPROVED;
            case "REJECTED" -> Status.REJECTED;
            default -> null;
        };
        return new POData(purchaseManager, financialManager, status);
    }

    private record POData(PurchaseManager purchaseManager, FinancialManager financialManager, Status status) {
    }
    public String getID(){
        return ID;
    }
    public requisition getRequisition(){
        return requisition;
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
            String requisID, approverID;
            if (requisition == null) {
                 requisID = "";
            } else
                requisID = requisition.getRequisID();
            if (approvedBy == null) {
                approverID = "";
            } else
                approverID = approvedBy.getID();

            if (items != null) {
                items.forEach((item, quantity) -> {
                    try {
                        bridge.addRow(new String[]{ID, item.getID(), String.valueOf(quantity)});
                    } catch (IncorrectNumberOfValues e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            String[] values = {
                    ID,
                    requisID,
                    String.valueOf(totalPrice),
                    String.valueOf(status),
                    approverID,
                    placer.getID(),
                    supplier.getsupplierID()
            };
            table.addRow(values);
            return true;
        } catch (IncorrectNumberOfValues e) {
            System.out.println("Incorrect input: " + e.getMessage());
            return false;
        }
    }

    public void setStatus(Status newStatus) {
        if (newStatus == status) return;

        if (newStatus == Status.PAID) {
            if (status == Status.APPROVED)
                supplier.setDueAmount(supplier.getDueAmount() - this.totalPrice);
        } else if (newStatus == Status.APPROVED) {
            supplier.setDueAmount(supplier.getDueAmount() + this.totalPrice);
        }
        update("Status", "", String.valueOf(newStatus));
        status = newStatus;
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
    public String toString() {
        String approvedByName = "No one yet...";
        if (approvedBy != null) {
            approvedByName = approvedBy.firstName + " " + approvedBy.lastName;
        }
        List<String> itemDisplays = new ArrayList<>();
        items.forEach((item, quantity) -> {
            itemDisplays.add(String.format("%s x%d", item.getItemName(), quantity));
        });

        String itemList = String.join(", ", itemDisplays);
        if (itemList.length() > 30) {
            itemList = itemList.substring(0, 27) + "...";
        }

        return String.format("%-10s | %-30s | $%9.2f | %-10s | %-15s | %-15s | %-15s",
            ID,
            itemList,
            totalPrice,
            status,
            approvedByName,
            placer.firstName + " " + placer.lastName,
            supplier.getSupplierName()
        );
    }
}