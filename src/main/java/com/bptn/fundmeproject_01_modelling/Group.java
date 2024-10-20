package com.bptn.fundmeproject_01_modelling;

import java.util.ArrayList;

public class Group {
    private String groupName;
    private int membersCount;
    private double savingsTarget;
    private int savingsPeriod;  // This is what you are missing in the current error
    private String savingsFrequency;
    private String startDate;
    private String savingFor;
    private String groupCode;
    private double monthlySavingsPerMember;
    private ArrayList<String> members; //the list where members we are adding will be stored

    // Constructor
    public Group(String groupName, int membersCount, double savingsTarget, int savingsPeriod, String savingsFrequency,
                 String startDate, String savingFor, String groupCode, double monthlySavingsPerMember) {
        this.groupName = groupName;
        this.membersCount = membersCount;
        this.savingsTarget = savingsTarget;
        this.savingsPeriod = savingsPeriod;  // Ensure this is being set
        this.savingsFrequency = savingsFrequency;
        this.startDate = startDate;
        this.savingFor = savingFor;
        this.groupCode = groupCode;
        this.monthlySavingsPerMember = monthlySavingsPerMember;
        this.members = new ArrayList<>();
    }

    // Getters and setters (if needed)
    public String getGroupName() {
        return groupName;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public double getMonthlySavingsPerMember() {
        return monthlySavingsPerMember;
    }

    public double getSavingsTarget() {
        return savingsTarget;
    }

    public String getSavingFor() {
        return savingFor;
    }

    // Add the missing method for getSavingsPeriod
    public int getSavingsPeriod() {
        return savingsPeriod;
    }

    // Method to add a member to the group
    public void addMember(String memberName) {
        members.add(memberName);  // Add the member to the list
        membersCount++;  // Increase the members count
    }

    // Override toString() method to display group details
    @Override
    public String toString() {
        return groupName + "," + groupCode + "," + membersCount + "," + savingsTarget + "," +
                savingsPeriod + "," + monthlySavingsPerMember + "," + savingsFrequency + "," + startDate + "," +
                savingFor;
    }
}
