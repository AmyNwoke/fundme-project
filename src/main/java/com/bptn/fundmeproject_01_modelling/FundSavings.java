package com.bptn.fundmeproject_01_modelling;

import java.util.Scanner;

public class FundSavings {

    public static void fundSavings(GroupManager groupManager) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter your group code: ");
            String groupCode = scanner.nextLine();

            System.out.println("Enter your name: ");
            String memberName = scanner.nextLine();

            System.out.println("Enter the amount to fund: ");
            double amount = scanner.nextDouble();

            // Record the contribution using GroupManager
            groupManager.recordContribution(groupCode, memberName, amount);

            System.out.println("Funded $" + amount + " for group " + groupCode + ". Confirmation email will be sent.");
        }
    }
}
