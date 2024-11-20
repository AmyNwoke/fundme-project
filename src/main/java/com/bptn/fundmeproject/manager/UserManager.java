package com.bptn.fundmeproject.manager;

import com.bptn.fundmeproject.utility.DatabaseConnectionF;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


import com.bptn.fundmeproject.model.User;

public class UserManager {
	// user list of type user
	private static List<User> users = new ArrayList<>(); // List to store all registered users

	// Method to load users from the database instead of a file
    public static void loadUsersFromFile() {
        String sql = "SELECT name, email, password FROM \"USERS\"";
        try (Connection conn = DatabaseConnectionF.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            users.clear(); // Clear the list to avoid duplicates
            while (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                users.add(new User(name, email, password));
            }
            System.out.println("Users loaded successfully from the database.");
        } catch (SQLException e) {
            System.out.println("Error loading users from the database.");
            e.printStackTrace();
        }
    }

    // Method to check if an email already exists in the database
    public static boolean emailExists(String email) {
        String sql = "SELECT email FROM \"USERS\" WHERE email = ?";
        try (Connection conn = DatabaseConnectionF.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Returns true if email is found
            }
        } catch (SQLException e) {
            System.out.println("Error checking if email exists.");
            e.printStackTrace();
        }
        return false; // Return false if email is not found or if an error occurs
    }

    // Method to save a new user to the database instead of a file
    public static void saveUserToFile(User user) {
        String sql = "INSERT INTO \"USERS\" (name, email, password) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnectionF.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.executeUpdate();
            users.add(user); // Add the user to the in-memory list as well
            System.out.println("User saved successfully to the database.");
        } catch (SQLException e) {
            System.out.println("An error occurred while saving the user to the database.");
            e.printStackTrace();
        }
    }
    // Method to retrieve a user by email and password from the database
    public static User getUserByEmailAndPassword(String email, String password) {
        String sql = "SELECT name, email, password FROM \"USERS\" WHERE email = ? AND password = ?";
        try (Connection conn = DatabaseConnectionF.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getString("name"), rs.getString("email"), rs.getString("password"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving user by email and password.");
            e.printStackTrace();
        }
        return null; // Return null if no user is found or if an error occurs
    }
	
	
	
	
	
	/*public static void loadUsersFromFile(String filename) {
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

*/

	/*// Method to check if an email already exists in the list of users
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

*/

}
