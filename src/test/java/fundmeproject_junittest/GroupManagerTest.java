package fundmeproject_junittest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.bptn.fundmeproject_01_modelling.*; // Your actual package path

import java.util.ArrayList;
import java.util.List;

class GroupManagerTest {

	private GroupManager groupManager;

	@BeforeEach
	void setUp() {
		groupManager = GroupManager.getInstance(); // Ensure you use the singleton instance
	}

	@Test
	void testAddGroup() {
		Group group = new Group("GroupName", 5, 1000.0, 12, "monthly", "2024-01-01", "SavingsGoal", "groupCode", 100);
		groupManager.addGroup(group, "Creator");

		// Assert that the group is added correctly
		Group retrievedGroup = groupManager.findGroupByCode("groupCode");
		assertNotNull(retrievedGroup);
		assertEquals("GroupName", retrievedGroup.getGroupName());
		assertTrue(retrievedGroup.getMembers().contains("Creator"));
	}

	@Test
	public void testRecordContribution_Success() throws Exception {
		Group group = new Group("Holiday Fund", 5, 1000.0, 12, "monthly", "2024-01-01", "Vacation", "GRP001", 100.0);
		groupManager.addGroup(group, "Alice");

		// Record contribution
		groupManager.recordContribution("GRP001", "Alice", 100.0);

		// Validate contribution
		List<Contribution> contributions = groupManager.getContributionList("GRP001");
		assertNotNull(contributions);
		assertEquals(1, contributions.size());
		assertEquals("Alice", contributions.get(0).getMember());
		assertEquals(100.0, contributions.get(0).getAmountContributed());
	}

	@Test
	public void testRecordContribution_TargetReached_ShouldThrowException() throws Exception {
		Group group = new Group("Holiday Fund", 5, 100.0, 12, "monthly", "2024-01-01", "Vacation", "GRP003", 10.0);
		groupManager.addGroup(group, "Alice");

		// Record a contribution that reaches the target
		groupManager.recordContribution("GRP003", "Alice", 100.0);

		// Try adding another contribution after the target has been reached
		Exception exception = assertThrows(Exception.class, () -> {
			groupManager.recordContribution("GRP003", "Alice", 10.0);
		});

		assertEquals("The group has reached its saving's target", exception.getMessage());
	}

	@Test
	public void testMemberContributionExists() {
		List<Contribution> contributionList = new ArrayList<>();
		contributionList.add(new Contribution("GRP001", "Alice", 100.0, "2024-01-01"));

		// Check that the method correctly identifies existing member
		assertTrue(groupManager.memberContributionExists(contributionList, "Alice"));

		// Check that the method correctly returns false for non-existing member
		assertFalse(groupManager.memberContributionExists(contributionList, "Bob"));
	}

	@Test
	public void testAddMemberToGroup_Success() throws Exception {
		Group group = new Group("Holiday Fund", 5, 1000.0, 12, "monthly", "2024-01-01", "Vacation", "GRP001", 100.0);
		groupManager.addGroup(group, "Alice");

		// Add a new member
		groupManager.addMemberToGroup("GRP001", "Bob");

		// Validate that Bob was added
		Group retrievedGroup = groupManager.findGroupByCode("GRP001");
		assertTrue(retrievedGroup.getMembers().contains("Bob"));
		assertEquals(2, retrievedGroup.getMembers().size());
	}

	@Test
	public void testAddMemberToFullGroup_ShouldThrowException() throws Exception {
		Group group = new Group("Holiday Fund", 2, 1000.0, 12, "monthly", "2024-01-01", "Vacation", "GRP002", 100.0);
		groupManager.addGroup(group, "Alice");

		// Add another member to fill the group
		groupManager.addMemberToGroup("GRP002", "Bob");

		// Try adding a member to a full group
		Exception exception = assertThrows(Exception.class, () -> {
			groupManager.addMemberToGroup("GRP002", "Charlie");
		});

		assertEquals("Group is already full, please join another group.", exception.getMessage());
	}

	@Test
	void testFindGroupByCode() {
		Group group = new Group("GroupName", 5, 1000.0, 12, "monthly", "2024-01-01", "SavingsGoal", "groupCode", 100);
		groupManager.addGroup(group, "Creator");

		// Retrieve group by code
		Group retrievedGroup = groupManager.findGroupByCode("groupCode");
		assertNotNull(retrievedGroup);
		assertEquals("GroupName", retrievedGroup.getGroupName());
	}

}
