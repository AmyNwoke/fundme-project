package com.bptn.fundmeproject_01_modelling;

import java.util.Scanner;

public class MainApp {
	

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Hello, you are one step closer to achieving your savings goal");
		
		System.out.println("Enter your full name: ");
        String fullName = scanner.nextLine();

        System.out.println("Enter your email: ");
        String email = scanner.nextLine();
        
       // System.out.println("Enter your password: ");
       // String password = scanner.nextLine();
        
     // Password validation and confirmation
        String password;
        String confirmPassword;
        
        while (true) {
            // Input password
            System.out.println("Enter your password (at least 7 characters, including 1 special character): ");
            password = scanner.nextLine();

            // Check if the password meets the criteria
            if (!User.isValidPassword(password)) {
                System.out.println("Invalid password. Please ensure it has at least 7 characters and contains at least one special character.");
                continue; // Ask for the password again if it's not valid
            }

            // Confirm password
            System.out.println("Confirm your password: ");
            confirmPassword = scanner.nextLine();

            // Check if the two passwords match
            if (!User.confirmPassword(password, confirmPassword)) {
                System.out.println("Passwords do not match. Please enter them again.");
            } else {
                break;  // Passwords match, exit the loop
            }
        }

        // Create a new user and save to file
        User user = new User(fullName, email, password);
        UserManager.saveUserToFile(user); // Store user info

        System.out.println("Congrats:");
        System.out.println("Full Name: " + user.getName());
        System.out.println("Email: " + user.getEmail());

        
        
        // Log in process with retry option
        while (true) {
            System.out.println("Welcome back");
            System.out.print("Login Email: ");
            String loginEmail = scanner.nextLine();
            System.out.print("Input Password: ");
            String loginPassword = scanner.nextLine();

            // Validate user credentials using the UserManager class
            if (UserManager.validateUser(loginEmail, loginPassword)) {
                System.out.println("Login successful! Welcome, " + user.getName());
                break;  // Exit the loop on successful login
            } else {
                System.out.println("Invalid email or password.");
                
                // Ask the user if they want to try again
                System.out.println("Do you want to try logging in again? (yes/no)");
                String tryAgain = scanner.nextLine();
                if (tryAgain.equalsIgnoreCase("no")) { //equalsignorecase is a method in java that compares two strings
                    System.out.println("Exiting login. Goodbye!");
                    break;  // Exit the login loop and end the program
                }
            }
        }

        scanner.close();
	}
}