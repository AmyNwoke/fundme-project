package com.bptn.fundmeproject.manager;

import java.util.List;
import com.bptn.fundmeproject.model.*;
import com.bptn.fundmeproject.service.GroupService;
import com.bptn.fundmeproject.service.SavingsContributionService;

public class GroupManager {
	// Singleton instance
	// creating an instance of group manager
	private static GroupManager instance;

	private GroupService groupService;
	private SavingsContributionService savingsContributionService;

	// Private constructor to prevent instantiation
	private GroupManager() {
		this.groupService = new GroupService();
		this.savingsContributionService = new SavingsContributionService();// ensuring that no other class can create an
																			// instance of this class
	}

	// Static method to provide access and return the single instance of
	// GroupManager
	public static GroupManager getInstance() {
		if (instance == null) {
			instance = new GroupManager();
		}
		return instance;
	}

	public void addGroup(Group group, String creatorName) throws Exception {
		groupService.addGroup(group, creatorName);
		savingsContributionService.initializeSavingsProgress(group, creatorName);
	}

	// calling method to find group by code
	public Group findGroupByCode(String groupCode) {
		return groupService.findGroupByCode(groupCode);
	}

	// Method to find member group
	public Group findGroupForMember(String memberName) {
		return groupService.findGroupForMember(memberName);
	}

	// call the addmember to group from the controller to add a member
	public void addMemberToGroup(String groupCode, String memberName) throws Exception {
		groupService.addMemberToGroup(groupCode, memberName);
	}

	// Method to record contributions and save them to the CSV file
	public void recordContribution(String groupCode, String memberName, double amount) throws Exception {
		savingsContributionService.recordContribution(groupCode, memberName, amount);
	}

	// used by dashboard to get the list of contribution
	public List<Contribution> getContributionList(String groupCode) {
		return savingsContributionService.getContributionList(groupCode);
	}

	// used by dashboard to display the total savings progress
	public SavingsProgress getTotalSavingsProgressForGroup(String groupCode) {
		return savingsContributionService.getTotalSavingsProgressForGroup(groupCode);
	}
}
