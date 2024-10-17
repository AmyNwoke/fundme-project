package com.bptn.fundmeproject_01_modelling;

public class User extends Person {
	
	
	private String password;

	public User(String name, String email, String password) {
		 super(name, email);  // Calling the parent class 
	        this.password = password;
		
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// Static method to validate password rules (e.g., at least 7 characters, includes a special character)
    public static boolean passwordValid(String password) {
        if (password.length() < 7) {
            return false;
        }

        return true;
    }

    // Static method to check if the password matches the confirmation
    public static boolean confirmPassword(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }
}


