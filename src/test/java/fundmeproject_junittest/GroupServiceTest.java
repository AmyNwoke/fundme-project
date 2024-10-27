package fundmeproject_junittest;


	import com.bptn.fundmeproject.model.Group;
	import com.bptn.fundmeproject.service.GroupService;
	import org.junit.jupiter.api.BeforeEach;
	import org.junit.jupiter.api.Test;
	import static org.junit.jupiter.api.Assertions.*;

	public class GroupServiceTest {

	    private GroupService groupService;

	    @BeforeEach
	    public void setUp() {
	        groupService = new GroupService();
	    }

	    @Test
	    public void testAddGroup() throws Exception {
	        Group group = new Group("Test Group", 5, 1000.0, 12, "Monthly", "2024-01-01", "Vacation", "TG123", 83.33);
	        groupService.addGroup(group, "John");

	        // Check that the group was added and contains the creator
	        assertEquals(group, groupService.findGroupByCode("TG123"));
	        assertTrue(group.getMembers().contains("John"));
	    }

	    @Test
	    public void testAddMemberToGroup() throws Exception {
	        Group group = new Group("Test Group", 5, 1000.0, 12, "Monthly", "2024-01-01", "Vacation", "TG123", 83.33);
	        groupService.addGroup(group, "John");

	        // Add a new member and verify
	        groupService.addMemberToGroup("TG123", "Alice");
	        assertTrue(group.getMembers().contains("Alice"));
	    }

	    @Test
	    public void testAddMemberToGroupMemberExistsException() throws Exception {
	        Group group1 = new Group("Group 1", 3, 500.0, 6, "Monthly", "2024-01-01", "Event", "G123", 83.33);
	        Group group2 = new Group("Group 2", 3, 700.0, 8, "Monthly", "2024-01-01", "Travel", "G124", 87.5);

	        groupService.addGroup(group1, "Alice");

	        // Attempt to add the same member to another group
	        Exception exception = assertThrows(Exception.class, () -> {
	            groupService.addGroup(group2, "Alice");
	        });

	        String expectedMessage = "You can only belong to a single group";
	        String actualMessage = exception.getMessage();
	        assertTrue(actualMessage.contains(expectedMessage));
	    }

	    @Test
	    public void testFindGroupByCode() throws Exception {
	        Group group = new Group("Test Group", 5, 1000.0, 12, "Monthly", "2024-01-01", "Vacation", "TG123", 83.33);
	        groupService.addGroup(group, "John");

	        Group foundGroup = groupService.findGroupByCode("TG123");
	        assertNotNull(foundGroup);
	        assertEquals("Test Group", foundGroup.getGroupName());
	    }

	    @Test
	    public void testIsMemberInAnyGroup() throws Exception {
	        Group group = new Group("Test Group", 5, 1000.0, 12, "Monthly", "2024-01-01", "Vacation", "TG123", 83.33);
	        groupService.addGroup(group, "John");

	        assertTrue(groupService.isMemberInAnyGroup("John"));
	        assertFalse(groupService.isMemberInAnyGroup("Alice"));
	    }

	    @Test
	    public void testFindGroupForMember() throws Exception {
	        Group group = new Group("Test Group", 5, 1000.0, 12, "Monthly", "2024-01-01", "Vacation", "TG123", 83.33);
	        groupService.addGroup(group, "John");

	        Group memberGroup = groupService.findGroupForMember("John");
	        assertNotNull(memberGroup);
	        assertEquals("TG123", memberGroup.getGroupCode());
	    }
	}


