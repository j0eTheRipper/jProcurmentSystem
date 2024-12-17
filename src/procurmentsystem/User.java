

package procurmentsystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import procurmentsystem.Table.InteractionsWithTable;
import procurmentsystem.Table.Table;
import procurmentsystem.Table.ValueNotFound;

public abstract class User extends InteractionsWithTable {
    // Define the path to the CSV file
    private static final String FILE_PATH = "C:\\Users\\elect\\OneDrive\\Documents\\NetBeansProjects\\AdminMY\\src\\adminmy\\users.csv";  // Adjust path if needed
    protected String username;
    protected String role;
    protected String password;
    protected String firstName;
    protected String lastName;

    
    // Login method to validate username and password
    public static User login(String email, String password) throws FileNotFoundException {
        Table table = new Table(FILE_PATH);

        try {
            List<String> row = table.getRow("email", (x) -> x.equals(email));
            switch (row.get(4)) {
                case "Financial Manager":
                    return new FinancialManager(row.get(0), row.get(1), row.get(2), row.get(3), row.get(5));

                case "Sales Manager ":
                    return new SalesManager(row.get(0), row.get(1), row.get(2), row.get(3), row.get(5));

                case "Purchase Manager":
                    return new PurchaseManager(row.get(0), row.get(1), row.get(2), row.get(3), row.get(5));

                case "Inventory Manager":
                    return new InventoryManager(row.get(0), row.get(1), row.get(2), row.get(3), row.get(5));

                case "Admin":
                    return new Admin(row.get(0), row.get(1), row.get(2), row.get(3), row.get(5));
            }
        } catch (ValueNotFound ex) {
            return null;
        }




        return null;
    }
        
//        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] userDetails = line.split(",");
//                // Assuming CSV format: username,password,role,firstName,lastName
//                if (userDetails[0].equals(username) && userDetails[1].equals(password)) {
//                    return true;
//                }
//            }
//            // If no matching credentials are found
//            System.out.println("Invalid credentials, please try again.");
//        } catch (FileNotFoundException e) {
//            System.out.println("Error: File not found.");
//        } catch (IOException e) {
//            System.out.println("An error occurred while reading the user database.");
//        }
//        
//        return false;
//  }
    
    // Method to update the password
    public boolean updatePassword(String username, String oldPassword, String newPassword) {
        List<String> fileContent = new ArrayList<>();
        boolean passwordUpdated = false;

        // Read all lines from users.csv
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                
                // Check if the username and old password match
                if (userDetails[0].equals(username) && userDetails[1].equals(oldPassword)) {
                    // Update password if old password is correct
                    userDetails[1] = newPassword;
                    passwordUpdated = true;
                    System.out.println("Password updated successfully!");
                }
                
                // Add updated or unchanged line back to file content
                fileContent.add(String.join(",", userDetails));
            }
        } catch (IOException e) {
            System.out.println("Error reading user database: " + e.getMessage());
            return false;
        }

        // If the password was updated, write back to the file
        if (passwordUpdated) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
                for (String line : fileContent) {
                    writer.write(line);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error writing to user database: " + e.getMessage());
                return false;
            }
        } else {
            System.out.println("Old password incorrect. Password update failed.");
        }

        return passwordUpdated;
    }    
    // Method to delete an account
public boolean deleteAccount(String username, String password) {
    List<String> fileContent = new ArrayList<>();
    boolean accountDeleted = false;

    try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] userDetails = line.split(",");
            
            // Check if the credentials match
            if (userDetails[0].equals(username) && userDetails[1].equals(password)) {
                accountDeleted = true;
                System.out.println("Account deleted successfully.");
                continue; // Skip adding this user to the new file content
            }
            
            // Add non-deleted users back to the file content
            fileContent.add(String.join(",", userDetails));
        }
    } catch (IOException e) {
        System.out.println("An issue occurred while accessing the user database. Please try again later.");
        return false;
    }

    if (accountDeleted) {
        // Write updated content back to the file (with user removed)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String line : fileContent) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("An issue occurred while updating the user database. Please try again later.");
            return false;
        }
    }

    return accountDeleted;
    
    
}

private List<User> loadUsersFromCSV() {
    List<User> users = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] details = line.split(",");
            if (details.length == 5) { // Ensure all fields are present
                users.add(new User(details[0], details[1], details[2], details[3], details[4]));
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading user database: " + e.getMessage());
    }
    return users;
}

private boolean saveUsersToCSV(List<User> users) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
        for (User user : users) {
            writer.write(user.toString());  // Call toString() to get the correct CSV format
            writer.newLine();
        }
        return true;
    } catch (IOException e) {
        System.out.println("Error writing to user database: " + e.getMessage());
        return false;
    }
}



public boolean createUser(String username, String password, String firstName, String lastName, String role) {
    // Load existing users to check for duplicates
    List<User> users = loadUsersFromCSV();
    for (User user : users) {
        if (user.getUsername().equals(username)) {
            System.out.println("Username already exists. Please choose a different username.");
            return false; // Duplicate username
        }
    }
    
    // Create new user and add to the list
    User newUser = new User(username, password, role, firstName, lastName);
    users.add(newUser);
    
    // Save the updated list back to the CSV
    if (saveUsersToCSV(users)) {
        System.out.println("User created successfully.");
        return true;
    } else {
        System.out.println("Error saving the user to the database.");
        return false;
    }
}



}
