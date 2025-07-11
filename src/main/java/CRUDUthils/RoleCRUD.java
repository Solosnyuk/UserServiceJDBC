package CRUDUthils;

import Model.Role;

import java.sql.*;
import java.util.*;

public class RoleCRUD {

    //создание роли
    public static int createRole(String roleName) {
        if (roleName == null || roleName.trim().isEmpty()) {
            System.err.println("Ошибка: название роли не может быть null, пустым или состоять из пробелов");
            return -1;
        }
        String sql = "INSERT INTO roles (name) VALUES (?) ON CONFLICT (name) DO UPDATE SET name = EXCLUDED.name RETURNING id";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, roleName.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int roleId = rs.getInt("id");
                    System.out.println("Роль создана/обновлена: " + roleName.trim() + ", ID: " + roleId);
                    return roleId;
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при создании/обновлении роли: " + roleName);
            e.printStackTrace();
        }
        return -1;
    }

    //получение списка всех ролей с их id
    public static List<Role> getAllRoles() {
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

    //получение списка ролей пользователя
    public static List<String> getRolesForUser(Connection conn, int userId) {
        if (userId <= 0) {
            System.err.println("Ошибка: ID пользователя должен быть положительным числом");
            return null;
        }
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

    //удаление роли по id
    public static boolean deleteRole(int roleId) {
        if (roleId <= 0) {
            System.err.println("Ошибка: ID роли должен быть положительным числом");
            return false;
        }
        String sql = "DELETE FROM roles WHERE id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roleId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Роль с ID " + roleId + " успешно удалена");
                return true;
            } else {
                System.out.println("Роль с ID " + roleId + " не найдена");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении роли с ID: " + roleId);
            e.printStackTrace();
        }
        return false;
    }
}
