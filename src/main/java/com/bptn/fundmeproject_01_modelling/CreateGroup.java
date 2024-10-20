package com.bptn.fundmeproject_01_modelling;
import java.util.Scanner;         // For reading user input

public class CreateGroup {

    public static void createNewGroup(GroupManager groupManager, User loggedInUser) {
        try (Scanner scanner = new Scanner(System.in)) {
            // Gather group details from the user
            System.out.println("Enter the group name: ");
            String groupName = scanner.nextLine();

            System.out.println("Enter the number of members: ");
            int membersCount = scanner.nextInt();
            scanner.nextLine();  // Read user input

            System.out.println("Enter the savings target/budget (e.g., $10,000): ");
            double savingsTarget = scanner.nextDouble();
            scanner.nextLine();  // Read user input

            System.out.println("Enter the savings period (in months): ");
            int savingsPeriod = scanner.nextInt();
            scanner.nextLine();  // Read user input

            // Hardcoded frequency to monthly as that's what we will offer for now
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

            // Print group code and monthly savings per member BEFORE asking to fund
            System.out.println("Your group code is: " + groupCode);
            System.out.println("Your monthly savings per member is: $" + monthlySavingsPerMember);

            // Ask the user if they want to fund savings now or exit
            System.out.println("Would you like to fund the savings now to confirm group creation? (yes/no)");
            String fundSavingsNow = scanner.nextLine();

            // If the user chooses to fund the savings, display Interac email and save group to file
            if (fundSavingsNow.equalsIgnoreCase("yes")) {
                System.out.println("Please send your payment via Interac to: fundmesavings.gmail.com");
                System.out.println("A confirmation email will be sent once the payment is received.");

                // Create a new group object
                Group newGroup = new Group(groupName, membersCount, savingsTarget, savingsPeriod, savingsFrequency, startDate, savingFor, groupCode, monthlySavingsPerMember);

                // Save the group to the GroupManager (also saves to the file)
                groupManager.addGroup(newGroup);
                groupManager.saveGroupToFile(newGroup);
                
                EmailManager emailManager = new EmailManager();
                emailManager.sendGroupCreationEmail(loggedInUser.getName(), loggedInUser.getEmail(), groupCode);

                // Print success message
                System.out.println("Group creation successful!");
                
                // Exit to login
               // System.out.println("Exiting to login. Please log in again.");
                //return;  // Exit the group creation flow and return to login
            } else {
                System.out.println("Group creation canceled. Exiting to login.");
                return;  // Exit the group creation flow and return to login
            }
        }
    }

    // Method to generate a random group code
    private static String generateGroupCode() {
        int code = (int) (Math.random() * 999999);  // Generate a random number between 0 and 999999
        return String.format("%06d", code);  // Format the number as a 6-digit code with leading zeros if necessary
    }
}
