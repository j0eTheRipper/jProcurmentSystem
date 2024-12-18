
package procurmentsystem;

import java.io.FileNotFoundException;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        boolean isLoggedIn = false;  // Flag to track login status
        
        while (!isLoggedIn) {  // Keep looping until the user logs in
            System.out.println("\n--- Welcome to the Procurement Order Tracking System ---");
            System.out.println("1. Login");
            System.out.println("2. Update Account");
            System.out.println("3. Delete Account");
            System.out.println("4. Exit");
            System.out.print("Please choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    // Login functionality
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    User user = User.login(username, password);
                    if (user != null) {
                        System.out.println("Login successful!");
                        isLoggedIn = true;  // Set flag to true to exit loop
                    } else {
                        System.out.println("Invalid credentials, please try again.");
                    }
                    break;
                
                case 2:
                    // Update account (change password) functionality
                    System.out.print("Enter username: ");
                    username = scanner.nextLine();
                    
                    System.out.print("Enter old password: ");
                    String oldPassword = scanner.nextLine();
                    
                    System.out.print("Enter new password: ");
                    String newPassword = scanner.nextLine();
                    
                    if (User.updatePassword(username, oldPassword, newPassword)) {
                        break;
                    } else {
                        System.out.println("Password change failed. Please check your credentials.");
                    }
                    break;

                case 3:
                    // Delete account functionality
//                    System.out.print("Enter username: ");
//                    username = scanner.nextLine();
//                    
//                    System.out.print("Enter password: ");
//                    password = scanner.nextLine();
//                    
//                    if (userManager.deleteAccount(username, password)) {
//                        break;
//                    } else {
//                        System.out.println("Account deletion failed. Wrong credentials.");
//                    }
                    break;
                
                case 4:
                    // Exit
                    System.out.println("Exiting the system. Goodbye!");
                    scanner.close();
                    System.exit(0);  // Terminate the program
                    break;
                
                default:
                    System.out.println("Invalid option. Please choose again.");
            }
        }
        
        // Additional functionalities can go here after login if needed
    }
}





