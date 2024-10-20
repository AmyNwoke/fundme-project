package com.bptn.fundmeproject_01_modelling;

import java.util.Scanner;

public class JoinGroup {
	
	public static void joinExistingGroup(GroupManager groupManager, User loggedInUser) {
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("Group code :  ");
			String groupCode = scanner.nextLine();
			
			//after code has been inputed, check groupManager to find the group to see if it exist
			
			Group group = groupManager.findGroupByCode(groupCode);
			
			if (group == null) {
				
				System.out.println("invalid group code. Returning to main menu");
				return; 
				
			}
			
			//however, if found, we will display the details of the group to the user to confirm 
			
			 System.out.println("Group Details:");
			    System.out.println("Group Name: " + group.getGroupName());
			    System.out.println("Savings Target: $" + group.getSavingsTarget());
			    System.out.println("Monthly Contribution Per Member: $" + group.getMonthlySavingsPerMember());
			    System.out.println("Savings Period: " + group.getSavingsPeriod() + " months");

			 // Since we have the user's name in our system, we will fetch it so we are able to save their contribution on groupmanager
			    String memberName = loggedInUser.getName();

			    // Then we will display the user's name and their contribution before they acknowledge
			    System.out.println("Hello, " + memberName + "! Your monthly contribution for this group will be: $" 
			                       + group.getMonthlySavingsPerMember());
			    
			    
			    
			    // now that we have displayed the group details and how much the contribution is , we will get confirmation from user 
			    
			    System.out.println("Do you want to join this group? (yes/no)");
			    String confirmJoin = scanner.nextLine();

			    if (!confirmJoin.equalsIgnoreCase("yes")) {
			        System.out.println("You were not added to the group. Exiting to login...");
			        return;  // Exit if the user does not want to join
			    }

			    // Display contribution amount and ask if they want to fund savings
			    System.out.println("Your monthly contribution is $" + group.getMonthlySavingsPerMember());
			    System.out.println("Do you want to fund savings now? (yes/no)");
			    String fundSavingsNow = scanner.nextLine();

			    if (fundSavingsNow.equalsIgnoreCase("yes")) {
			        // Automatically record the contribution using the system-calculated amount
			        double amount = group.getMonthlySavingsPerMember();
			        
			        // Record the contribution using GroupManager
			        groupManager.recordContribution(groupCode, memberName, amount);

			        // Save the member's details to the group file
			        groupManager.addMemberToGroup(groupCode, memberName);

			        // Display success message
			        System.out.println("Congratulations, you have successfully joined the group.");
			        System.out.println("A confirmation email will be sent to you.");

			        // Exit to login
			        System.out.println("Exiting to login...");
			        return;
			        
			    } else {
			        System.out.println("You were not added to the group. Exiting to login...");
			    }
		}
		
		//collect group code from user 

	    }
	
	
	
	
	}


