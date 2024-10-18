package com.bptn.fundmeproject_01_modelling;


import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class GroupManager {

    private ArrayList<Group> groupList = new ArrayList<>();  // Store multiple groups

    // Method to add a group to the list and save to file
    public void addGroup(Group group) {
        groupList.add(group);  // Add group to ArrayList
        saveGroupToFile(group);  // Save the group details to the CSV file
    }

    // Method to save group information to a CSV file using Group's toString method
    public void saveGroupToFile(Group group) {
        try (FileWriter writer = new FileWriter("groupData.csv", true)) {  // Append mode for CSV file
            writer.write(group.toString() + "\n");  // Write group details using the Group's toString() method
            System.out.println("Group information saved successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while saving the group information.");
            e.printStackTrace();
        }
    }

    // Updated Method to record contributions and save them to the CSV file
    public void recordContribution(String groupCode, String memberName, double amount) {
        try (FileWriter writer = new FileWriter("groupData.csv", true)) {  // Append mode
            LocalDate currentDate = LocalDate.now();  // Get today's date
            writer.write("Contribution," + groupCode + "," + memberName + "," + amount + "," + currentDate + "\n");
            System.out.println("Contribution recorded: " + memberName + " funded $" + amount + " on " + currentDate);
        } catch (IOException e) {
            System.out.println("An error occurred while saving the contribution.");
            e.printStackTrace();
        }
    }

    // Method to retrieve all groups (if needed)
    public ArrayList<Group> getAllGroups() {
        return groupList;
    }
}
