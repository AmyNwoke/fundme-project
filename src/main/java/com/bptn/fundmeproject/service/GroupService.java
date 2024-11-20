package com.bptn.fundmeproject.service;

import com.bptn.fundmeproject.model.Group;
//import java.io.*;
import java.util.*;
import com.bptn.fundmeproject.utility.DatabaseConnectionF;
import java.sql.*;
//import java.util.stream.Collectors;

public class GroupService {
	// file path to store and retrieve data
	// private static final String FILE_PATH = "groupData.csv";
	// map to save group in memory with groupcode as the key and group has value
	private Map<String, Group> groupMap = new HashMap<>();

	// This method adds a new group but checks if the member is already in a group
	// user can only join one group -file path

	/*
	 * public void addGroup(Group group, String creatorName) throws Exception { if
	 * (!isMemberInAnyGroup(creatorName)) { group.addMember(creatorName); // add
	 * member to group if not in any group groupMap.put(group.getGroupCode(),
	 * group); // store group saveToFile(); // save to file } else { // throw an
	 * exception if member is in a group throw new
	 * Exception("You can only belong to a single group"); }
	 * 
	 * 
	 * }
	 */
//db path 
	/*
	 * public void addGroup(Group group, String creatorName) throws Exception { if
	 * (!isMemberInAnyGroup(creatorName)) { group.addMember(creatorName); // Add
	 * creator as member to group saveGroupToDatabase(group, creatorName); // Pass
	 * creatorName to saveGroupToDatabase groupMap.put(group.getGroupCode(), group);
	 * // Update in-memory map
	 * 
	 * // Add creator to GROUP_MEMBERS table addCreatorToGroupMembers(group,
	 * creatorName); } else { throw new
	 * Exception("You can only belong to a single group"); } }
	 */
	public void addGroup(Group group, String creatorName) throws Exception {
		if (!isMemberInAnyGroup(creatorName)) {
			System.out.println("Adding group: " + group.getGroupName() + " with creator: " + creatorName);

			// Save the group to the GROUPS table
			saveGroupToDatabase(group, creatorName);
			System.out.println("Group saved to database with code: " + group.getGroupCode());

			// Add creator as the first member
			addCreatorToGroupMembers(group, creatorName);
			System.out.println("Creator added to GROUP_MEMBERS table as the first member.");

			// Update in-memory map
			groupMap.put(group.getGroupCode(), group);
		} else {
			throw new Exception("You can only belong to a single group");
		}
	}

	// used by fund savings & join group to find group when code is inputed by user
	/*
	 * public Group findGroupByCode(String groupCode) { if (groupMap.isEmpty()) {
	 * loadFromFile(); } return groupMap.get(groupCode); }
	 */
	// fing group by code database

	// Method to find a group by its code, using the in-memory map for fast lookup
	public Group findGroupByCode(String groupCode) {
		if (groupMap.isEmpty()) {
			loadGroupsFromDatabase(); // Populate map from database if empty
		}
		return groupMap.get(groupCode);
	}

	// this method is used when a user wants to join a group
	// check if the user has an existing group
	/*
	 * public void addMemberToGroup(String groupCode, String memberName) throws
	 * Exception { // check if member is in any group if
	 * (isMemberInAnyGroup(memberName)) { throw new
	 * Exception("You can only belong to a single group"); }
	 * 
	 * Group group = groupMap.get(groupCode); if (group == null) { throw new
	 * Exception("Group not found"); }
	 * 
	 * // Retrieve the group members and check if the group is already full
	 * List<String> members = group.getMembers(); // returns an obj type of string
	 * containing member list if (members.size() >= group.getMembersCount()) { throw
	 * new Exception("Group is already full, please join another group."); }
	 * group.addMember(memberName); // add member if group isn't full saveToFile();
	 * 
	 * System.out.println("Member " + memberName +
	 * " added to the group successfully.");
	 * 
	 * }
	 */

	// Method to add a member to a group both in the database and the in-memory map
	public void addMemberToGroup(String groupCode, String memberName) throws Exception {
		int groupId = getGroupIdByCode(groupCode); // Retrieve the groupId using the groupCode

		if (groupId == -1) {
			throw new Exception("Group not found for the provided groupCode.");
		}

		try (Connection conn = DatabaseConnectionF.getConnection()) {
			String sql = "INSERT INTO \"GROUP_MEMBERS\" (\"groupId\", \"userId\", \"groupCode\", \"memberName\") VALUES (?, ?, ?, ?)";

			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, groupId);
				pstmt.setInt(2, getUserIdByName(memberName)); // Fetches `userId` based on member name
				pstmt.setString(3, groupCode);
				pstmt.setString(4, memberName); // Set member name

				pstmt.executeUpdate();
				System.out.println("Member added to GROUP_MEMBERS table successfully.");
			}
		} catch (SQLException e) {
			System.out.println("Error adding member to GROUP_MEMBERS table.");
			e.printStackTrace();
		}
	}

	// method to find member group

	public Group findGroupForMember(String memberName) {
		if (groupMap.isEmpty()) {
			loadGroupsFromDatabase();
		}

		return groupMap.values().stream().filter(group -> group.getMembers().contains(memberName)).findFirst()
				.orElseGet(() -> findGroupForMemberInDatabase(memberName));
	}

	// method to find the group that a specific member belongs to
	// used in dashboard controller
	/*
	 * public Group findGroupForMember(String memberName) { if (groupMap.isEmpty())
	 * { loadFromFile(); }
	 * 
	 * // converts map to list so its easier to loop through and search for the
	 * member // toList didn't work for me hence why i used
	 * .collect(Collectors.toList) List<Group> consolidatedGroupList =
	 * groupMap.values().stream().collect(Collectors.toList());
	 * 
	 * // for each loop to check each group for the member for (Group group :
	 * consolidatedGroupList) { if (group.getMembers().contains(memberName)) {
	 * return group; }
	 * 
	 * } return null; }
	 */

	/*
	 * public Group findGroupForMember(String memberName) { if (groupMap.isEmpty())
	 * { loadFromFile(); }
	 * 
	 * return groupMap.values().stream() .filter(group ->
	 * group.getMembers().contains(memberName)) // Lambda to check member presence
	 * .findFirst() //finds the first group with member name .orElse(null); //
	 * Returns the first matching group or null if none found }
	 */

	/*
	 * public Group findGroupForMember(String memberName) { // Check if the
	 * in-memory map is empty; if so, load from the database if (groupMap.isEmpty())
	 * { loadGroupsFromDatabase(); }
	 * 
	 * // Look for the group containing the specified member return
	 * groupMap.values().stream() .filter(group ->
	 * group.getMembers().contains(memberName)) // Check if the member is part of
	 * the group .findFirst() // Find the first group with the specified member
	 * .orElseGet(() -> findGroupForMemberInDatabase(memberName)); // Fallback to
	 * database if not in cache }
	 */

	/*
	 * public boolean isMemberInAnyGroup(String memberName) { for (Group group :
	 * groupMap.values()) { // Iterate through each group in the map if
	 * (group.getMembers().contains(memberName)) { // Check if the member is in the
	 * group's members list return true; // Return true if the member is found } }
	 * return false; // Return false if the member is not found in any group }
	 */

	// db method
	// Method to check if a member is in any group using in-memory map for fast
	// lookup
	public boolean isMemberInAnyGroup(String memberName) {
		return groupMap.values().stream().anyMatch(group -> group.getMembers().contains(memberName));
	}

	/*
	 * private void saveToFile() { try { File file = new File(FILE_PATH);// creating
	 * file object to store group details
	 * 
	 * try (FileWriter writer = new FileWriter(file)) { // Write header
	 * 
	 * writer.write(
	 * "GroupName,GroupCode,NumOfMembers,TargetAmount,SavingsPeriod,MonthlyContribution,Frequency,StartDate,SavingFor,Members\n"
	 * );
	 * 
	 * // iterate through the details in the map // Write each group details to the
	 * file
	 * 
	 * for (Group group : groupMap.values()) { writer.write(group.toString() +
	 * "\n"); // Write each group using its toString() method }
	 * System.out.println("Groups saved successfully."); }
	 * 
	 * } catch (IOException e) { System.out.println("Error saving groups to file.");
	 * e.printStackTrace(); } }
	 */
	private void saveGroupToDatabase(Group group, String creatorName) {
		try (Connection conn = DatabaseConnectionF.getConnection()) {

			String sql = "INSERT INTO \"GROUPS\" (\"groupName\", \"groupCode\", \"numOfMembers\", \"targetAmount\", \"savingsPeriod\", \"monthlyContribution\", \"frequency\", \"startDate\", \"savingFor\", \"creatorName\", \"withdrawn\") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, group.getGroupName());
				pstmt.setString(2, group.getGroupCode());
				pstmt.setInt(3, group.getMembersCount());
				pstmt.setDouble(4, group.getSavingsTarget());
				pstmt.setInt(5, group.getSavingsPeriod());
				pstmt.setDouble(6, group.getMonthlySavingsPerMember());
				pstmt.setString(7, group.getSavingsFrequency());

				// Convert startDate from String to java.sql.Date
				java.sql.Date sqlDate = java.sql.Date.valueOf(group.getStartDate());
				pstmt.setDate(8, sqlDate);

				pstmt.setString(9, group.getSavingFor());
				pstmt.setString(10, creatorName); // Pass creatorName directly here
				pstmt.setBoolean(11, group.isWithdrawn());
				pstmt.executeUpdate();
			}
			System.out.println("Group saved to database successfully.");
		} catch (SQLException e) {
			System.out.println("Error saving group to database.");
			e.printStackTrace();
		}
	}

	// Method to update group members in the database
	// Method to update group members in the database
	/*
	 * private void updateGroupMembersInDatabase(Group group) { int groupId =
	 * getGroupIdByCode(group.getGroupCode()); // Retrieve the groupId using the
	 * groupCode
	 * 
	 * if (groupId == -1) {
	 * System.out.println("Group not found for the provided groupCode."); return; //
	 * Exit if groupId is not found }
	 * 
	 * try (Connection conn = DatabaseConnectionF.getConnection()) { String sql =
	 * "INSERT INTO \"GROUP_MEMBERS\" (\"groupId\", \"userId\", \"groupCode\", \"memberName\") VALUES (?, ?, ?, ?)"
	 * ;
	 * 
	 * try (PreparedStatement pstmt = conn.prepareStatement(sql)) { for (String
	 * member : group.getMembers()) { int userId = getUserIdByName(member); //
	 * Assuming getUserIdByName fetches userId based on member name if (userId ==
	 * -1) { System.out.println("User not found for the member: " + member);
	 * continue; // Skip if userId is not found }
	 * 
	 * pstmt.setInt(1, groupId); // Set groupId pstmt.setInt(2, userId); // Set
	 * userId pstmt.setString(3, group.getGroupCode()); // Set groupCode
	 * pstmt.setString(4, member); // Set memberName pstmt.addBatch(); // Add to
	 * batch for efficiency } pstmt.executeBatch(); // Execute the batch }
	 * System.out.println("Group members updated in database successfully."); }
	 * catch (SQLException e) {
	 * System.out.println("Error updating group members in database.");
	 * e.printStackTrace(); } }
	 */

	// Method to load members for a specific group from the database
	// Method to load members for a specific group from the database
	private void loadGroupMembersFromDatabase(Group group) {
		try (Connection conn = DatabaseConnectionF.getConnection()) {
			String sql = "SELECT \"memberName\" FROM \"GROUP_MEMBERS\" WHERE \"groupCode\" = ?";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, group.getGroupCode());
				try (ResultSet rs = pstmt.executeQuery()) {
					while (rs.next()) {
						// Use rs.getString("memberName") without extra quotes
						group.addMember(rs.getString("memberName"));
					}
				}
			}
			System.out.println("Members loaded from database for group: " + group.getGroupCode());
		} catch (SQLException e) {
			System.out.println("Error loading group members from database.");
			e.printStackTrace();
		}
	}

	private void loadGroupsFromDatabase() {
		try (Connection conn = DatabaseConnectionF.getConnection()) {
			String sql = "SELECT * FROM \"GROUPS\"";
			try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
				while (rs.next()) {
					Group group = new Group(rs.getString("groupName"), rs.getInt("numOfMembers"),
							rs.getDouble("targetAmount"), rs.getInt("savingsPeriod"), rs.getString("frequency"),
							rs.getString("startDate"), rs.getString("savingFor"), rs.getString("groupCode"),
							rs.getDouble("monthlyContribution"), rs.getBoolean("withdrawn")// Load withdrawn status

					);
					loadGroupMembersFromDatabase(group);
					groupMap.put(group.getGroupCode(), group);
				}
			}
		} catch (SQLException e) {
			System.out.println("Error loading groups from database.");
			e.printStackTrace();
		}
	}

	private Group findGroupForMemberInDatabase(String memberName) {
		try (Connection conn = DatabaseConnectionF.getConnection()) {
			String sql = "SELECT g.\"groupName\", g.\"groupCode\", g.\"numOfMembers\", g.\"targetAmount\", g.\"savingsPeriod\", "
					+ "g.\"monthlyContribution\", g.\"frequency\", g.\"startDate\", g.\"savingFor\", g.\"withdrawn\" " + // Add
																															// creatorName
					"FROM \"GROUPS\" g " + "JOIN \"GROUP_MEMBERS\" gm ON g.\"groupCode\" = gm.\"groupCode\" "
					+ "WHERE gm.\"memberName\" = ?";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, memberName);
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						Group group = new Group(rs.getString("groupName"), rs.getInt("numOfMembers"),
								rs.getDouble("targetAmount"), rs.getInt("savingsPeriod"), rs.getString("frequency"),
								rs.getString("startDate"), rs.getString("savingFor"), rs.getString("groupCode"),
								rs.getDouble("monthlyContribution"), rs.getBoolean("withdrawn")

						);
						loadGroupMembersFromDatabase(group);
						groupMap.put(group.getGroupCode(), group);
						return group;
					}
				}
			}
		} catch (SQLException e) {
			System.out.println("Error finding group for member in database.");
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * private void addCreatorToGroupMembers(Group group, String creatorName) { int
	 * groupId = getGroupIdByCode(group.getGroupCode()); // Retrieve group ID using
	 * the group code
	 * 
	 * try (Connection conn = DatabaseConnectionF.getConnection()) { String sql =
	 * "INSERT INTO \"GROUP_MEMBERS\" (\"groupId\", \"userId\", \"groupCode\", \"memberName\") VALUES (?, ?, ?, ?)"
	 * ;
	 * 
	 * try (PreparedStatement pstmt = conn.prepareStatement(sql)) { pstmt.setInt(1,
	 * groupId); // Set groupId pstmt.setInt(2, getUserIdByName(creatorName)); //
	 * Retrieve and set creator's userId pstmt.setString(3, group.getGroupCode());
	 * // Set groupCode pstmt.setString(4, creatorName); // Set memberName as
	 * creatorName pstmt.executeUpdate(); }
	 * System.out.println("Creator added to GROUP_MEMBERS table successfully."); }
	 * catch (SQLException e) {
	 * System.out.println("Error adding creator to GROUP_MEMBERS table.");
	 * e.printStackTrace(); } }
	 */
	private void addCreatorToGroupMembers(Group group, String creatorName) {
		int groupId = getGroupIdByCode(group.getGroupCode());
		int userId = getUserIdByName(creatorName);

		// Check if IDs are correctly fetched
		if (groupId == -1) {
			System.out.println("Error: Group ID not found for group code: " + group.getGroupCode());
			return;
		}
		if (userId == -1) {
			System.out.println("Error: User ID not found for creator name: " + creatorName);
			return;
		}

		try (Connection conn = DatabaseConnectionF.getConnection()) {
			String sql = "INSERT INTO \"GROUP_MEMBERS\" (\"groupId\", \"userId\", \"groupCode\", \"memberName\") VALUES (?, ?, ?, ?)";

			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, groupId);
				pstmt.setInt(2, userId);
				pstmt.setString(3, group.getGroupCode());
				pstmt.setString(4, creatorName);
				pstmt.executeUpdate();
				System.out.println("Creator added to GROUP_MEMBERS table successfully with group ID: " + groupId
						+ " and user ID: " + userId);
			}
		} catch (SQLException e) {
			System.out.println("Error adding creator to GROUP_MEMBERS table.");
			e.printStackTrace();
		}
	}

	private int getUserIdByName(String userName) {
		int userId = -1; // Default to -1 to indicate failure if not found
		try (Connection conn = DatabaseConnectionF.getConnection()) {
			String sql = "SELECT \"userId\" FROM \"USERS\" WHERE \"name\" = ?";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, userName);
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						userId = rs.getInt("userId");
					}
				}
			}
		} catch (SQLException e) {
			System.out.println("Error retrieving userId by userName.");
			e.printStackTrace();
		}
		return userId;
	}

	private int getGroupIdByCode(String groupCode) {
		int groupId = -1; // Default to -1 to indicate failure if not found
		try (Connection conn = DatabaseConnectionF.getConnection()) {
			String sql = "SELECT \"groupId\" FROM \"GROUPS\" WHERE \"groupCode\" = ?";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, groupCode);
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						groupId = rs.getInt("groupId");
					}
				}
			}
		} catch (SQLException e) {
			System.out.println("Error retrieving groupId by groupCode.");
			e.printStackTrace();
		}
		return groupId;
	}

	public void updateWithdrawalStatus(String groupCode, boolean status) {
		String sql = "UPDATE \"GROUPS\" SET \"withdrawn\" = ? WHERE \"groupCode\" = ?";
		try (Connection conn = DatabaseConnectionF.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setBoolean(1, status);
			pstmt.setString(2, groupCode);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<String> getGroupEmailsByCode(String groupCode) {
		List<String> emails = new ArrayList<>();
		String sql = "SELECT u.email FROM \"USERS\" u JOIN \"GROUP_MEMBERS\" gm ON u.\"userId\" = gm.\"userId\" WHERE gm.\"groupCode\" = ?";

		try (Connection conn = DatabaseConnectionF.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, groupCode);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					emails.add(rs.getString("email"));
				}
			}
		} catch (SQLException e) {
			System.out.println("Error retrieving group member emails.");
			e.printStackTrace();
		}
		return emails;
	}

	public boolean isGroupCreator(String groupCode, String userName) {
		try (Connection conn = DatabaseConnectionF.getConnection()) {
			String sql = "SELECT \"creatorName\" FROM \"GROUPS\" WHERE \"groupCode\" = ?";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, groupCode);
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						return rs.getString("creatorName").equals(userName);
					}
				}
			}
		} catch (SQLException e) {
			System.out.println("Error verifying group creator.");
			e.printStackTrace();
		}
		return false; // Return false if not found or error occurs
	}

	/*
	 * private void loadFromFile() { File file = new File(FILE_PATH); if
	 * (!file.exists()) { System.out.println("File not found: " + FILE_PATH);
	 * return; // Exit the method if the file does not exist } else if
	 * (file.length() == 0) { System.out.println("File is empty: " + FILE_PATH);
	 * return; // Exit the method if the file is empty // the above ensures we dont
	 * read from a file that doesnt exist or is empty }
	 * 
	 * // read from file try (BufferedReader reader = new BufferedReader(new
	 * FileReader(FILE_PATH))) { String line; reader.readLine(); // skip the header
	 * row // loop to read line till there is no more line left to read while ((line
	 * = reader.readLine()) != null) { String[] groupDetails = line.split(","); //
	 * Assuming the group CSV follows the order of your toString method String
	 * groupName = groupDetails[0]; String groupCode = groupDetails[1]; int
	 * membersCount = Integer.parseInt(groupDetails[2]); double savingsTarget =
	 * Double.parseDouble(groupDetails[3]); int savingsPeriod =
	 * Integer.parseInt(groupDetails[4]); double monthlySavingsPerMember =
	 * Double.parseDouble(groupDetails[5]); String savingsFrequency =
	 * groupDetails[6]; String startDate = groupDetails[7]; String savingFor =
	 * groupDetails[8]; List<String> members = new
	 * ArrayList<>(Arrays.asList(groupDetails[9].split(";"))); // Split members list
	 * // Re-create the group Group group = new Group(groupName, membersCount,
	 * savingsTarget, savingsPeriod, savingsFrequency, startDate, savingFor,
	 * groupCode, monthlySavingsPerMember); group.getMembers().addAll(members); //
	 * Add members to the group groupMap.put(groupCode, group); // store the group
	 * obj to the in-memory list } } catch (IOException e) { e.printStackTrace(); }
	 * }
	 */
}