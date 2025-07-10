import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CRUDUtils {

    public static void createUser(String name) {
        String sql = "INSERT INTO users (name) VALUES (?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
            System.out.println("Пользователь добавлен: " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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

    public static void updateUserName(int userId, String newName) {
        String sql = "UPDATE users SET name = ? " +
                "WHERE id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setInt(2, userId);
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0 ? "Имя обновлено" : "Пользователь не найден");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteUser(int userId) {
        String sql = "DELETE " +
                "FROM users " +
                "WHERE id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0 ? "Пользователь удалён" : " Пользователь не найден");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void assignRoleToUser(int userId, int roleId) {
        String sql = "INSERT INTO user_roles (user_id, role_id) " +
                "VALUES (?, ?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, roleId);
            stmt.executeUpdate();
            System.out.println("Роль назначена пользователю");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
