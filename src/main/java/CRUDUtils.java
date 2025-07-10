import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CRUDUtils {

    //создание пользователя
    public static int createUser(String name) {
        String sql = "INSERT INTO users (name) VALUES (?) RETURNING id";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             stmt.setString(1, name);
             ResultSet rs = stmt.executeQuery();
             if (rs.next()) {
                 int id = rs.getInt("id");
                 System.out.println("Пользователь добавлен: " + name + " (id=" + id + ")");
                 return id;
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    //создание роли
    public static int createRole(String roleName) {
        String sql = "INSERT INTO roles (name) VALUES (?) ON CONFLICT (name) DO UPDATE SET name = EXCLUDED.name RETURNING id";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, roleName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int roleId = rs.getInt("id");
                    System.out.println("Роль создана/обновлена: " + roleName + ", ID: " + roleId);
                    return roleId;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }



    //получение списка поьзователей и их ролей
    public static List<Users> getUserData(String query) {
        List<Users> userList = new ArrayList<>();
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int userId = rs.getInt("id");
                String userName = rs.getString("name");
                List<String> roles = getRolesForUser(conn, userId);
                userList.add(new Users(userId, userName, roles));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }
    //получение списка ролей пользователя
    private static List<String> getRolesForUser(Connection conn, int userId) {
        List<String> roles = new ArrayList<>();
        String sql = """
            SELECT r.name
            FROM user_roles ur
            JOIN roles r ON ur.role_id = r.id
            WHERE ur.user_id = ?
            """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                roles.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    //обновление пользователя по id
    public static void updateUserName(int userId, String newName) {
        String sql = "UPDATE users SET name = ? " +
                "WHERE id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setInt(2, userId);
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0 ? "Имя пользователя с id: " + userId + ", обновлено на имя " + newName :
                    "Пользователь c id: " + userId + ", не найден");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //удаление пользователя
    public static void deleteUser(int userId) {
        String sql = "DELETE " +
                "FROM users " +
                "WHERE id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0 ?
                    "Пользователь c id: " + userId + " удалён" : " Пользователь c id: " + userId + " не найден");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //изменение роли пользователя по его id и id роли
    public static void assignRoleToUser(int userId, int roleId) {
        String sql = "INSERT INTO user_roles (user_id, role_id) " +
                "VALUES (?, ?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, roleId);
            stmt.executeUpdate();
            System.out.println("Роль с id: " + roleId + ", назначена пользователю с id: " + roleId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //очистка таблиц
    public static void clearUserData() {
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

    //получение списка всех ролей с их id
    public static List<Role> getAllRolesDetailed() {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT id, name FROM roles";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                roles.add(new Role(id, name));
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при получении списка ролей");
            e.printStackTrace();
        }
        return roles;
    }
}
