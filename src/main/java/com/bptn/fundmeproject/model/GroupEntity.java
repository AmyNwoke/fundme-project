package com.bptn.fundmeproject.model;

public abstract class GroupEntity {
	protected String groupCode;

	public GroupEntity(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	@Override
	public abstract String toString();
}
