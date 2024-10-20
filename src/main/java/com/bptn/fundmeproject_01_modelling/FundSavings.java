package com.bptn.fundmeproject_01_modelling;

import java.util.Scanner;

public class FundSavings {

	public static void fundSavings(GroupManager groupManager, User loggedInUser) {
		try (Scanner scanner = new Scanner(System.in)) {
			// Step 1: Ask the user for the group code
			System.out.println("Enter your group code: ");
			String groupCode = scanner.nextLine();

			// Step 2: Retrieve the group details from the GroupManager
			Group group = groupManager.findGroupByCode(groupCode);
			if (group == null) {
				System.out.println("Invalid group code. Returning to main menu...");
				return; // Exit if the group code is invalid
			}

			// Step 3: Display the Interac email and the pre-calculated contribution amount
			double amount = group.getMonthlySavingsPerMember();
			System.out.println("Please send your payment via Interac to: savings@fundmeapp.com");
			System.out.println("Your monthly contribution is: $" + amount);

			// Step 4: Ask the user if they want to proceed with funding
			System.out.println("Do you want to fund the savings now? (yes/no)");
			String confirmFund = scanner.nextLine();

			if (confirmFund.equalsIgnoreCase("yes")) {
				// Automatically record the contribution using GroupManager
				String memberName = loggedInUser.getName(); // Fetch logged-in user name
				groupManager.recordContribution(groupCode, memberName, amount);

				// Show success message
				System.out.println("Deposit successful. You have funded $" + amount + " for group " + groupCode + ".");
				System.out.println("A confirmation email will be sent to you.");
			} else {
				System.out.println("You have chosen not to fund the savings at this time.");
			}
		}
	}
}
