package com.bptn.fundmeproject_01_modelling;

import java.util.ArrayList;
import java.util.List;

public class GroupSavingsProgress {

    private GroupManager groupManager;
    private User loggedInUser;

    // Constructor to initialize with GroupManager and logged-in user
    public GroupSavingsProgress(GroupManager groupManager, User loggedInUser) {
        this.groupManager = groupManager;
        this.loggedInUser = loggedInUser;
    }

    // Method to fetch and display savings progress for each group the user belongs to
    public List<String> fetchSavingsProgress() {
        List<String> progressDetails = new ArrayList<>(); // To store group details and progress
        
        // Loop through all groups to find the ones the user belongs to
        for (Group group : groupManager.getAllGroups()) {
            // Check if the logged-in user is a member of the group
            if (group.getMembers().contains(loggedInUser.getName())) {

                // Calculate savings progress for the group
                double totalGroupSavings = groupManager.calculateTotalGroupSavings(group.getGroupCode());
                double userContribution = groupManager.calculateUserContribution(group.getGroupCode(), loggedInUser.getName());
                int monthsLeft = calculateMonthsLeft(group.getSavingsPeriod(), totalGroupSavings, group.getSavingsTarget());

                // Format the details to populate the dashboard cards
                String groupProgress = String.format(
                    "Group Code: %s\nGroup Name: %s\nTarget Savings: $%.2f\nTotal Group Savings: $%.2f\n" +
                    "Monthly Contribution: $%.2f\nUser's Total Contribution: $%.2f\nSavings Period: %d months\nMonths Left: %d",
                    group.getGroupCode(), group.getGroupName(), group.getSavingsTarget(), totalGroupSavings,
                    group.getMonthlySavingsPerMember(), userContribution, group.getSavingsPeriod(), monthsLeft
                );

                // Add group progress details to the list
                progressDetails.add(groupProgress);
            }
        }

        return progressDetails; // Return the list of progress details (can be displayed in dashboard cards)
    }

    // Method to calculate how many months are left based on the savings progress
    private int calculateMonthsLeft(int savingsPeriod, double totalSavings, double targetSavings) {
        if (targetSavings == 0) {
            return 0; // If target savings is 0, we assume no months left
        }

        double monthlySavingsRate = targetSavings / savingsPeriod;
        double monthsSavedSoFar = totalSavings / monthlySavingsRate;

        return (int) Math.ceil(savingsPeriod - monthsSavedSoFar); // Round up to next full month
    }
}
