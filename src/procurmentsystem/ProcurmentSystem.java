/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package procurmentsystem;

import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author j0eTh
 */
public class ProcurmentSystem {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n==== Inventory Manager Menu ====");
            System.out.println("1. Supplier Management");
            System.out.println("2. Item Management");
            System.out.println("3. Exit");
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
                    exit = true;
                    System.out.println("Exiting the system. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
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

    private static void itemMenu(Scanner scanner) throws IOException {
        boolean backToMain = false;

        while (!backToMain) {
            System.out.println("\n==== Item Management ====");
            System.out.println("1. Add an Item");
            System.out.println("2. Delete an Item");
            System.out.println("3. View all Items");
            System.out.println("4. Update an Item");
            System.out.println("5. Increase Item Quantity");
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


                case 5: // Increase Item Quantity
                    Item.displayAllItems(scanner);
                    System.out.println("Enter Item Code to increase quantity: ");
                    String itemCodeToIncrease = scanner.nextLine();
                    Item itemToIncrease = Item.get("ItemCode", (x) -> x.equals(itemCodeToIncrease));

                    if (itemToIncrease != null) {
                        System.out.println("Current Quantity: " + itemToIncrease.getItemQuantity());
                        System.out.print("Enter quantity to add: ");
                        int quantityToAdd = scanner.nextInt();
                        scanner.nextLine();

                        if (quantityToAdd > 0) {
                            boolean quantityIncreased = itemToIncrease.increaseItemQuantity(quantityToAdd);
                            if (quantityIncreased) {
                                System.out.println("Quantity increased successfully!");
                                System.out.println("New Quantity: " + itemToIncrease.getItemQuantity());
                            } else {
                                System.out.println("Failed to increase quantity.");
                            }
                        } else {
                            System.out.println("Invalid quantity. Please enter a positive number.");
                        }
                    }
                    break;


                case 6: // Back to Main Menu
                    backToMain = true;
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}