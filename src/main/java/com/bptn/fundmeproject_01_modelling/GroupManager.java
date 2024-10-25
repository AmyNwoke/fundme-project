package com.bptn.fundmeproject_01_modelling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class GroupManager {
	private static Map<String, Group> groupList = new HashMap<>(); //declaration of grouplist to save in memory
	private static Map<String, List<Contribution>> contributionMap = new HashMap<>(); //declaration of contribution list 
	private static Map<String, SavingsProgress> savingsProgressMap = new HashMap<>();   //declaring savings progress
	// Singleton instance
	//creating an instance of group manager 
	private static GroupManager instance;
	private static final String FILE_PATH = "groupData.csv";
	private static final String CONTRIBUTION_FILE_PATH = "contributionData.csv";
	private static final String SAVINGS_PROGRESS_FILE_PATH = "savingsProgress.csv";

	// Private constructor to prevent instantiation
	private GroupManager() { //ensuring that no other class can create an instance of this class
	}

	// Static method to return the single instance of GroupManager
	public static GroupManager getInstance() {
		if (instance == null) {
			instance = new GroupManager();
		}
		return instance;
	}

	// Method to add a group to the list and save to file, with creator as the first
	// member
	public void addGroup(Group group, String creatorName) { //method declaration to add group
		// Add the creator as the first member
		group.addMember(creatorName); // calling the add member method Automatically add the creator to the group as a member
		groupList.put(group.getGroupCode(), group); // // Adding the group to the groupList map, with the group code as the key and the group object as the value
		System.out.println("group size = " + groupList.size() + "\n group enteries = " + groupList); //printing the current size for debugging
		saveToGroupFile(); //save the updated list to file

		SavingsProgress savingsProgress = new SavingsProgress(group.getGroupCode(), group.getSavingsTarget(), 0, 0); //Create a new SavingsProgress object for the group with the initial savings progress
		savingsProgress.addMember(creatorName); //add the creator of the group as a contributing member to the savings progress
		System.out.println("savings progress = " + savingsProgress.toString());
		savingsProgressMap.put(group.getGroupCode(), savingsProgress); 
		updateGroupSavingsProgress(); //update overall progress for all group
		// saveGroupToFile(group); // Save the group details to the CSV file
		System.out.println("Group created by " + creatorName + " and added to the group.");
	}

	// Method to record contributions and save them to the CSV file
	public void recordContribution(String groupCode, String memberName, double amount) throws Exception {
		// Load contributions and savings progress from the file if the contribution map
		// is empty
		if (contributionMap.isEmpty()) {
			loadContributionsFromFile();
			loadSavingsProgressFromFile();
		}

		// Check if the group's savings target has been reached
		if (groupSavingsTargetReached(groupCode)) {
			throw new Exception("The group has reached its saving's target");
		}

		// Get the list of contributions for the group
		List<Contribution> contributionList = contributionMap.get(groupCode);

		// Check if the member has already contributed
		if (contributionList != null && memberContributionExists(contributionList, memberName)) {
			throw new Exception("You have met your contribution quota");
		} else {
			// If no contributions exist for this group, create a new list for the group
			if (contributionList == null) {
				contributionList = new ArrayList<>(); // Create a new list
				contributionMap.put(groupCode, contributionList); // Add it to the contribution map groupcode key and contribution list value
			}

			// Add the new contribution to the list
			contributionList.add(new Contribution(groupCode, memberName, amount, LocalDate.now().toString()));

			// Update the savings progress for the group
			savingsProgressMap.get(groupCode).updateTotalSavings(amount);
			System.out.println("Updated savings progress: " + savingsProgressMap);

			// Save the updated contributions and savings progress to the file
			saveToContributionFile();
			updateGroupSavingsProgress();
		}
	}

	public boolean memberContributionExists(List<Contribution> contributionList, String memberName) {
		if (contributionList == null) {
			return false;
		}
		for (Contribution contribution : contributionList) {
			if (contribution.getMember() == memberName) {
				return true;
			}
		}

		return false;
	}

	private boolean groupSavingsTargetReached(String groupCode) {
		SavingsProgress savingsProgress = savingsProgressMap.get(groupCode);
		if (savingsProgress != null) {
			if (savingsProgress.getTotalSavings() >= savingsProgress.getTargetSavings()) {
				return true;
			}
		}
		return false;
	}

	public void saveToContributionFile() {
		List<Contribution> consolidatedList = contributionMap.values().stream().flatMap(List::stream)
				.collect(Collectors.toList());
		try {
			File file = new File(CONTRIBUTION_FILE_PATH);
			try (FileWriter writer = new FileWriter(file)) {
				writer.write("GroupCode,MemberName,Amount,ContributionDate\n");

				for (Contribution contribution : consolidatedList) {
					writer.write(contribution.toString() + "\n"); // Write each group using its toString() method
				}
				System.out.println("Contribution record saved successfully.");
			}

		} catch (IOException e) {
			System.out.println("Error saving groups to file.");
			e.printStackTrace();
		}
	}

	private void updateGroupSavingsProgress() {
		try {
			File file = new File(SAVINGS_PROGRESS_FILE_PATH);
			try (FileWriter writer = new FileWriter(file)) {
				writer.write("GroupCode,TargetSavings,TotalSavings,PercentageProgress,ContributingMembers\n");

				for (SavingsProgress progress : savingsProgressMap.values()) {
					writer.write(progress.toString() + "\n"); // Write each group using its toString() method
				}
				System.out.println("Progress saved successfully.");
			}

		} catch (IOException e) {
			System.out.println("Error saving progress to file.");
			e.printStackTrace();
		}
	}

	// Method to find a group by group code
	public Group findGroupByCode(String groupCode) {
		if (groupList.isEmpty()) {
			loadGroupsFromFile();
		}
		System.out.println("findGroupByCode calls loadGroupsFromFile: group size = " + groupList.size()
				+ "\n group enteries = " + groupList);
		return groupList.get(groupCode);
	}

	// Method to find a group by group code
	public Group findGroupForMember(String memberName) {
		if (groupList.isEmpty()) {
			loadGroupsFromFile();
		}
		List<Group> consolidatedGroupList = groupList.values().stream().collect(Collectors.toList());

		for (Group group : consolidatedGroupList) {
			if (group.getMembers().contains(memberName)) {
				return group;
			}

		}
		return null;
	}

	public void addMemberToGroup(String groupCode, String memberName) throws Exception {
		// Find the group by its code
		Group group = groupList.get(groupCode);

		// Check if the group exists
		if (group == null) {
			throw new Exception("Group with code " + groupCode + " not found.");
		}

		// Retrieve the group members and check if the group is already full
		List<String> members = group.getMembers();
		if (members.size() >= group.getMembersCount()) {
			throw new Exception("Group is already full, please join another group.");
		}

		// Check if the member is already in the group
		if (members.contains(memberName)) {
			throw new Exception(memberName + " is already a member of this group.");
		}

		// Add the new member to the group
		group.addMember(memberName);

		// Save the updated group details to the file
		saveToGroupFile();

		System.out.println("Member " + memberName + " added to the group successfully.");
	}

	// Method to load all groups from a file
	public void loadGroupsFromFile() {
		System.out.println("loadSavingsProgressFromFile methos");

		File file = new File(FILE_PATH);
		if (!file.exists()) {
			System.out.println("File not found: " + FILE_PATH);
			return; // Exit the method if the file does not exist
		} else if (file.length() == 0) {
			System.out.println("File is empty: " + FILE_PATH);
			return; // Exit the method if the file is empty
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
			String line;
			reader.readLine();
			while ((line = reader.readLine()) != null) {
				String[] groupDetails = line.split(",");
				// Assuming the group CSV follows the order of your toString method
				String groupName = groupDetails[0];
				String groupCode = groupDetails[1];
				int membersCount = Integer.parseInt(groupDetails[2]);
				double savingsTarget = Double.parseDouble(groupDetails[3]);
				int savingsPeriod = Integer.parseInt(groupDetails[4]);
				double monthlySavingsPerMember = Double.parseDouble(groupDetails[5]);
				String savingsFrequency = groupDetails[6];
				String startDate = groupDetails[7];
				String savingFor = groupDetails[8];
				List<String> members = new ArrayList<>(Arrays.asList(groupDetails[9].split(";"))); // Split members list
				// Re-create the group
				Group group = new Group(groupName, membersCount, savingsTarget, savingsPeriod, savingsFrequency,
						startDate, savingFor, groupCode, monthlySavingsPerMember);
				group.getMembers().addAll(members); // Add members to the group
				groupList.put(groupCode, group); // Add the group to the in-memory list
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Contribution> getContributionList(String groupCode) {
		if (contributionMap.isEmpty()) {
			loadContributionsFromFile();
		}
		return (contributionMap.get(groupCode));
	}

	public SavingsProgress getTotalSavingsProgressForGroup(String groupCode) {
		if (savingsProgressMap.isEmpty()) {
			loadSavingsProgressFromFile();
		}
		return (savingsProgressMap.get(groupCode));
	}

	public void loadContributionsFromFile() {
		System.out.println("loadSavingsProgressFromFile methos");

		File file = new File(CONTRIBUTION_FILE_PATH);
		if (!file.exists()) {
			System.out.println("File not found: " + CONTRIBUTION_FILE_PATH);
			return;
		} else if (file.length() == 0) {
			System.out.println("File is empty: " + CONTRIBUTION_FILE_PATH);
			return;
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(CONTRIBUTION_FILE_PATH))) {
			String line;
			reader.readLine();
			while ((line = reader.readLine()) != null) {
				String[] contributionDetails = line.split(",");
				String groupCode = contributionDetails[0];
				String member = contributionDetails[1];
				double amountContributed = Double.parseDouble(contributionDetails[2]);
				String currentDate = contributionDetails[3];

				Contribution contribution = new Contribution(groupCode, member, amountContributed, currentDate);
				System.out.println("contribution from file : " + contribution.toString());

				if (contributionMap.containsKey(groupCode)) {
					// If the key exists, add the new contribution to the existing list
					contributionMap.get(groupCode).add(contribution);
				} else {
					// If the key does not exist, create a new list, add the contribution, and put
					// it in the map
					List<Contribution> newContributionList = new ArrayList<>();
					newContributionList.add(contribution);
					contributionMap.put(groupCode, newContributionList);
				} // Add the contribution to the in-memory list
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadSavingsProgressFromFile() {
		System.out.println("loadSavingsProgressFromFile methos");

		File file = new File(SAVINGS_PROGRESS_FILE_PATH);
		if (!file.exists()) {
			System.out.println("File not found: " + SAVINGS_PROGRESS_FILE_PATH);
			return; // Exit the method if the file does not exist
		} else if (file.length() == 0) {
			System.out.println("File is empty: " + SAVINGS_PROGRESS_FILE_PATH);
			return; // Exit the method if the file is empty
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(SAVINGS_PROGRESS_FILE_PATH))) {
			String line;
			reader.readLine();
			while ((line = reader.readLine()) != null) {
				String[] savingsProgressDetails = line.split(",");
				String groupCode = savingsProgressDetails[0];
				double targetSavings = Double.parseDouble(savingsProgressDetails[1]);
				double totalSavings = Double.parseDouble(savingsProgressDetails[2]);
				double percentageProgress = Double.parseDouble(savingsProgressDetails[3]);
				List<String> contributingMembers = new ArrayList<>(Arrays.asList(savingsProgressDetails[4].split(";"))); // Split
																															// members
																															// list
				// Re-create the group
				SavingsProgress savingsProgress = new SavingsProgress(groupCode, targetSavings, totalSavings,
						percentageProgress);
				savingsProgress.getContributingMembers().addAll(contributingMembers);
				System.out.println("savings progress from file: " + savingsProgress.toString());

				savingsProgressMap.put(groupCode, savingsProgress);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void saveToGroupFile() {
		try {
			File file = new File(FILE_PATH);
			// Check if file exists and is not empty before appending new lines
			// boolean isEmptyFile = file.length() == 0;
			try (FileWriter writer = new FileWriter(file)) {
				// Write header

				writer.write(
						"GroupName,GroupCode,NumOfMembers,TargetAmount,SavingsPeriod,MonthlyContribution,Frequency,StartDate,SavingFor,Members\n");

				// Write each group to the file
				for (Group group : groupList.values()) {
					writer.write(group.toString() + "\n"); // Write each group using its toString() method
				}
				System.out.println("Groups saved successfully.");
			}

		} catch (IOException e) {
			System.out.println("Error saving groups to file.");
			e.printStackTrace();
		}
	}
}
