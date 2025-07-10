import org.testng.annotations.*;
import java.util.List;

public class CRUDUtilsTest {

    @BeforeMethod
    public void setup() {
        CRUDUtils.clearUserData();

        int adminID = CRUDUtils.createRole("admin");
        int editorID = CRUDUtils.createRole("editor");

        int aliceID = CRUDUtils.createUser("Alice");
        int bobID = CRUDUtils.createUser("Bob");

        CRUDUtils.assignRoleToUser(aliceID, adminID);
        CRUDUtils.assignRoleToUser(aliceID, editorID);
        CRUDUtils.assignRoleToUser(bobID, editorID);
    }

    @Test
    public void testGetUserData() {
        List<Users> users = CRUDUtils.getUserData("SELECT * FROM users");

        assert users.size() == 2;

        Users alice = users.stream().filter(u -> u.getName().equals("Alice")).findFirst().orElse(null);
        assert alice != null;
        assert alice.getRoles().contains("admin");
        assert alice.getRoles().contains("editor");

        Users bob = users.stream().filter(u -> u.getName().equals("Bob")).findFirst().orElse(null);
        assert bob != null;
        assert bob.getRoles().contains("editor");
    }

    @Test
    public void testUpdateUserName() {
        List<Users> users = CRUDUtils.getUserData("SELECT * FROM users");
        int aliceId = users.stream().filter(u -> u.getName().equals("Alice")).findFirst().get().getId();

        CRUDUtils.updateUserName(aliceId, "Alicia");

        List<Users> updated = CRUDUtils.getUserData("SELECT * FROM users");
        boolean found = updated.stream().anyMatch(u -> u.getName().equals("Alicia"));
        assert found;
    }

    @Test
    public void testDeleteUser() {
        List<Users> users = CRUDUtils.getUserData("SELECT * FROM users");
        int bobId = users.stream().filter(u -> u.getName().equals("Bob")).findFirst().get().getId();

        CRUDUtils.deleteUser(bobId);

        List<Users> updated = CRUDUtils.getUserData("SELECT * FROM users");
        assert updated.size() == 1;
        assert updated.stream().noneMatch(u -> u.getName().equals("Bob"));
    }

    @Test
    public void testGetAllRolesDetailed() {
        List<Role> roles = CRUDUtils.getAllRolesDetailed();
        assert roles.size() >= 2;

        boolean adminExists = roles.stream().anyMatch(r -> r.getName().equals("admin"));
        boolean editorExists = roles.stream().anyMatch(r -> r.getName().equals("editor"));

        assert adminExists;
        assert editorExists;
    }

    @AfterMethod
    public void cleanup() {
        CRUDUtils.clearUserData();
    }
}
