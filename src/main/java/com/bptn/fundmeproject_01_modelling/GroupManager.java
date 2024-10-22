package com.bptn.fundmeproject_01_modelling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GroupManager {
    private static Map<String, Group> groupList = new HashMap<>();  // Store multiple groups
    // Singleton instance
    private static GroupManager instance;
    private static final String FILE_PATH = "groupData.csv";

    // Private constructor to prevent instantiation
    private GroupManager() {}

    // Static method to return the single instance of GroupManager
    public static GroupManager getInstance() {
        if (instance == null) {
            instance = new GroupManager();
        }
        return instance;
    }

    // Method to add a group to the list and save to file, with creator as the first member
    public void addGroup(Group group, String creatorName) {
        // Add the creator as the first member
        group.addMember(creatorName);  // Automatically add the creator to the group as a member
        groupList.put(group.getGroupCode(), group);  // Add group to ArrayList
        System.out.println("group size = "+groupList.size()+"\n group enteries = "+ groupList);
        saveToFile();
        //saveGroupToFile(group);  // Save the group details to the CSV file
        System.out.println("Group created by " + creatorName + " and added to the group.");
    }

    

    // Updated Method to record contributions and save them to the CSV file
    public void recordContribution(String groupCode, String memberName, double amount) {
        try (FileWriter writer = new FileWriter(groupCode + "_contributions.txt", true)) {  // Append mode
            LocalDate currentDate = LocalDate.now();  // Get today's date
            writer.write("Contribution," + groupCode + "," + memberName + "," + amount + "," + currentDate + "\n");
            System.out.println("Contribution recorded: " + memberName + " funded $" + amount + " on " + currentDate);
        } catch (IOException e) {
            System.out.println("An error occurred while saving the contribution.");
            e.printStackTrace();
        }
    }

    // Method to find a group by group code
    public Group findGroupByCode(String groupCode) {
    	if(groupList.isEmpty()) {
    		loadGroupsFromFile();
    	}
    	System.out.println("findGroupByCode calls loadGroupsFromFile: group size = "+groupList.size()+"\n group enteries = "+ groupList);
    	return groupList.get(groupCode);
    }

    // Updated Method to add a member to a group
    public String addMemberToGroup(String groupCode, String memberName) {
    	//loadGroupsFromFile(FILE_PATH);
        Group group =groupList.get(groupCode);
        
        if (group != null) {
        	if(group.getMembers().size() >= group.getMembersCount()) {
        		System.out.println("Group alreay full, join another group.");
        		return "err1";
        	}
            // Add the new member if they're not already in the list
            if (!group.getMembers().contains(memberName)) {
                group.addMember(memberName);
                System.out.println("findGroupByCode calls loadGroupsFromFile: group size = "+groupList.size()+"\n group enteries = "+ groupList);
            	
                // Now, rewrite the CSV file with the updated group members list
                saveToFile();
                return "success";
                //saveAllGroupsToFile("groupData.csv");
            } else {
                System.out.println(memberName + " is already a member of this group.");
                return "err2";
            }
        } else {
            System.out.println("Group with code " + groupCode + " not found.");
            return "err3";
        }
    }

    // Method to calculate total group savings
    public double calculateTotalGroupSavings(String groupCode) {
        double totalSavings = 0.0;
        // Go through the group contribution file and add up all contributions for the group
        try (BufferedReader reader = new BufferedReader(new FileReader(groupCode + "_contributions.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals("Contribution") && data[1].equals(groupCode)) {
                    totalSavings += Double.parseDouble(data[3]); // Add the contribution amount
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return totalSavings;
    }

    // Method to calculate the user's total contribution in a specific group
    public double calculateUserContribution(String groupCode, String memberName) {
        double userTotalContribution = 0.0;
        // Go through the group contribution file and sum up the user's contributions for the group
        try (BufferedReader reader = new BufferedReader(new FileReader(groupCode + "_contributions.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals("Contribution") && data[1].equals(groupCode) && data[2].equals(memberName)) {
                    userTotalContribution += Double.parseDouble(data[3]); // Add user's contribution amount
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userTotalContribution;
    }

    // Method to load all groups from a file
    public static void loadGroupsFromFile() {
    	 File file = new File(FILE_PATH);
    	 if (!file.exists()) {
             System.out.println("File not found: " + FILE_PATH);
             return;  // Exit the method if the file does not exist
         } else if (file.length() == 0) {
             System.out.println("File is empty: " + FILE_PATH);
             return;  // Exit the method if the file is empty
         }
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] groupDetails = line.split(",");
                // Assuming the group CSV follows the order of your toString method
                String groupName = groupDetails[0];
                String groupCode = groupDetails[1];
                int membersCount = Integer.parseInt(groupDetails[2]);
                double savingsTarget = Double.parseDouble(groupDetails[3]);
                int savingsPeriod = Integer.parseInt(groupDetails[4]);
                double monthlySavingsPerMember = Double.parseDouble(groupDetails[5]);
                String savingsFrequency = groupDetails[6];
                String startDate = groupDetails[7];
                String savingFor = groupDetails[8];
                List<String> members = new ArrayList<>(Arrays.asList(groupDetails[9].split(";")));  // Split members list
                // Re-create the group
                Group group = new Group(groupName, membersCount, savingsTarget, savingsPeriod, savingsFrequency, startDate, savingFor, groupCode, monthlySavingsPerMember);
                group.getMembers().addAll(members);  // Add members to the group
                groupList.put(groupCode, group);  // Add the group to the in-memory list
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to check if a group code exists
    public boolean checkGroupCode(String groupCode) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                // Skip the header line
                if (line.startsWith("GroupName")) {
                    continue;
                }
                String[] groupData = line.split(",");
                if (groupData.length > 1 && groupData[1].equals(groupCode)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while checking the group code.");
            e.printStackTrace();
        }
        return false;
    }
    
    public void saveToFile() {
    	try {
            File file = new File(FILE_PATH);
         // Check if file exists and is not empty before appending new lines
            //boolean isEmptyFile = file.length() == 0;
        try (FileWriter writer = new FileWriter(file)) {
            // Write header
        	
        		writer.write("GroupName,GroupCode,NumOfMembers,TargetAmount,SavingsPeriod,MonthlyContribution,Frequency,StartDate,SavingFor,Members\n");

        	
            
            // Write each group to the file
        	 for (Group group : groupList.values()) {
                 writer.write(group.toString() + "\n");  // Write each group using its toString() method
             }
            System.out.println("Groups saved successfully.");
            }
        
        } catch (IOException e) {
            System.out.println("Error saving groups to file.");
            e.printStackTrace();
        }
    }
}
