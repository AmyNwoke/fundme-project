package com.bptn.fundmeproject_01_modelling;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String groupName;
    private int membersCount;
    private double savingsTarget;
    private int savingsPeriod;  
    private String savingsFrequency;
    private String startDate;
    private String savingFor;
    private String groupCode;
    private double monthlySavingsPerMember;
    private ArrayList<String> members;  // List to store group members

    // Constructor
    public Group(String groupName, int membersCount, double savingsTarget, int savingsPeriod, String savingsFrequency,
                 String startDate, String savingFor, String groupCode, double monthlySavingsPerMember) {
        this.groupName = groupName;
        this.membersCount = membersCount;
        this.savingsTarget = savingsTarget;
        this.savingsPeriod = savingsPeriod;
        this.savingsFrequency = savingsFrequency;
        this.startDate = startDate;
        this.savingFor = savingFor;
        this.groupCode = groupCode;
        this.monthlySavingsPerMember = monthlySavingsPerMember;
        this.members = new ArrayList<>();
    }

    // Getters and setters
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

    public int getSavingsPeriod() {
        return savingsPeriod;
    }
    
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    // Getter method to retrieve the list of members
    public List<String> getMembers() {
        return this.members;  // Return the list of members
    }

    // Method to add a member to the group
    public void addMember(String memberName) {
        members.add(memberName);  // Add the member to the list
    }

    public int getMembersCount() {
		return membersCount;
	}

	public void setMembersCount(int membersCount) {
		this.membersCount = membersCount;
	}

	// Updated toString() method to include the members
    @Override
    public String toString() {
        return groupName + "," + groupCode + "," + membersCount + "," + savingsTarget + "," +
                savingsPeriod + "," + monthlySavingsPerMember + "," + savingsFrequency + "," + startDate + "," +
                savingFor + "," + String.join(";", members);  // Add members as a semicolon-separated list
    }
}
