/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package procurmentsystem;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author j0eTh
 */
public class ProcurmentSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        String fileName = "src/files/supplier.csv";

        while (!exit) {
            System.out.println("\n==== Procurement System ====");
            System.out.println("1. Add a Supplier");
            System.out.println("2. Delete a Supplier");
            System.out.println("3. View All Suppliers");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1: // Add a Supplier
                    System.out.print("Enter Supplier ID: ");
                    String supplierID = scanner.nextLine();
                    System.out.print("Enter Supplier Name: ");
                    String supplierName = scanner.nextLine();
                    System.out.print("Enter Supplier Contact: ");
                    String supplierContact = scanner.nextLine();

                    // Create and write supplier details to the CSV
                    Supplier supplier = new Supplier(supplierID, supplierName, supplierContact, fileName);
                    supplier.writeToCSV();
                    System.out.println("Supplier added successfully!");
                    break;

                case 2: // Delete a Supplier
                    System.out.print("Enter Supplier ID to delete: ");
                    String deleteID = scanner.nextLine();
                    Supplier.deleteFromCSV(fileName, deleteID);
                    break;

                case 3: // View All Suppliers
                    try {
                        Table table = new Table(fileName);
                        System.out.println("\n=== Current Suppliers ===");
                        List<List<String>> rows = table.getRows("supplierID", id -> true); // Get all rows

                        if (rows.isEmpty()) {
                            System.out.println("No suppliers found.");
                        } else {
                            for (List<String> row : rows) {
                                System.out.println(String.join(", ", row));
                            }
                        }
                    } catch (FileNotFoundException e) {
                        System.out.println("The supplier file was not found.");
                    }
                    break;


                case 4: // Exit
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
