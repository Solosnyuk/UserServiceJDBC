import DbTools.*;
import Model.*;
import org.testng.*;
import org.testng.annotations.Test;

import java.util.*;

public class CRUDUtilsTest extends BaseTest {
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
}
