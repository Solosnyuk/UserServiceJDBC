package DbTools;

import Model.Users;

import java.sql.*;
import java.util.*;

import static DbTools.RoleCRUD.getRolesForUser;

public class UserCRUD {

    //создание пользователя
    public static int createUser(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.err.println("Ошибка: имя пользователя не может быть null, пустым или состоять из пробелов");
            Logger.logAction("createUser", "Недопустимое имя пользователя: '" + name + "'");
            return -1;
        }
        String sql = "INSERT INTO users (name) VALUES (?) RETURNING id";
        try (java.sql.Connection conn = Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name.trim()); // Удаляем лишние пробелы по краям
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                System.out.println("Пользователь добавлен: " + name + " (id=" + id + ")");
                Logger.logAction("createUser", "Пользователь добавлен: '" + name + "' id=" + id + "");
                return id;
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении пользователя: " + name);
            Logger.logAction("createUser", "Ошибка SQL при добавлении пользователя: '" + name + "'");
            e.printStackTrace();
        }
        Logger.logAction("createUser",  "Не удалось добавить пользователя: '" + name + "'");
        return -1;
    }

    //удаление пользователя
    public static void deleteUser(int userId) {
        if (userId <= 0) {
            System.err.println("Ошибка: ID пользователя должен быть положительным числом");
            Logger.logAction("deleteUser", "Недопустимый ID пользователя: " + userId);
            return;
        }
        String sql = "DELETE FROM users WHERE id = ?";
        try (java.sql.Connection conn = Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Пользователь c id: " + userId + " удалён");
                Logger.logAction("deleteUser",  "Пользователь удалён id: " + userId);
            } else {
                System.out.println("Пользователь c id: " + userId + " не найден");
                Logger.logAction("deleteUser",  "Пользователь не найден id: " + userId);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении пользователя с id: " + userId);
            Logger.logAction("deleteUser", "SQL ошибка при удалении пользователя id: " + userId);
            e.printStackTrace();
        }
    }

    //обновление пользователя по id
    public static void updateUserName(int userId, String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            System.err.println("Ошибка: новое имя не может быть null, пустым или состоять из пробелов");
            Logger.logAction("updateUsername", "Передано некорректное имя: " + newName);
            return;
        }
        String sql = "UPDATE users SET name = ? WHERE id = ?";
        try (java.sql.Connection conn = Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newName.trim());
            stmt.setInt(2, userId);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Имя пользователя с id: " + userId + ", обновлено на имя " + newName.trim());
                Logger.logAction("updateUserName", "Имя пользователя обновлено id:" + userId + ", новое имя " + newName.trim());
            } else {
                System.out.println("Пользователь c id: " + userId + ", не найден");
                Logger.logAction("updateUserName", "Пользователь не найден id:" + userId);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при обновлении имени пользователя с id: " + userId);
            Logger.logAction("updateUserName",  "SQL ошибка при обновлении имени пользователя id: " + userId);
            e.printStackTrace();
        }
    }

    //изменение роли пользователя по его id и id роли
    public static void assignRoleToUser(int userId, int roleId) {
        if (userId <= 0 || roleId <= 0) {
            System.err.println("Ошибка: ID пользователя и роли должны быть положительными числами");
            Logger.logAction("assignRoleToUser",  "Недопустимые userId: " + userId + ", roleId:" + roleId);
            return;
        }
        String sql = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
        try (java.sql.Connection conn = Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, roleId);
            stmt.executeUpdate();
            System.out.println("Роль с id: " + roleId + ", назначена пользователю с id: " + userId);
            Logger.logAction("assignRoleToUser",  "Роль назначена roleId:" + roleId + ", userId:" + userId);
        } catch (SQLException e) {
            System.err.println("Ошибка при назначении роли " + roleId + " пользователю " + userId);
            Logger.logAction("assignRoleToUser", "Ошибка SQL при назначении роли roleId:" + roleId + " userId:" + userId);
            e.printStackTrace();
        }
    }

    //получение Users по его id
    public static Users getUserById(int userId) {
        if (userId <= 0) {
            System.err.println("Ошибка: ID пользователя должен быть положительным числом");
            Logger.logAction("getUserById",  "Передан некорректный userId: " + userId);
            return null;
        }
        String sql = "SELECT * FROM users WHERE id = ?";
        try (java.sql.Connection conn = Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String userName = rs.getString("name");
                List<String> roles = getRolesForUser(conn, userId);
                Logger.logAction("getUserById", "Пользователь найден id: " + userId +
                        ", name: " + userName + ", ролей: " + roles.size());
                return new Users(userId, userName, roles);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении пользователя по ID: " + userId);
            Logger.logAction("getUserById", "SQL ошибка при получении пользователя id:" + userId);
            e.printStackTrace();
        }
        return null;
    }

    //получение списка пользователей и их ролей
    public static List<Users> getUserData(String query) {
        List<Users> userList = new ArrayList<>();
        try (java.sql.Connection conn = Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int userId = rs.getInt("id");
                String userName = rs.getString("name");
                List<String> roles = getRolesForUser(conn, userId);
                userList.add(new Users(userId, userName, roles));
            }
            Logger.logAction("getUserData", "Количество полученных пользователей: " + userList.size());
        } catch (SQLException e) {
            Logger.logAction("getUserData",  "SQL ошибка при выполнении запроса: " + query);
            e.printStackTrace();
        }
        return userList;
    }
}
