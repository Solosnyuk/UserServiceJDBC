package DbTools;

import java.sql.*;

public class DataBase {

    //очистка таблиц
    public static void clearDataBase() {
        String deleteUserRolesSQL = "DELETE FROM user_roles;";
        String deleteUsersSQL = "DELETE FROM users;";
        String deleteRolesSQL = "DELETE FROM roles;";

        try (java.sql.Connection conn = Connection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(deleteUserRolesSQL);
            stmt.executeUpdate(deleteUsersSQL);
            stmt.executeUpdate(deleteRolesSQL);
            Logger.logAction("clearDataBase", "Все таблицы успешно очищены");
            System.out.println("Таблицы user_roles и users, roles успешно очищены");
        } catch (SQLException e) {
            System.out.println("Ошибка при очистке данных");
            Logger.logAction("clearDataBase", "Ошибка SQL при очистке базы данных");
            e.printStackTrace();
        }
    }
}
