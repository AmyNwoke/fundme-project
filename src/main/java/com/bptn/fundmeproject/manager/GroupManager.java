package com.bptn.fundmeproject.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import com.bptn.fundmeproject.model.*;
import com.bptn.fundmeproject.service.GroupService;
import com.bptn.fundmeproject.service.SavingsContributionService;
import com.bptn.fundmeproject.utility.DatabaseConnectionF;

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
		//savingsContributionService.initializeSavingsProgress(group, creatorName);
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

/*	// used by dashboard to display the total savings progress
	public SavingsProgress getTotalSavingsProgressForGroup(String groupCode) {
		return savingsContributionService.getTotalSavingsProgressForGroup(groupCode);
	}
	*/
	
	// Used by dashboard to display the total savings for the group
    public double getTotalSavingsForGroup(String groupCode) { // Updated method call
        return savingsContributionService.getTotalSavingsForGroup(groupCode); // Updated to getTotalSavingsForGroup
    }
    
    public void updateWithdrawalStatus(String groupCode, boolean withdrawnStatus) {
        groupService.updateWithdrawalStatus(groupCode, withdrawnStatus);
    }
    
    public List<String> getGroupEmailsByCode(String groupCode) {
        return groupService.getGroupEmailsByCode(groupCode);
    }

 // Method to check if the user is the creator of the group
    public boolean isGroupCreator(String groupCode, String userName) {
        return groupService.isGroupCreator(groupCode, userName);
    }
    
    public void recordWithdrawal(String groupCode, String interacEmail) {
        double totalSavings = getTotalSavingsForGroup(groupCode);

        String sql = "INSERT INTO \"WITHDRAWALS\" (\"groupCode\", \"withdrawalAmount\", \"withdrawalDate\", \"interacEmail\") VALUES (?, ?, CURRENT_DATE, ?)";
        try (Connection conn = DatabaseConnectionF.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, groupCode);  // Use groupCode as the identifier
            pstmt.setDouble(2, totalSavings);
            pstmt.setString(3, interacEmail);
            pstmt.executeUpdate();
            System.out.println("Withdrawal recorded successfully.");
        } catch (SQLException e) {
            System.out.println("Error recording withdrawal.");
            e.printStackTrace();
        }
    }



}
