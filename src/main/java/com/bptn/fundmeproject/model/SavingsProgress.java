package com.bptn.fundmeproject.model;

import java.util.ArrayList;
import java.util.List;

public class SavingsProgress extends GroupEntity  {

	//private String groupCode;
	private double targetSavings;
	private double totalSavings;
	private double percentageProgress;
	private List<String> contributingMembers;

	public SavingsProgress(String groupCode, double targetSavings, double totalSavings, double percentageProgress) {
		super(groupCode); //
		//this.groupCode = groupCode;
		this.targetSavings = targetSavings;
		this.totalSavings = totalSavings;
		this.percentageProgress = percentageProgress;
		this.contributingMembers = new ArrayList<>();
	}

//	public String getGroupCode() {
	//	return groupCode;
	//}

	//public void setGroupCode(String groupCode) {
		//this.groupCode = groupCode;
	//}

	public double getTargetSavings() {
		return targetSavings;
	}

	public void setTargetSavings(double targetSavings) {
		this.targetSavings = targetSavings;
	}

	public double getTotalSavings() {
		return totalSavings;
	}

	public void updateTotalSavings(double totalSavings) {
		this.totalSavings += totalSavings;
	}

	public List<String> getContributingMembers() {
		return contributingMembers;
	}

	public void addMember(String contributingMembers) {
		this.contributingMembers.add(contributingMembers);
	}

	public double getPercentageProgress() {
		return percentageProgress;
	}

	public void calculatePercentage() {
		this.percentageProgress = (this.totalSavings / this.targetSavings) * 100;
	}

	@Override
	public String toString() {
		return groupCode + "," + targetSavings + "," + totalSavings + "," + percentageProgress + ","
				+ String.join(";", contributingMembers);
	}

}
