package com.bptn.fundmeproject.model;

public class Contribution extends GroupEntity {

	private String member;
	private double amountContributed;
	private String currentDate;

	public Contribution(String groupCode, String member, double amountContributed, String currentDate) {
		super(groupCode); //
		this.member = member;
		this.amountContributed = amountContributed;
		this.currentDate = currentDate;
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
