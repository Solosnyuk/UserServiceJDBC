package CRUDUthils;

import Model.Users;

import java.sql.*;
import java.util.*;

import static CRUDUthils.RoleCRUD.getRolesForUser;

public class UserCRUD {

    //создание пользователя
    public static int createUser(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.err.println("Ошибка: имя пользователя не может быть null, пустым или состоять из пробелов");
            return -1;
        }
        String sql = "INSERT INTO users (name) VALUES (?) RETURNING id";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name.trim()); // Удаляем лишние пробелы по краям
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                System.out.println("Пользователь добавлен: " + name + " (id=" + id + ")");
                return id;
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении пользователя: " + name);
            e.printStackTrace();
        }
        return -1;
    }

    //удаление пользователя
    public static void deleteUser(int userId) {
        if (userId <= 0) {
            System.err.println("Ошибка: ID пользователя должен быть положительным числом");
            return;
        }
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0
                    ? "Пользователь c id: " + userId + " удалён"
                    : "Пользователь c id: " + userId + " не найден");
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении пользователя с id: " + userId);
            e.printStackTrace();
        }
    }

    //обновление пользователя по id
    public static void updateUserName(int userId, String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            System.err.println("Ошибка: новое имя не может быть null, пустым или состоять из пробелов");
            return;
        }
        String sql = "UPDATE users SET name = ? WHERE id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newName.trim());
            stmt.setInt(2, userId);
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0
                    ? "Имя пользователя с id: " + userId + ", обновлено на имя " + newName.trim()
                    : "Пользователь c id: " + userId + ", не найден");
        } catch (SQLException e) {
            System.err.println("Ошибка при обновлении имени пользователя с id: " + userId);
            e.printStackTrace();
        }
    }

    //изменение роли пользователя по его id и id роли
    public static void assignRoleToUser(int userId, int roleId) {
        if (userId <= 0 || roleId <= 0) {
            System.err.println("Ошибка: ID пользователя и роли должны быть положительными числами");
            return;
        }
        String sql = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, roleId);
            stmt.executeUpdate();
            System.out.println("Роль с id: " + roleId + ", назначена пользователю с id: " + userId);
        } catch (SQLException e) {
            System.err.println("Ошибка при назначении роли " + roleId + " пользователю " + userId);
            e.printStackTrace();
        }
    }

    //получение Users по его id
    public static Users getUserById(int userId) {
        if (userId <= 0) {
            System.err.println("Ошибка: ID пользователя должен быть положительным числом");
            return null;
        }
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String userName = rs.getString("name");
                List<String> roles = getRolesForUser(conn, userId);
                return new Users(userId, userName, roles);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении пользователя по ID: " + userId);
            e.printStackTrace();
        }
        return null;
    }

    //получение списка пользователей и их ролей
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
}
