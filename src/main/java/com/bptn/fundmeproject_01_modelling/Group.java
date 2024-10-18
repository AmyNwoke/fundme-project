package com.bptn.fundmeproject_01_modelling;

	
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

	    // Override toString() method to display group details
	    @Override
	    public String toString() {
	        return groupName + "," + groupCode + "," + membersCount + "," + savingsTarget + "," +
	                savingsPeriod + "," + monthlySavingsPerMember + "," + savingsFrequency + "," + startDate + "," +
	                savingFor;
	    }
	}



