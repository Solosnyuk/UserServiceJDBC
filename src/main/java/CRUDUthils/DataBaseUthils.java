package CRUDUthils;

import java.sql.*;

public class DataBaseUthils {

    //очистка таблиц
    public static void clearDataBase() {
        String deleteUserRolesSQL = "DELETE FROM user_roles;";
        String deleteUsersSQL = "DELETE FROM users;";
        String deleteRolesSQL = "DELETE FROM roles;";

        try (Connection conn = DBUtils.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(deleteUserRolesSQL);
            stmt.executeUpdate(deleteUsersSQL);
            stmt.executeUpdate(deleteRolesSQL);

            System.out.println("Таблицы user_roles и users, roles успешно очищены");
        } catch (SQLException e) {
            System.out.println("Ошибка при очистке данных");
            e.printStackTrace();
        }
    }
}
