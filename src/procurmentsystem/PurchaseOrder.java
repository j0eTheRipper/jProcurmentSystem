package procurmentsystem;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import procurmentsystem.Table.*;

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



    @Override
    public String toString() {
        return "";
    }

    @Override
    protected boolean add() {
        return false;
    }

    @Override
    protected boolean delete() {
        return false;
    }
}