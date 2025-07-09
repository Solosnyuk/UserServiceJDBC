import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {
    private static String dbURL = "jdbc:h2:mem:test;INIT=RUNSCRIPT FROM 'classpath:init.sql'";
    private static String dbUsername = "sa";
    private static String dbPassword = "";

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
