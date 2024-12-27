/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package procurmentsystem;


import procurmentsystem.Table.Status;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import procurmentsystem.Table.Status;
import procurmentsystem.Table.Table;
import procurmentsystem.Table.ValueNotFound;

public class ProcurmentSystem {
    public static void main(String[] args) throws IOException, ValueNotFound {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {

            // menu methods here
        }
        scanner.close();
    }

    private static void fm() throws FileNotFoundException, ValueNotFound {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n==== Financial Manager ====");
        System.out.println("1. Purchase Order Management");
        System.out.println("2. Stock status");
        System.out.println("3. Supplier Payments");
        System.out.println("4. exit");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        FinancialManager fm = (FinancialManager) User.get("email", x -> x.equals("youssef@gmail.com"));
        switch (choice) {
            case 1:
                System.out.println("==== Purchase Orders ====");
                List<PurchaseOrder> purchaseOrders = PurchaseOrder.getMultiple("POID", (x) -> !x.isBlank());
                String header = String.format("%-10s | %-30s | %-10s | %-10s | %-15s | %-15s",
                        "POID",
                        "Item xQTY",
                        "Total $$$",
                        "Status",
                        "Approved By",
                        "Placed By");
                System.out.println(header);
                for (PurchaseOrder po : purchaseOrders) {
                    System.out.println(po);
                }
                System.out.println("===========================================");
                System.out.print("Choose a purchase order to process: ");
                choice = scanner.nextInt();
                if (choice == 0) break;
                PurchaseOrder purchaseOrder = purchaseOrders.get(choice - 1);
                System.out.print("1. Approve\n2. Reject\n3. Pay\nChoose an option: ");
                choice = scanner.nextInt();
                if (choice == 1) purchaseOrder.setStatus(Status.APPROVED, fm);
                else if (choice == 2) purchaseOrder.setStatus(Status.REJECTED, fm);
                else if (choice == 3) purchaseOrder.setStatus(Status.PAID, fm);
                break;
            case 2:
                System.out.println("==== Purchase Orders ====");
                List<PurchaseOrder> purchaseOrders1 = PurchaseOrder.getMultiple("Status", (x) -> x.equals("STOCKED"));
                System.out.println(String.format("%-10s | %-30s | %-10s | %-10s | %-15s | %-15s | %-15s",
                        "POID",
                        "Item xQTY",
                        "Total $$$",
                        "Status",
                        "Approved By",
                        "Placed By",
                        "Supplier"));
                for (PurchaseOrder po : purchaseOrders1) {
                    System.out.println(po);
                }
                System.out.println("===========================================");
                System.out.println("Choose the purchase order for which you want to see the stock: ");
                choice = scanner.nextInt();
                if (choice == 0) break;
                PurchaseOrder po = purchaseOrders1.get(choice - 1);
                List<Item> items = po.getItems();
                System.out.println("Code | Item Name        | Description         | Quantity | Price/Unit | MOQ | Supplier   | Sales Price");
                for (Item item : items)
                    System.out.print(item);
                break;
            case 3:
                List<Supplier> suppliers = Supplier.getMultiple("supplierID", x -> true);
                System.out.printf("%-10s | %-12s | %-15s%n",
                        "ID", "Name", "Contact");
                for (Supplier supplier : suppliers)
                    System.out.println(supplier);
                System.out.print("Enter the supplier to view payment history: ");
                choice = scanner.nextInt();
                if (choice == 0) break;
                int finalChoice = choice;
                Supplier supplier = Supplier.get("supplierID", x -> x.equals(String.valueOf(finalChoice)));
                Table table = new Table("src/files/PaymentHistory.csv");
                List<List<String>> history = table.getRows("SupplierID", x -> x.equals(supplier.getsupplierID()));
                for (List<String> rec : history) {
                    System.out.println(rec);
                }

        }
    }

    private static void supplyManager() throws IOException, ValueNotFound {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n==== Inventory Manager Menu ====");
        System.out.println("1. Supplier Management");
        System.out.println("2. Item Management");
        System.out.println("3. Sales Management");
        System.out.println("4. Exit");
        System.out.print("Choose an option: ");
        int mainChoice = scanner.nextInt();
        scanner.nextLine();

        switch (mainChoice) {
            case 1:
                supplierMenu(scanner);
                break;

            case 2:
                itemMenu(scanner);
                break;

            case 3:
                salesMenu(scanner);
                break;

            case 4:
                System.out.println("Exiting the system. Goodbye!");
                break;

            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private static void supplierMenu(Scanner scanner) throws IOException {
        boolean backToMain = false;

        while (!backToMain) {
            System.out.println("\n==== Supplier Management ====");
            System.out.println("1. Add a Supplier");
            System.out.println("2. Delete a Supplier");
            System.out.println("3. View all Suppliers");
            System.out.println("4. Update a Supplier");
            System.out.println("5. Back to Main Menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: // Add a Supplier
                    System.out.print("Enter Supplier Name: ");
                    String supplierName = scanner.nextLine();
                    System.out.print("Enter Supplier Contact: ");
                    String supplierContact = scanner.nextLine();

                    Supplier supplier = new Supplier(supplierName, supplierContact);
                    supplier.add();
                    System.out.println("Supplier added successfully!");
                    break;

                case 2: // Delete a Supplier
                    System.out.print("Enter Supplier ID to delete: ");
                    String deleteID = scanner.nextLine();
                    Supplier supplier1 = Supplier.get("supplierID", (x) -> x.equals(deleteID));
                    if (supplier1 != null) {
                        supplier1.delete();
                    }
                    break;

                case 3: // View All Suppliers
                    Supplier.displayAllSuppliers(scanner);
                    System.out.println("Press enter key to continue...");
                    scanner.nextLine();
                    break;

                case 4: // Update a Supplier
                    Supplier.displayAllSuppliers(scanner);
                    System.out.println("Enter ID of Supplier you want to update: ");
                    String updateID = scanner.nextLine();
                    Supplier UpdateSupplier = Supplier.get("supplierID", (x) -> x.equals(updateID));
                    if (UpdateSupplier == null) {
                        break;
                    }
                    System.out.println("1:Change name \n2:Change contact number \nChoose the column you wish to edit: ");
                    int supplierChoice = scanner.nextInt();
                    scanner.nextLine();

                    if (supplierChoice == 1) {
                        System.out.println("Enter the new name: ");
                        String name = scanner.next();
                        boolean updatedSuccessfully = UpdateSupplier.setSupplierName(name);

                        if (updatedSuccessfully) {
                            System.out.println("Updated successfully");
                            System.out.println(UpdateSupplier);
                        } else
                            System.out.println("Please enter a valid name");
                    } else if (supplierChoice == 2) {
                        System.out.println("Enter the new contact number: ");
                        String number = scanner.next();
                        boolean updatedSuccessfully = UpdateSupplier.setSupplierContact(number);
                        if (updatedSuccessfully) {
                            System.out.println("Updated successfully");
                            System.out.println(UpdateSupplier);
                        } else
                            System.out.println("Please enter a valid number");
                    } else {
                        System.out.println("Invalid input");
                    }
                    break;

                case 5: // Back to Main Menu
                    backToMain = true;
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void itemMenu(Scanner scanner) throws IOException, ValueNotFound {
        boolean backToMain = false;

        while (!backToMain) {
            System.out.println("\n==== Item Management ====");
            System.out.println("1. Add an Item");
            System.out.println("2. Delete an Item");
            System.out.println("3. View all Items");
            System.out.println("4. Update an Item");
            System.out.println("5. Stock new shipment");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: // Add an Item
                    System.out.print("Enter Item Name: ");
                    String itemName = scanner.nextLine();
                    System.out.print("Enter Item Description: ");
                    String itemDesc = scanner.nextLine();
                    System.out.print("Enter Price Per Unit: ");
                    double pricePerUnit = scanner.nextDouble();
                    System.out.print("Enter Minimum Order Quantity (MOQ): ");
                    int moq = scanner.nextInt();
                    scanner.nextLine();
                    Supplier.displayAllSuppliers(scanner);
                    System.out.print("Enter Supplier ID: ");
                    String supplierID = scanner.nextLine();
                    Supplier selectedSupplier = Supplier.get("supplierID", (x) -> x.equals(supplierID));
                    System.out.print("Enter Initial Item Quantity: ");
                    int itemQuantity = scanner.nextInt();
                    scanner.nextLine();

                    if (selectedSupplier == null) {
                        System.out.println("Invalid Supplier ID!");
                        break;
                    }

                    System.out.print("Enter Recommended Sales Price: ");
                    double recommendedSalesPrice = scanner.nextDouble();

                    Item item = new Item(itemName, itemDesc, pricePerUnit, moq, selectedSupplier, recommendedSalesPrice, itemQuantity);
                    item.add();
                    System.out.println("Item added successfully!");
                    break;

                case 2: // Delete an Item
                    System.out.print("Enter Item ID to delete: ");
                    String deleteItemID = scanner.nextLine();
                    Item itemToDelete = Item.get("itemID", (x) -> x.equals(deleteItemID));
                    if (itemToDelete != null) {
                        itemToDelete.delete();
                    }
                    break;

                case 3: // View All Items
                    Item.displayAllItems(scanner);
                    System.out.println("Press enter key to continue...");
                    scanner.nextLine();
                    break;

                case 4: // Update an Item
                    Item.displayAllItems(scanner);
                    System.out.println("Enter Item Code of Item you want to update: ");
                    String updateItemCode = scanner.nextLine();
                    Item updateItem = Item.get("ItemCode", (x) -> x.equals(updateItemCode));
                    if (updateItem == null) {
                        break;
                    }
                    System.out.println("Choose the column you wish to edit: ");
                    System.out.println("1: Change Name");
                    System.out.println("2: Change Description");
                    System.out.println("3: Change Price Per Unit");
                    System.out.println("4: Change MOQ");
                    System.out.println("5: Change Supplier");
                    System.out.println("6: Change Recommended Sales Price");
                    int itemChoice = scanner.nextInt();
                    scanner.nextLine();

                    boolean updated = false;
                    switch (itemChoice) {
                        case 1:
                            System.out.println("Enter the new name: ");
                            String newName = scanner.nextLine();
                            updated = updateItem.setItemName(newName);
                            break;
                        case 2:
                            System.out.println("Enter the new description: ");
                            String newDesc = scanner.nextLine();
                            updated = updateItem.setItemDesc(newDesc);
                            break;
                        case 3:
                            System.out.println("Enter the new price per unit: ");
                            double newPrice = scanner.nextDouble();
                            updated = updateItem.setPricePerUnit(newPrice);
                            break;
                        case 4:
                            System.out.println("Enter the new MOQ: ");
                            int newMOQ = scanner.nextInt();
                            scanner.nextLine();
                            updated = updateItem.setMoq(newMOQ);
                            break;
                        case 5:
                            Supplier.displayAllSuppliers(scanner);
                            System.out.println("Enter the new supplier ID: ");
                            String newSupplierID = scanner.nextLine();
                            Supplier newSupplier = Supplier.get("supplierID", (x) -> x.equals(newSupplierID));
                            if (newSupplier != null) {
                                updated = updateItem.setSupplier(newSupplier);
                            } else {
                                System.out.println("Invalid Supplier ID!");
                            }
                            break;
                        case 6:
                            System.out.println("Enter the new recommended sales price: ");
                            double newRecommendedPrice = scanner.nextDouble();
                            updated = updateItem.setRecommendedSalesPrice(newRecommendedPrice);
                            break;
                        default:
                            System.out.println("Invalid input");
                    }

                    if (updated) {
                        System.out.println("Item updated successfully");
                        System.out.println(updateItem);
                    } else {
                        System.out.println("Update failed. Please check your input.");
                    }
                    break;


                case 5: // Stock shipment delivery and add to inventory
                    List<PurchaseOrder> PaidPO = PurchaseOrder.getMultiple("Status",(x)-> x.matches("PAID"));
                    String header = String.format("%-10s | %-30s | %-10s | %-10s | %-15s | %-15s",
                            "POID",
                            "Item xQTY",
                            "Total $$$",
                            "Status",
                            "Approved By",
                            "Placed By");
                    System.out.println(header);
                    for (PurchaseOrder po : PaidPO) {
                        System.out.println(po);
                    }
                    System.out.println("Select Shipment to Stock: ");
                    String POidToStock = scanner.nextLine();
                    PurchaseOrder POToStock = PurchaseOrder.get("POID", (x)->x.equals(POidToStock));
                    POToStock.setStatus(Status.STOCKED);

                    //Item.updateStockOfStocked(POidToStock);
                    System.out.println("Inventory updated successfully!");




                case 6: // Back to Main Menu
                    backToMain = true;
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    private static void promptToContinue(Scanner scanner) {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    private static void salesMenu(Scanner scanner) {
        boolean exitMenu = false;

        while (!exitMenu) {
            System.out.println("\n=== Sales Manager Menu ===");
            System.out.println("1. List of Items (View)");
            System.out.println("2. Add Sale");
            System.out.println("3. Edit Sale");
            System.out.println("4. Delete Sale");
            System.out.println("5. Sales Report");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1 -> {
                    listItems(scanner);
                    promptToContinue(scanner); // Ask user to continue after viewing items
                }
                case 2 -> {
                    addSale(scanner);
                    promptToContinue(scanner);
                }
                case 3 -> {
                    editSale(scanner);
                    promptToContinue(scanner);
                }
                case 4 -> {
                    deleteSale(scanner);
                    promptToContinue(scanner);
                }
                case 5 -> {
                    System.out.println("\n=== Sales Report ===");
                    Sale sale = new Sale();
                    sale.salesReport();
                    promptToContinue(scanner); // Ask user to continue after viewing the report
                }
                case 6 -> {
                    System.out.println("Exiting Sales Manager Menu...");
                    exitMenu = true;
                }
                default -> {
                    System.out.println("Invalid choice. Please enter a number between 1 and 6.");
                    promptToContinue(scanner); // Ask user to continue after invalid input
                }
            }
        }
    }



    // 1. List of Items (View)
    private static void listItems(Scanner scanner) {
        Item.displayAllItems(scanner);
        System.out.println("Press enter key to continue...");
        scanner.nextLine();
    }

    //
    private static void addSale(Scanner scanner) {
        try {
            System.out.print("Enter Item Code: ");
            String itemCode = scanner.nextLine();

            System.out.print("Enter Quantity Sold: ");
            int quantity = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            // Create and add sale
            Sale sale = new Sale(itemCode, quantity);
            if (!sale.add()) {
                System.out.println("Failed to add sale. Please check the inputs and try again.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void editSale(Scanner scanner) {
        try {
            System.out.print("Enter Sale ID to Edit: ");
            String saleID = scanner.nextLine();

            System.out.print("Enter New Quantity Sold: ");
            int newQuantity = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            // Create sale instance and set saleID
            Sale sale = new Sale();
            sale.saleID = saleID; // Set the saleID for the instance

            // Call update with the correct saleID
            boolean success = sale.update("qty", "", String.valueOf(newQuantity));
            if (!success) {
                System.out.println("Failed to update the sale. Please check the Sale ID and try again.");
            }
        } catch (Exception e) {
            System.out.println("Error editing sale: " + e.getMessage());
        }
    }

    private static void deleteSale(Scanner scanner) {
        try {
            System.out.print("Enter Sale ID to Delete: ");
            String saleID = scanner.nextLine();

            // Create sale instance and set saleID
            Sale sale = new Sale();
            sale.saleID = saleID; // Set the saleID for the instance

            // Call delete
            boolean success = sale.delete();
            if (!success) {
                System.out.println("Failed to delete the sale. Please check the Sale ID and try again.");
            }
        } catch (Exception e) {
            System.out.println("Error deleting sale: " + e.getMessage());
        }
    }
private static void purchaseManager(Scanner sc){
            boolean exit = false;
            System.out.println("==== Purchase Manager Menu ====");
            System.out.println("1. Purchase Order Menu.");
            System.out.println("2. Generate a new requisition.");
            System.out.println("3. View all items.");
            System.out.println("4. View all suppliers.");
            System.out.println("5. View all requisitions.");
            System.out.println("6. Exit.");
            

            int choices = sc.nextInt();
            sc.nextLine();

            switch(choices){
                case 1:
                    purchaseManagerMenu(sc);
                    break;
                case 2:
                    //requester
                    System.out.println("Enter requester name: ");
                    String requesterName = sc.nextLine();
                    //po id
                    System.out.println("Enter purchase order ID: ");
                    String purchaseOrderID = sc.nextLine();
                    //status
                    System.out.println("Enter status (Pending/Approved/Rejected): " );
                    String status = sc.nextLine();

                    //create requisition
                    requisition newRequisition = new requisition(requesterName, purchaseOrderID, status);

                    //add items
                    boolean addItems = true;
                    while(addItems){
                        System.out.println("Enter item code: ");
                        String itemCode = sc.nextLine();
                        System.out.println("Enter quantity of items: ");
                        int quantity = sc.nextInt();
                        sc.nextLine();

                        newRequisition.addItem(itemCode, quantity);

                        System.out.println("Do you want to add more? (y/n): ");
                        String response = sc.nextLine();
                        addItems = response.equalsIgnoreCase("y");
                        
                    }
                    //save
                    if(newRequisition.add()){
                        System.out.println("Requisition created successfully.");
                    }else{
                        System.out.println("Failed to create requisition.");
                    }
                    System.out.println("Press enter key to return to the menu...");
                    sc.nextLine();
                    purchaseManager(sc);
                case 3:
                    Item.displayAllItems(sc);
                    System.out.println("Press enter key to continue...");
                    sc.nextLine();
                    System.out.println("Press enter key to return to the menu...");
                    sc.nextLine();
                    purchaseManager(sc);
                case 4:
                    Supplier.displayAllSuppliers(sc);
                    System.out.println("Press enter key to continue...");
                    sc.nextLine();
                    System.out.println("Press enter key to return to the menu...");
                    sc.nextLine();
                    purchaseManager(sc);

                case 5:
                    List<requisition> allReq = requisition.getMultiple("requisID", x-> true);
                    if(allReq != null || !allReq.isEmpty()){
                        for(requisition reqs : allReq){
                            System.out.println(reqs);
                        }
                    }else{
                        System.out.println("No requisition is found.");
                    }
                    System.out.println("Press enter key to return to the menu...");
                    sc.nextLine();
                    purchaseManager(sc);;
                case 6:
                    System.out.println("Exiting...");
                    break;

            }

        }

        private static void purchaseManagerMenu(Scanner sc){
            boolean exit = false;
            System.out.println("==== Purchase Order Menu ====");
            System.out.println("1. Create one.");
            System.out.println("2. View all.");
            System.out.println("3. Update a purchase order.");
            System.out.println("4. Delete a purchase order.");
            System.out.println("5. Back to main menu.");

            int choices = sc.nextInt();
            sc.nextLine();

            switch(choices){
                case 1:
                    //placer input
                    System.out.println("Enter Purchase Manager ID: ");
                    String placerID = sc.nextLine();
                    PurchaseManager placer = (PurchaseManager)User.get("id", x -> x.equals(placerID));

                    if (placer == null){
                        System.out.println("Purchase manager not found.");
                        break;
                    }
                    //approver input
                    System.out.println("enter Financial Manager ID: ");
                    String approvalID = sc.nextLine();
                    FinancialManager approvedby = (FinancialManager)User.get("id", x->x.equals(approvalID));

                    if(approvedby == null){
                        System.out.println("Financial manager not found.");
                        break;
                    }

                    //item and quantity input
                    HashMap<Item, Integer> items = new HashMap<>();
                    boolean addItems = true;
                    while(addItems){
                        System.out.println("Enter item code: ");
                        String itemCode = sc.nextLine();
                        Item item = Item.get("ItemCode", x->x.equals(itemCode));

                        if(item == null){
                            System.out.println("Item not found.");
                        }else{
                            System.out.println("Enter quantity for" +itemCode+ ":");
                            int quantity = sc.nextInt();
                            sc.nextLine();
                            items.put(item, quantity);
                        }

                        System.out.println("Would you like to add another item? (y/n)");
                        String response = sc.nextLine();
                        addItems = response.equalsIgnoreCase("y");
                    }
                    //calculate price and select status
                    int totalPrice = 0;
                    for(Map.Entry<Item, Integer> entry : items.entrySet()){
                        totalPrice += entry.getKey().getPricePerUnit()*entry.getValue();
                    }
                    //status
                    System.out.println("Enter the status of purchase order (PENDING, APPROVED, PAID, REJECTED): ");
                    String statusinput = sc.nextLine();
                    Status status = Status.valueOf(statusinput.toUpperCase());

                    //generate purchase order
                    PurchaseOrder purchaseOrder = new PurchaseOrder(items, placer, approvedby, status);
                    boolean createOrder = purchaseOrder.add();

                    if (createOrder){
                        System.out.println("Purchase Order created successfully.");
                    }else{
                        System.out.println("Failed to create Purchase Order.");
                    }
                    System.out.println("Press enter key to return to the menu...");
                    sc.nextLine();
                    purchaseManagerMenu(sc);

                case 2:
                    List<PurchaseOrder> allOrders = PurchaseOrder.getMultiple("POID", x -> true);
                    if(allOrders != null || !allOrders.isEmpty()){
                        for(PurchaseOrder orders : allOrders){
                            System.out.println(orders);
                        }
                    }else{
                        System.out.println("No purchase orders found.");
                    }
                    System.out.println("Press enter key to return to the menu...");
                    sc.nextLine();
                    purchaseManagerMenu(sc);
                case 3:
                    purchaseOrderUpdateMenu(sc);
                case 4:
                    System.out.println("Enter a purchase order ID to delete: ");
                    String delPurchaseOrder = sc.nextLine();
                    PurchaseOrder delOrder = PurchaseOrder.get(delPurchaseOrder, x -> x.equals(delPurchaseOrder));

                    if (delOrder != null){
                        delOrder.delete();
                        System.out.println("the purchase order "+delOrder+" has been deleted.");
                    }else{
                        System.out.println("Error: Purchase order not found.");
                    }
                    System.out.println("Press enter key to return to the menu...");
                    sc.nextLine();
                    purchaseManagerMenu(sc);
                case 5:
                    purchaseManager(sc);
            }
        }

        private static void purchaseOrderUpdateMenu(Scanner sc){
            boolean exit =false;
            System.out.println("==== Update Purchase Order ====");
            System.out.println("1.  ");
            System.out.println("2.  ");
        }

}