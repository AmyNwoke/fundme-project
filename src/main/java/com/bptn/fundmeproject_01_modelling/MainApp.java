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
        
        System.out.println("Enter your password: ");
        String password = scanner.nextLine();
        
        User user = new User(fullName, email, password);

        
        System.out.println("Congrats:");
        System.out.println("Full Name: " + user.getName());
        System.out.println("Email: " + user.getEmail());
    }
}

	


