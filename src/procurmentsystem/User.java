/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package procurmentsystem;

/**
 *
 * @author elect
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class User {
    // Define the path to the CSV file
    private static final String FILE_PATH = "C:\\Users\\elect\\OneDrive\\Documents\\NetBeansProjects\\AdminMY\\src\\adminmy\\users.csv";  // Adjust path if needed

    // Login method to validate username and password
    public boolean login(String username, String password) {
        File file = new File(FILE_PATH);
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                // Assuming CSV format: username,password,role,firstName,lastName
                if (userDetails[0].equals(username) && userDetails[1].equals(password)) {
                    return true;
                }
            }
            // If no matching credentials are found
            System.out.println("Invalid credentials, please try again.");
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
        } catch (IOException e) {
            System.out.println("An error occurred while reading the user database.");
        }
        
        return false;
    }
    
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
}


