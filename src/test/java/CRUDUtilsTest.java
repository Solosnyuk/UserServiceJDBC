import CRUDUthils.*;
import Model.Role;
import Model.Users;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import java.util.List;
import java.util.Set;

public class CRUDUtilsTest {
    int aliceID;
    int bobID;
    int adminID;
    int editorID;

    @BeforeMethod
    public void setup() {
        DataBaseUthils.clearDataBase();

        adminID = RoleCRUD.createRole("admin");
        editorID = RoleCRUD.createRole("editor");

        aliceID = UserCRUD.createUser("Alice");
        bobID = UserCRUD.createUser("Bob");

        UserCRUD.assignRoleToUser(aliceID, adminID);
        UserCRUD.assignRoleToUser(aliceID, editorID);
        UserCRUD.assignRoleToUser(bobID, editorID);
    }

    @Test
    public void testGetUserData() {
            List<Users> users = UserCRUD.getUserData("SELECT * FROM users");

            Assert.assertTrue(
                    users.size() == 2 &&
                            users.stream().anyMatch(u ->
                                    u.getName().equals("Alice") &&
                                            u.getRoles().containsAll(List.of("admin", "editor"))
                            ) &&
                            users.stream().anyMatch(u ->
                                    u.getName().equals("Bob") &&
                                            u.getRoles().contains("editor")),
                    "Данные пользователей не соответствуют ожидаемым"
            );
    }

    @Test
    public void testUpdateUserName() {
        String newName = "Irina";
        UserCRUD.updateUserName(aliceID, newName);

        Assert.assertEquals(
                UserCRUD.getUserById(aliceID).getName(),
                "Irina",
                "Имя пользователя  измениться на " + newName
        );
    }

    @Test
    public void testDeleteUser() {
        List<Users> users = UserCRUD.getUserData("SELECT * FROM users");
        int bobId = users.stream()
                .filter(u -> u.getName().equals("Bob"))
                .findFirst()
                .get()
                .getId();

        UserCRUD.deleteUser(bobId);
        List<Users> updated = UserCRUD.getUserData("SELECT * FROM users");

        assert updated.stream().noneMatch(u -> u.getName().equals("Bob"));
    }


    @Test
    public void testGetAllRoles() {
        List<Role> roles = RoleCRUD.getAllRoles();
        Set<String> requiredRoles = Set.of("admin", "editor");

        long matchedCount = roles.stream()
                .map(Role::getName)
                .filter(requiredRoles::contains)
                .distinct()
                .count();

        assert matchedCount == requiredRoles.size();
    }

    @AfterMethod
    public void cleanup() {
        DataBaseUthils.clearDataBase();
    }

    @AfterMethod
    public void logAfterTest(ITestResult result) {
        String status = result.isSuccess() ? "FINISHED" : "FAILED";
        String testName = result.getMethod().getMethodName();
        String message = status.equals("FAILED") ? "Тест завершился с ошибкой" : "Тест завершён успешно";

        Logger.log(testName, status, message);
    }

}
