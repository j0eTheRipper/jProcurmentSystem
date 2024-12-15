package procurmentsystem;

public class PurchaseOrder{
    //attribute
    private requisition requisition;
    private Item item;
    private String POID;
    private String status;
    private double price;

    //constructor
    public PurchaseOrder(String POID, requisition requisition, Item item, String status, double price){
        this.POID = POID;
        this.requisition = requisition;
        this.item = item;
        this.status = "Pending";//defaulted to pending, waiting approve/reject
        this.price = price;

    }

    public PurchaseOrder(requisition requisition){
        this.requisition = requisition;
    }

    //approve or reject status
    public void statusApprove(){
        this.status = "Approved";
    }
    public void statusReject(){
        this.status = "Rejected";
    }

    //getter and setter
    public String getPOID(){
        return POID;
    }
    public String getStatus(){
        return status;
    }
    public double getPrice(){
        return price;
    }
    public void setPOID(String POID){
        this.POID = POID;
    }
    public void setPrice(double price){
        this.price = price;
    }
}