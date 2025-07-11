import CRUDUthils.DataBaseUthils;
import CRUDUthils.RoleCRUD;
import CRUDUthils.UserCRUD;
import Model.Role;
import Model.Users;

import java.util.List;

public class App {
    public static void main(String[] args) {
        //int aliceID = UserCRUD.createUser("Alice");
        //int bobID = UserCRUD.createUser("Bob");
        //int adminID = RoleCRUD.createRole("admin");
        //int userID = RoleCRUD.createRole("user");
//
        //UserCRUD.assignRoleToUser(aliceID, adminID);
        //UserCRUD.assignRoleToUser(bobID, userID);
        //UserCRUD.assignRoleToUser(bobID, userID);

        List<Role> roles = RoleCRUD.getAllRoles();
        System.out.println("============================");
        System.out.println("Доступные роли:" + "\n");
        for (Role role : roles) {
            System.out.println(role);
        }

        List<Users> users = UserCRUD.getUserData("SELECT * FROM users");
        System.out.println("============================");
        System.out.println("Пользователи:" + "\n");
        for (Users user : users) {
            System.out.println(user);
        }

        //изменение пользователя по id
        //CRUDUtils.updateUserName(1, "Alicia");

        //удаление роли по id
        //RoleCRUD.deleteRole();

        //удаление пользователя по его id
        //CRUDUtils.deleteUser(1);

        //удалние всей информации
        //DataBaseUthils.clearDataBase();
    }
}