import java.util.List;

public class App {
    public static void main(String[] args) {

        List<Users> users = CRUDUtils.getUserData("SELECT * FROM users");
        System.out.println(users);
    }
}
