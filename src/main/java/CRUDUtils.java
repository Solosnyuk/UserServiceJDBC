import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CRUDUtils {

    public static List<Users> getUserData(String query) {
        List<Users> users = new ArrayList<>();

        try (Connection connection = DBUtils.getConnection();
            PreparedStatement preaparedStatement = connection.prepareStatement(query)){
            ResultSet rs = preaparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int role_user = rs.getInt("role_id");

                users.add(new Users(id,name,role_user));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    return users;
    }
}
