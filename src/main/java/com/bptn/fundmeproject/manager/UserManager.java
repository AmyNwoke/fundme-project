package com.bptn.fundmeproject.manager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.bptn.fundmeproject.model.User;

public class UserManager {
	// user list of type user
	private static List<User> users = new ArrayList<>(); // List to store all registered users

	public static void loadUsersFromFile(String filename) {
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {// automatically close resource
			String line;
			// read line till there are no more line to be read
			while ((line = reader.readLine()) != null) {
				line = line.trim(); // Remove leading and trailing whitespace
				if (line.isEmpty()) {
					continue; // Skip empty lines
				}

				String[] userDetails = line.split(",");

				// Ensure the line has exactly 3 elements (name, email, password)
				if (userDetails.length == 3) {
					String name = userDetails[0]; // Assuming the first element is the user's name
					String email = userDetails[1]; // Assuming the second element is the email
					String password = userDetails[2]; // Assuming the third element is the password

					// Create a new User object and add it to the list
					users.add(new User(name, email, password));
				} else {
					System.err.println("Invalid user data format: " + line); // Print an error for invalid format
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Method to check if an email already exists in the list of users
	public static boolean emailExists(String email) {
		for (User user : users) {
			if (user.getEmail().equals(email)) {
				return true; // Email found, return true
			}
		}
		return false; // Email not found, return false
	}

	// Method to save a new user to the file (CSV format)
	public static void saveUserToFile(User user) {
		try (FileWriter writer = new FileWriter("userData.csv", true)) { // Append mode to add a new user
			writer.write(user.getName() + "," + user.getEmail() + "," + user.getPassword() + "\n");
			users.add(user); // Also add the user to the in-memory list
			System.out.println("User saved successfully.");
		} catch (IOException e) {
			System.out.println("An error occurred while saving the user data.");
			e.printStackTrace();
		}
	}

	// method to retrieve a user by email and password
	public static User getUserByEmailAndPassword(String email, String password) {
		// Loop through the loaded users and find a match
		for (User user : users) {
			System.out.println("user: " + user.getEmail() + "password: " + user.getPassword());
			if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
				return user; // Return the matching user
			}
		}
		return null; // No match found, or credentials are incorrect
	}
}
