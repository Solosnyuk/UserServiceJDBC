import java.util.List;

public class App {
    public static void main(String[] args) {
        CRUDUtils.createUser("Alice");
        CRUDUtils.createUser("Bob");

        CRUDUtils.assignRoleToUser(1, 1); // Alice - admin
        CRUDUtils.assignRoleToUser(1, 2); // Alice - editor
        CRUDUtils.assignRoleToUser(2, 2); // Bob - editor

        List<Users> users = CRUDUtils.getUserData("SELECT * FROM users");
        for (Users user : users) {
            System.out.println(user);
        }

        //изменение пользователя по id
        //CRUDUtils.updateUserName(1, "Alicia");

        //удаление пользователя по его id
        //CRUDUtils.deleteUser(1);

    }
}
