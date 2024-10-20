package com.bptn.fundmeproject_01_modelling;

import java.util.Scanner;

public class MainApp {

	public static void main(String[] args) {
		// Create a GroupManager instance to handle groups
		GroupManager groupManager = new GroupManager();
		User loggedInUser = null;

		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("Hello, you are one step closer to achieving your savings goal");

			System.out.println("Enter your full name: ");
			String fullName = scanner.nextLine();

			String email;

			while (true) {
				System.out.println("Enter your email: ");
				email = scanner.nextLine();

				// Check if the email is valid
				if (!User.isValidEmail(email)) {
					System.out.println("Invalid email format. Please enter a valid email.");
					continue; // Ask for the email again if it's not valid
				}

				// Loop to ensure user doesn't sign up with an existing email

				// Check if the email already exists
				if (UserManager.emailExists(email)) {
					System.out.println("User with this email already exists. Please proceed to login.");
					return; // Exit or proceed to login logic
				} else {
					break; // proceed with registration
				}
			}

			// Password validation and confirmation
			String password;
			String confirmPassword;

			while (true) {
				// Input password
				System.out.println("Enter your password (at least 7 characters, including 1 special character): ");
				password = scanner.nextLine();

				// Check if the password meets the criteria
				if (!User.passwordValid(password)) {
					System.out.println("Invalid password. Please ensure it has at least 7 characters.");
					continue; // Ask for the password again if it's not valid
				}

				// Confirm password
				System.out.println("Confirm your password: ");
				confirmPassword = scanner.nextLine();

				// Check if the two passwords match
				if (!User.confirmPassword(password, confirmPassword)) {
					System.out.println("Passwords do not match. Please enter them again.");
				} else {
					break; // Passwords match, exit the loop
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
					loggedInUser = user; // now a user has been logged in, we set the logged in user
					break; // Exit the loop on successful login
				} else {
					System.out.println("Invalid email or password.");

					// Ask the user if they want to try again
					System.out.println("Do you want to try logging in again? (yes/no)");
					String tryAgain = scanner.nextLine();
					if (tryAgain.equalsIgnoreCase("no")) {
						System.out.println("Exiting login. Goodbye!");
						break; // Exit the login loop and end the program
					}
				}
			}

			// After successful login, display menu options
			while (true) {
				System.out.println("Welcome back! What would you like to do?");
				System.out.println("1. Create a Group");
				System.out.println("2. Join an Existing Group");
				System.out.println("3. Fund Savings");
				System.out.println("4. View Group Savings Progress");
				System.out.println("5. Logout");

				// Get user input
				if (scanner.hasNext()) {
					int choice = scanner.nextInt();
					scanner.nextLine(); // Consume newline

					switch (choice) {
					case 1:
						// Pass the groupManager instance when creating a new group
						CreateGroup.createNewGroup(groupManager);
						break;
					case 2:
						JoinGroup.joinExistingGroup(groupManager, loggedInUser); // Pass groupManager to join an
																					// existing group
						break;
					case 3:
						FundSavings.fundSavings(groupManager); // Call method to fund savings
						break;
					// case 4:
					// viewSavingsProgress(); // Call method to view group savings progress
					// break;
					case 5:
						System.out.println("Logging out... Goodbye!");
						scanner.close();
						return; // End the program
					default:
						System.out.println("Invalid choice. Please try again.");
					}

				} else {
					System.out.println("Invalid input. Please enter a number between 1 and 5.");
					scanner.nextLine(); // Clear invalid input

				}
			}
		}
	}
}

// Method to join an existing group (uses groupManager)
// private static void joinGroup(GroupManager groupManager) {
// Code to join an existing group using the groupManager
// System.out.println("You selected to join an existing group.");
// }

// private static void viewSavingsProgress() {
// Code to view group savings progress will go here
// System.out.println("You selected to view group savings progress.");
// }
