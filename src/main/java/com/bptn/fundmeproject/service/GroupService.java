package com.bptn.fundmeproject.service;

import com.bptn.fundmeproject.model.Group;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class GroupService {
	// file path to store and retrieve data
	private static final String FILE_PATH = "groupData.csv";
	// map to save group in memory with groupcode as the key and group has value
	private Map<String, Group> groupMap = new HashMap<>();

	// This method adds a new group but checks if the member is already in a group
	// user can only join one group

	public void addGroup(Group group, String creatorName) throws Exception {
		if (!isMemberInAnyGroup(creatorName)) {
			group.addMember(creatorName); // add member to group if not in any group
			groupMap.put(group.getGroupCode(), group); // store group
			saveToFile(); // save to file
		} else { // throw an exception if member is in a group
			throw new Exception("You can only belong to a single group");
		}

	}

	// used by fund savings & join group to find group when code is inputed by user
	public Group findGroupByCode(String groupCode) {
		if (groupMap.isEmpty()) {
			loadFromFile();
		}
		return groupMap.get(groupCode);
	}

	// this method is used when a user wants to join a group
	// check if the user has an existing group
	public void addMemberToGroup(String groupCode, String memberName) throws Exception {
		// check if member is in any group
		if (isMemberInAnyGroup(memberName)) {
			throw new Exception("You can only belong to a single group");
		}

		Group group = groupMap.get(groupCode);
		if (group == null) {
			throw new Exception("Group not found");
		}

		// Retrieve the group members and check if the group is already full
		List<String> members = group.getMembers(); // returns an obj type of string containing member list
		if (members.size() >= group.getMembersCount()) {
			throw new Exception("Group is already full, please join another group.");
		}
		group.addMember(memberName); // add member if group isn't full
		saveToFile();

		System.out.println("Member " + memberName + " added to the group successfully.");

	}

	// method to find the group that a specific member belongs to
	// used in dashboard controller
	/*public Group findGroupForMember(String memberName) {
		if (groupMap.isEmpty()) {
			loadFromFile();
		}

		// converts map to list so its easier to loop through and search for the member
		// toList didn't work for me hence why i used .collect(Collectors.toList)
		List<Group> consolidatedGroupList = groupMap.values().stream().collect(Collectors.toList());

		// for each loop to check each group for the member
		for (Group group : consolidatedGroupList) {
			if (group.getMembers().contains(memberName)) {
				return group;
			}

		}
		return null;
	}
*/
	
	public Group findGroupForMember(String memberName) {
	    if (groupMap.isEmpty()) {
	        loadFromFile();
	    }

	    return groupMap.values().stream()
	        .filter(group -> group.getMembers().contains(memberName)) // Lambda to check member presence
	        .findFirst()  //finds the first group with member name
	        .orElse(null); // Returns the first matching group or null if none found
	}

	public boolean isMemberInAnyGroup(String memberName) {
		for (Group group : groupMap.values()) { // Iterate through each group in the map
			if (group.getMembers().contains(memberName)) { // Check if the member is in the group's members list
				return true; // Return true if the member is found
			}
		}
		return false; // Return false if the member is not found in any group
	}

	private void saveToFile() {
		try {
			File file = new File(FILE_PATH);// creating file object to store group details

			try (FileWriter writer = new FileWriter(file)) {
				// Write header

				writer.write(
						"GroupName,GroupCode,NumOfMembers,TargetAmount,SavingsPeriod,MonthlyContribution,Frequency,StartDate,SavingFor,Members\n");

				// iterate through the details in the map
				// Write each group details to the file

				for (Group group : groupMap.values()) {
					writer.write(group.toString() + "\n"); // Write each group using its toString() method
				}
				System.out.println("Groups saved successfully.");
			}

		} catch (IOException e) {
			System.out.println("Error saving groups to file.");
			e.printStackTrace();
		}
	}

	private void loadFromFile() {
		File file = new File(FILE_PATH);
		if (!file.exists()) {
			System.out.println("File not found: " + FILE_PATH);
			return; // Exit the method if the file does not exist
		} else if (file.length() == 0) {
			System.out.println("File is empty: " + FILE_PATH);
			return; // Exit the method if the file is empty
			// the above ensures we dont read from a file that doesnt exist or is empty
		}

		// read from file
		try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
			String line;
			reader.readLine(); // skip the header row
			// loop to read line till there is no more line left to read
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
				groupMap.put(groupCode, group); // store the group obj to the in-memory list
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}