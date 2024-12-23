package com.bptn.fundmeproject.service;

import com.bptn.fundmeproject.model.Contribution;
import com.bptn.fundmeproject.model.Group;
//import com.bptn.fundmeproject.model.SavingsProgress;
import com.bptn.fundmeproject.utility.DatabaseConnectionF;

//import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class SavingsContributionService {
	// File path and map to store savings progress and contribution
	// private static final String CONTRIBUTION_FILE_PATH = "contributionData.csv";
	private Map<String, List<Contribution>> contributionMap = new HashMap<>();
//	private static final String SAVINGS_PROGRESS_FILE_PATH = "savingsProgress.csv";
//	private Map<String, SavingsProgress> savingsProgressMap = new HashMap<>();

	// used to create a new savings progress entry for the group
	// add member to the group progress

	// Initialize a new entry for contributions (if needed for setup)
	public void initializeContribution(Group group, String memberName) {
		// This can be expanded if any setup is required initially
		loadContributionsFromDatabase();
	}

	/*
	 * public void initializeSavingsProgress(Group group, String memberName) { //
	 * create savings progress object, setting contri & target to 0,0%
	 * SavingsProgress progress = new SavingsProgress(group.getGroupCode(),
	 * group.getSavingsTarget(), 0, 0); // add member as contributing participant
	 * progress.addMember(memberName); // adding the new savings progress obj to map
	 * savingsProgressMap.put(group.getGroupCode(), progress);
	 * updateGroupSavingsProgress(); // calls this method that writes the updated
	 * progress to file from the map }
	 */

	// Record a contribution, update progress, and save to database
	public void recordContribution(String groupCode, String memberName, double amount) throws Exception {

		// Check if the contributionMap is empty, or if the groupCode does not exist in
		// the map
		if (contributionMap.isEmpty() || !contributionMap.containsKey(groupCode)) {
			loadContributionsFromDatabase(); // Only load if needed
		}

		// Check if the group's savings target has been reached
		if (groupSavingsTargetReached(groupCode)) {
			throw new Exception("The group has reached its savings target");
		}

		List<Contribution> contributionList = contributionMap.get(groupCode);

		// Check if the member has already contributed
		if (contributionList != null && memberContributionExists(contributionList, memberName)) {
			throw new Exception("You have met your contribution quota");
		} else {
			// If no contributions exist for this group, create a new list for the group
			if (contributionList == null) {
				contributionList = new ArrayList<>();
				contributionMap.put(groupCode, contributionList);
			}

			// Add the new contribution to the list
			Contribution contribution = new Contribution(groupCode, memberName, amount, LocalDate.now().toString());
			contributionList.add(contribution);

			// Save the contribution to the database
			saveContributionToDatabase(contribution);
		}
	}

	/*
	 * public void recordContribution(String groupCode, String memberName, double
	 * amount) throws Exception { // Load contributions and savings progress from
	 * the file if the contribution map // is empty if (contributionMap.isEmpty()) {
	 * loadContributionsFromFile(); loadSavingsProgressFromFile(); }
	 * 
	 * // Check if the group's savings target has been reached if
	 * (groupSavingsTargetReached(groupCode)) { throw new
	 * Exception("The group has reached its saving's target"); }
	 * 
	 * // Get the list of contributions for the group List<Contribution>
	 * contributionList = contributionMap.get(groupCode);
	 * 
	 * // Check if the member has already contributed if (contributionList != null
	 * && memberContributionExists(contributionList, memberName)) { throw new
	 * Exception("You have met your contribution quota"); } else { // If no
	 * contributions exist for this group, create a new list for the group if
	 * (contributionList == null) { contributionList = new ArrayList<>(); // Create
	 * a new list contributionMap.put(groupCode, contributionList); // Add it to the
	 * contribution map groupcode key and // contribution list value }
	 * 
	 * // Add the new contribution to the list contributionList.add(new
	 * Contribution(groupCode, memberName, amount, LocalDate.now().toString()));
	 * 
	 * // Update the savings progress for the group
	 * savingsProgressMap.get(groupCode).updateTotalSavings(amount);
	 * savingsProgressMap.get(groupCode).calculatePercentage();
	 * savingsProgressMap.get(groupCode).addMember(memberName);
	 * System.out.println("Updated savings progress: " + savingsProgressMap);
	 * 
	 * // Save the updated contributions and savings progress to the file
	 * saveToContributionFile(); updateGroupSavingsProgress(); } }
	 */
	// this method checks if a member has made a contribution
	/*
	 * public boolean memberContributionExists(List<Contribution> contributionList,
	 * String memberName) { if (contributionList == null) { return false; } for
	 * (Contribution contribution : contributionList) { if (contribution.getMember()
	 * == memberName) { return true; } }
	 * 
	 * return false; }
	 */
	// Check if a member has already contributed
	public boolean memberContributionExists(List<Contribution> contributionList, String memberName) {
		return contributionList != null
				&& contributionList.stream().anyMatch(contribution -> contribution.getMember().equals(memberName));
	}

	private boolean groupSavingsTargetReached(String groupCode) {
		try (Connection conn = DatabaseConnectionF.getConnection()) {
			String sql = "SELECT SUM(amount) AS totalSavings FROM \"CONTRIBUTIONS\" WHERE \"groupCode\" = ?";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, groupCode);
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						double totalSavings = rs.getDouble("totalSavings");
						double targetSavings = getTargetSavingsForGroup(groupCode);
						return totalSavings >= targetSavings;
					}
				}
			}
		} catch (SQLException e) {
			System.out.println("Error calculating group savings target.");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * //this method checks if a member's contribution exist public boolean
	 * memberContributionExists(List<Contribution> contributionList, String
	 * memberName) { return contributionList != null // Lambda is used here to match
	 * member name && contributionList.stream().anyMatch(contribution ->
	 * contribution.getMember().equals(memberName)); }
	 */

	// Check if a group has reached its savings target by summing contributions

	// Helper method to get target savings for a specific group from the database
	private double getTargetSavingsForGroup(String groupCode) {
		try (Connection conn = DatabaseConnectionF.getConnection()) {
			String sql = "SELECT \"targetAmount\" FROM \"GROUPS\" WHERE \"groupCode\" = ?";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, groupCode);
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						return rs.getDouble("targetAmount");
					}
				}
			}
		} catch (SQLException e) {
			System.out.println("Error retrieving target savings from the database.");
			e.printStackTrace();
		}
		return 0;
	}

	// Save individual contribution to the database
	private void saveContributionToDatabase(Contribution contribution) {
		try (Connection conn = DatabaseConnectionF.getConnection()) {
			String sql = "INSERT INTO \"CONTRIBUTIONS\" (\"groupCode\", \"memberName\", \"amount\", \"contributionDate\") VALUES (?, ?, ?, ?)";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, contribution.getGroupCode());
				pstmt.setString(2, contribution.getMember());
				pstmt.setDouble(3, contribution.getAmountContributed());
				pstmt.setDate(4, Date.valueOf(contribution.getCurrentDate()));
				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println("Error saving contribution to database.");
			e.printStackTrace();
		}
	}

	// Load contributions from database and populate contributionMap
	private void loadContributionsFromDatabase() {
		try (Connection conn = DatabaseConnectionF.getConnection()) {
			String sql = "SELECT * FROM \"CONTRIBUTIONS\"";
			try (PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					String groupCode = rs.getString("groupCode");
					String memberName = rs.getString("memberName");
					double amount = rs.getDouble("amount");
					String date = rs.getDate("contributionDate").toString();

					Contribution contribution = new Contribution(groupCode, memberName, amount, date);
					contributionMap.computeIfAbsent(groupCode, k -> new ArrayList<>()).add(contribution);
				}
			}
		} catch (SQLException e) {
			System.out.println("Error loading contributions from database.");
			e.printStackTrace();
		}
	}

	public List<Contribution> getContributionList(String groupCode) {
		if (contributionMap.isEmpty()) {
			loadContributionsFromDatabase();
		}
		return contributionMap.getOrDefault(groupCode, Collections.emptyList());
	}

	// Get total savings for a group by summing contributions
	// Get total savings progress for a group by summing contributions
	public double getTotalSavingsForGroup(String groupCode) {
		double totalSavings = 0;

		try (Connection conn = DatabaseConnectionF.getConnection()) {
			String sql = "SELECT SUM(amount) AS totalSavings FROM \"CONTRIBUTIONS\" WHERE \"groupCode\" = ?";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, groupCode);
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						totalSavings = rs.getDouble("totalSavings");
					}
				}
			}
		} catch (SQLException e) {
			System.out.println("Error retrieving total savings from the database.");
			e.printStackTrace();
		}
		return totalSavings;
	}

	/*
	 * //this method ensures than we can't save more than the set target private
	 * boolean groupSavingsTargetReached(String groupCode) { SavingsProgress
	 * savingsProgress = savingsProgressMap.get(groupCode); if (savingsProgress !=
	 * null) { if (savingsProgress.getTotalSavings() >=
	 * savingsProgress.getTargetSavings()) { return true; } } return false; }
	 */
	// this method writes all contributions store in memory to the file

	/*
	 * public void saveToContributionFile() { // retrieves list, converts list to
	 * individual contribution obj // collects into single list and write to file
	 * easily List<Contribution> consolidatedList =
	 * contributionMap.values().stream().flatMap(List::stream)
	 * .collect(Collectors.toList()); try { File file = new
	 * File(CONTRIBUTION_FILE_PATH); try (FileWriter writer = new FileWriter(file))
	 * { writer.write("GroupCode,MemberName,Amount,ContributionDate\n"); // iterate
	 * and write each contribution to file for (Contribution contribution :
	 * consolidatedList) { writer.write(contribution.toString() + "\n"); // Write
	 * each group using its toString() method }
	 * System.out.println("Contribution record saved successfully."); }
	 * 
	 * } catch (IOException e) { System.out.println("Error saving groups to file.");
	 * e.printStackTrace(); } }
	 */
	// method to write savings progress to file
	/*
	 * private void updateGroupSavingsProgress() { try { File file = new
	 * File(SAVINGS_PROGRESS_FILE_PATH); try (FileWriter writer = new
	 * FileWriter(file)) { writer.write(
	 * "GroupCode,TargetSavings,TotalSavings,PercentageProgress,ContributingMembers\n"
	 * );
	 * 
	 * // loop through each obj in the map and write to the file in a formatted
	 * string for (SavingsProgress progress : savingsProgressMap.values()) {
	 * writer.write(progress.toString() + "\n"); // Write each group using its
	 * toString() method } System.out.println("Progress saved successfully."); }
	 * 
	 * } catch (IOException e) {
	 * System.out.println("Error saving progress to file."); e.printStackTrace(); }
	 * }
	 */
	// method to get contribution used
	/*
	 * // used in dashboard controller to display contribution public
	 * List<Contribution> getContributionList(String groupCode) { if
	 * (contributionMap.isEmpty()) { loadContributionsFromFile(); } return
	 * (contributionMap.get(groupCode)); }
	 * 
	 * // used to get total savings progress and is displayed on dashboard public
	 * SavingsProgress getTotalSavingsProgressForGroup(String groupCode) { if
	 * (savingsProgressMap.isEmpty()) { loadSavingsProgressFromFile(); } return
	 * (savingsProgressMap.get(groupCode)); }
	 */
	// this methods reads contribution data and stores it in memory in the
	// contribution map
	// organize contribution by group
	/*
	 * public void loadContributionsFromFile() {
	 * System.out.println("loadSavingsProgressFromFile methos");
	 * 
	 * File file = new File(CONTRIBUTION_FILE_PATH); if (!file.exists()) {
	 * System.out.println("File not found: " + CONTRIBUTION_FILE_PATH); return; }
	 * else if (file.length() == 0) { System.out.println("File is empty: " +
	 * CONTRIBUTION_FILE_PATH); return; } try (BufferedReader reader = new
	 * BufferedReader(new FileReader(CONTRIBUTION_FILE_PATH))) { String line;
	 * reader.readLine(); while ((line = reader.readLine()) != null) { String[]
	 * contributionDetails = line.split(","); // Each element in contributionDetails
	 * corresponds // to a specific attribute of a Contribution object. String
	 * groupCode = contributionDetails[0]; String member = contributionDetails[1];
	 * double amountContributed = Double.parseDouble(contributionDetails[2]); String
	 * currentDate = contributionDetails[3]; // creates new obj Contribution
	 * contribution = new Contribution(groupCode, member, amountContributed,
	 * currentDate); System.out.println("contribution from file : " +
	 * contribution.toString());
	 * 
	 * if (contributionMap.containsKey(groupCode)) { // If the key exists, add the
	 * new contribution to the existing list
	 * contributionMap.get(groupCode).add(contribution); } else { // If the key does
	 * not exist, create a new list, add the contribution, and put // it in the map
	 * List<Contribution> newContributionList = new ArrayList<>();
	 * newContributionList.add(contribution); contributionMap.put(groupCode,
	 * newContributionList); } // Add the contribution to the in-memory list } }
	 * catch (IOException e) { e.printStackTrace(); } }
	 * 
	 */

	/*
	 * public void loadSavingsProgressFromFile() {
	 * System.out.println("loadSavingsProgressFromFile methos");
	 * 
	 * File file = new File(SAVINGS_PROGRESS_FILE_PATH); if (!file.exists()) {
	 * System.out.println("File not found: " + SAVINGS_PROGRESS_FILE_PATH); return;
	 * // Exit the method if the file does not exist } else if (file.length() == 0)
	 * { System.out.println("File is empty: " + SAVINGS_PROGRESS_FILE_PATH); return;
	 * // Exit the method if the file is empty } // read from file and store into
	 * the map so info is available within the // application try (BufferedReader
	 * reader = new BufferedReader(new FileReader(SAVINGS_PROGRESS_FILE_PATH))) {
	 * String line; reader.readLine(); // skip the first line // while reading the
	 * next line from file, split line by commas while ((line = reader.readLine())
	 * != null) { String[] savingsProgressDetails = line.split(","); // extract
	 * &assign elements of savings progress to variables String groupCode =
	 * savingsProgressDetails[0]; double targetSavings =
	 * Double.parseDouble(savingsProgressDetails[1]); // convert from string to
	 * double double totalSavings = Double.parseDouble(savingsProgressDetails[2]);
	 * double percentageProgress = Double.parseDouble(savingsProgressDetails[3]); //
	 * split contributing members to individual members List<String>
	 * contributingMembers = new
	 * ArrayList<>(Arrays.asList(savingsProgressDetails[4].split(";"))); // Split //
	 * members // list // Re-create the group and members that have contributed
	 * SavingsProgress savingsProgress = new SavingsProgress(groupCode,
	 * targetSavings, totalSavings, percentageProgress);
	 * savingsProgress.getContributingMembers().addAll(contributingMembers);// add
	 * list to savingsprogress obj System.out.println("savings progress from file: "
	 * + savingsProgress.toString());
	 * 
	 * savingsProgressMap.put(groupCode, savingsProgress); // add new created obj to
	 * map } } catch (IOException e) { e.printStackTrace(); }
	 * 
	 * }
	 */

}
