package com.bptn.fundmeproject_01_modelling;

public class Person {
	
	//declaring variable
	
	private String name;
	private String email;
	
	//constructor 
	
	 public Person(String name, String email) {
	        this.name = name;
	        this.email = email;
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
}
	 
	 



/*//main method to test the class
public static void main(String[] args) {
    // create an instance of Person
    Person person = new Person("Amara", "awesomeamy300@gmail.com");

    // print out the name and email
    System.out.println("Name: " + person.getName());
    System.out.println("Email: " + person.getEmail());
}
}
*/
