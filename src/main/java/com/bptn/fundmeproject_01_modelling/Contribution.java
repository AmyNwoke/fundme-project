package com.bptn.fundmeproject_01_modelling;

public class Contribution {
	private String groupCode;
	private String member;
	private double amountContributed;
	private String currentDate;

	public Contribution(String groupCode, String member, double amountContributed, String currentDate) {
		this.groupCode = groupCode;
		this.member = member;
		this.amountContributed = amountContributed;
		this.currentDate = currentDate;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getMember() {
		return member;
	}

	public void setMember(String member) {
		this.member = member;
	}

	public double getAmountContributed() {
		return amountContributed;
	}

	public void setAmountContributed(double amountContributed) {
		this.amountContributed = amountContributed;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	@Override
	public String toString() {
		return groupCode + "," + member + "," + amountContributed + "," + currentDate;
	}
}
