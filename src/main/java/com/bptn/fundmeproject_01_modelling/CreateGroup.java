package com.bptn.fundmeproject_01_modelling;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CreateGroup {

    public static void createNewGroup() {
        try (Scanner scanner = new Scanner(System.in)) {
			// Gather group details from the user
			System.out.println("Enter the group name: ");
			String groupName = scanner.nextLine();

			System.out.println("Enter the number of members: ");
			int membersCount = scanner.nextInt();
			scanner.nextLine();  // read user input

			System.out.println("Enter the savings target/budget (e.g., $10,000): ");
			double savingsTarget = scanner.nextDouble();
			scanner.nextLine();  // read user input

			System.out.println("Enter the savings period (in months): ");
			int savingsPeriod = scanner.nextInt();
			scanner.nextLine();  // read user input 

			// Hardcoded frequency to monthly as thats what we will offer for now
			String savingsFrequency = "Monthly";

			System.out.println("Enter the start date (dd-mm-yyyy): ");
			String startDate = scanner.nextLine();

			System.out.println("What is this saving for? (e.g., vacation): ");
			String savingFor = scanner.nextLine();

			// Calculate monthly contribution per member
			double totalMonthlySavings = savingsTarget / savingsPeriod;
			double monthlySavingsPerMember = totalMonthlySavings / membersCount;

			// Generate a random group code using Math.random()
			String groupCode = generateGroupCode();

			// Display the group creation success message and calculated monthly savings
			System.out.println("Group creation successful!");
			System.out.println("Your group code is: " + groupCode);
			System.out.println("Your monthly savings per member is: $" + monthlySavingsPerMember);

			// Ask the user if they want to fund savings now or exit
			System.out.println("Would you like to fund the savings now? (yes/no)");
			String fundSavingsNow = scanner.nextLine();

			
				if (fundSavingsNow.toLowerCase().equals("yes")) {
			    // Simulate funding savings by displaying an Interac email
			    System.out.println("Please send your payment via Interac to: savings@fundmeapp.com");
			    System.out.println("A confirmation email will be sent once the payment is received.");
			    // Logic to send a confirmation email using Java Mail can be added here
			} else {
			    System.out.println("Returning to main menu...");
			    return; // Exit to main menu
			}

			// Save the group information to a file
			saveGroupToFile(groupName, membersCount, savingsTarget, savingsPeriod, savingsFrequency, startDate, savingFor, groupCode, monthlySavingsPerMember);
		}
    }

    private static String generateGroupCode() {
        int code = (int) (Math.random() * 999999);  // Generate a random number between 0 and 999999
        return String.format("%06d", code);  // Format the number as a 6-digit code with leading zeros if necessary
    }

    private static void saveGroupToFile(String groupName, int membersCount, double savingsTarget, int savingsPeriod, String savingsFrequency,
                                        String startDate, String savingFor, String groupCode, double monthlySavingsPerMember) {
        try {
            FileWriter writer = new FileWriter("groupData.txt", true);  // Append mode
            writer.write("Group Name: " + groupName + "\n");
            writer.write("Group Code: " + groupCode + "\n");
            writer.write("Number of Members: " + membersCount + "\n");
            writer.write("Savings Target: $" + savingsTarget + "\n");
            writer.write("Savings Period: " + savingsPeriod + " months\n");
            writer.write("Monthly Savings per Member: $" + monthlySavingsPerMember + "\n");
            writer.write("Savings Frequency: " + savingsFrequency + "\n");
            writer.write("Start Date: " + startDate + "\n");
            writer.write("Purpose: " + savingFor + "\n\n");
            writer.close();
            System.out.println("Group information saved successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while saving the group information.");
            e.printStackTrace();
        }
    }
}
