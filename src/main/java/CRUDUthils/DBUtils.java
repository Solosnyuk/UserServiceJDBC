package CRUDUthils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {
    private static String dbURL = "jdbc:postgresql://localhost:5432/UserServiceDB";
    private static String dbUsername = "postgres";
    private static String dbPassword = "123456";

    public static Connection getConnection() {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(dbURL,dbUsername,dbPassword);
            System.out.println("Успешное подключение к PostgreSQL");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка подключения к PostgreSQL");
         }
        return connection;
    }

}
