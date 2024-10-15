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



}
