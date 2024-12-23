package com.bptn.fundmeproject.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
	// Define user variables

	private String name;
	private String email;
	private String password;

//create constructor, getter and setters

	public User(String name, String email, String password) {

		this.name = name;
		this.password = password;
		this.email = email;

	}

	public User(String email) {
		this.email = email;

	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// Static method to validate password rules (e.g., at least 7 characters,
	// includes a special character)
	public static boolean passwordValid(String password) {
		if (password.length() < 7) {
			return false;
		}

		return true;
	}

	// writing method to check if password and confirm password is equal
	public static boolean confirmPassword(String password, String confirmPassword) {
		return password.equals(confirmPassword);
	}

	// this checks for email format
	private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

	public static boolean isValidEmail(String email) {
		Pattern pattern = Pattern.compile(EMAIL_REGEX);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
}
