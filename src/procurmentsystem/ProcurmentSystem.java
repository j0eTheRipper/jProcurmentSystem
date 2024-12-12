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
        String fileName = "src/files/supplier.csv";

        while (!exit) {
            System.out.println("\n==== Procurement System ====");
            System.out.println("1. Add a Supplier");
            System.out.println("2. Delete a Supplier");
            System.out.println("3. View all Suppliers");
            System.out.println("4. Update a Supplier");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1: // Add a Supplier
                    System.out.print("Enter Supplier Name: ");
                    String supplierName = scanner.nextLine();
                    System.out.print("Enter Supplier Contact: ");
                    String supplierContact = scanner.nextLine();

                    // Create and write supplier details to the CSV
                    Supplier supplier = new Supplier(supplierName, supplierContact);
                    supplier.add();
                    System.out.println("Supplier added successfully!");
                    break;

                case 2: // Delete a Supplier
                    System.out.print("Enter Supplier ID to delete: ");
                    String deleteID = scanner.nextLine();
                    Supplier supplier1 =  Supplier.get("supplierID", (x) -> x.equals(deleteID));
                    supplier1.delete();
                    break;

                case 3: // View All Suppliers
                    Supplier.displayAllSuppliers(scanner);
                    System.out.println("Press enter key to continue...");
                    scanner.nextLine();
                    break;


                case 4:
                    Supplier.displayAllSuppliers(scanner);
                    System.out.println("Enter ID of Supplier you want to update: ");
                    String updateID = scanner.nextLine();
                    Supplier UpdateSupplier = Supplier.get("supplierID", (x) -> x.equals(updateID));
                    if (UpdateSupplier == null) {
                        break;
                    }
                    System.out.println("1:Change name \n2:Change contact number \nChoose the column you wish to edit: ");
                    choice = scanner.nextInt();
                    if(choice == 1){
                        System.out.println("Enter the new name: ");
                        String name = scanner.next();
                        boolean updatedSuccessfully = UpdateSupplier.setSupplierName(name);

                        if(updatedSuccessfully) {
                            System.out.println("Updated successfully");
                            System.out.println(UpdateSupplier);
                        } else
                            System.out.println("Please enter a valid name");
                    } else if (choice == 2) {

                        System.out.println("Enter the new contact number: ");
                        String number = scanner.next();
                        boolean updatedSuccessfully = UpdateSupplier.setSupplierContact(number);
                        if(updatedSuccessfully) {
                            System.out.println("Updated successfully");
                            System.out.println(UpdateSupplier);
                        } else
                            System.out.println("Please enter a valid number");

                    }else{
                        System.out.println("Invalid input");
                        continue;
                    }

                    break;


                case 5: // Exit
                    exit = true;
                    System.out.println("Exiting the system. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }

}
