import java.util.List;

public class App {
    public static void main(String[] args) {
        //int aliceID = CRUDUtils.createUser("Alice");
        //int bobID = CRUDUtils.createUser("Bob");
        //int adminIN = CRUDUtils.createRole("admin");
        //int userID = CRUDUtils.createRole("user");

        //CRUDUtils.assignRoleToUser(aliceID, adminID);
        //CRUDUtils.assignRoleToUser(bobID, userID);
        //CRUDUtils.assignRoleToUser(bobID, userID);

        List<Role> roles = CRUDUtils.getAllRolesDetailed();
        System.out.println("============================");
        System.out.println("Доступные роли:" + "\n");
        for (Role role : roles) {
            System.out.println(role);
        }

        List<Users> users = CRUDUtils.getUserData("SELECT * FROM users");
        System.out.println("============================");
        System.out.println("Пользователи:" + "\n");
        for (Users user : users) {
            System.out.println(user);
        }

        //изменение пользователя по id
        //CRUDUtils.updateUserName(1, "Alicia");

        //удаление пользователя по его id
        //CRUDUtils.deleteUser(1);

    }
}
