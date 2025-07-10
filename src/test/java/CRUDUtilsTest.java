import CRUDUthils.DataBaseUthils;
import CRUDUthils.RoleCRUD;
import CRUDUthils.UserCRUD;
import Model.Role;
import Model.Users;
import org.testng.annotations.*;
import java.util.List;

public class CRUDUtilsTest {

    @BeforeMethod
    public void setup() {
        DataBaseUthils.clearDataBase();

        int adminID = RoleCRUD.createRole("admin");
        int editorID = RoleCRUD.createRole("editor");

        int aliceID = UserCRUD.createUser("Alice");
        int bobID = UserCRUD.createUser("Bob");

        UserCRUD.assignRoleToUser(aliceID, adminID);
        UserCRUD.assignRoleToUser(aliceID, editorID);
        UserCRUD.assignRoleToUser(bobID, editorID);
    }

    @Test
    public void testGetUserData() {
        List<Users> users = UserCRUD.getUserData("SELECT * FROM users");

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
        List<Users> users = UserCRUD.getUserData("SELECT * FROM users");
        int aliceId = users.stream().filter(u -> u.getName().equals("Alice")).findFirst().get().getId();

        UserCRUD.updateUserName(aliceId, "Alicia");

        List<Users> updated = UserCRUD.getUserData("SELECT * FROM users");
        boolean found = updated.stream().anyMatch(u -> u.getName().equals("Alicia"));
        assert found;
    }

    @Test
    public void testDeleteUser() {
        List<Users> users = UserCRUD.getUserData("SELECT * FROM users");
        int bobId = users.stream().filter(u -> u.getName().equals("Bob")).findFirst().get().getId();

        UserCRUD.deleteUser(bobId);

        List<Users> updated = UserCRUD.getUserData("SELECT * FROM users");
        assert updated.size() == 1;
        assert updated.stream().noneMatch(u -> u.getName().equals("Bob"));
    }

    @Test
    public void testGetAllRoles() {
        List<Role> roles = RoleCRUD.getAllRoles();
        assert roles.size() >= 2;

        boolean adminExists = roles.stream().anyMatch(r -> r.getName().equals("admin"));
        boolean editorExists = roles.stream().anyMatch(r -> r.getName().equals("editor"));

        assert adminExists;
        assert editorExists;
    }

    @AfterMethod
    public void cleanup() {
        DataBaseUthils.clearDataBase();
    }
}
