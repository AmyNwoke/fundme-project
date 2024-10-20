package com.bptn.fundmeproject_01_modelling;

import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class UserManager {
	private static final String FILE_PATH = "userData.csv";

    // Method to save user information to a file
    public static void saveUserToFile(User user) {
        try {
            // Using 'storage' as the variable name for FileWriter
            FileWriter storage = new FileWriter(FILE_PATH, true);  // 'true' means append mode
            storage.write(user.getName() + "," + user.getEmail() + "," + user.getPassword() + "\n");
            storage.close();
            System.out.println("User data saved successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while saving user data.");
            e.printStackTrace();
        }
    }
    
    ;

    public static boolean emailExists(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length > 1 && userData[1].equals(email)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while checking email.");
            e.printStackTrace();
        }
        return false;
    }

    // Method to validate user login credentials
    public static boolean validateUser(String email, String password) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("userData.csv"));
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line by commas to extract user details
                String[] userDetails = line.split(",");
                String storedEmail = userDetails[1];
                String storedPassword = userDetails[2];

                // Check if the email and password match
                if (storedEmail.equals(email) && storedPassword.equals(password)) {
                    reader.close();
                    return true;  // User credentials are correct
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("An error occurred while reading user data.");
            e.printStackTrace();
        }
        return false;  // No match found, or credentials are incorrect
    }
}
