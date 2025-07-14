import DbTools.DataBase;
import DbTools.Logger;
import DbTools.RoleCRUD;
import DbTools.UserCRUD;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public class BaseTest {
    int aliceID;
    int bobID;
    int adminID;
    int editorID;

    @BeforeSuite
    public void clearLogs() {
        Logger.clearLogs();
    }

    @BeforeMethod
    public void setup() {
        DataBase.clearDataBase();

        adminID = RoleCRUD.createRole("admin");
        editorID = RoleCRUD.createRole("editor");

        aliceID = UserCRUD.createUser("Alice");
        bobID = UserCRUD.createUser("Bob");

        UserCRUD.assignRoleToUser(aliceID, adminID);
        UserCRUD.assignRoleToUser(aliceID, editorID);
        UserCRUD.assignRoleToUser(bobID, editorID);
    }

    @AfterMethod
    public void cleanup() {
        DataBase.clearDataBase();
    }

    @AfterMethod
    public void logAfterTest(ITestResult result) {
        String status = result.isSuccess() ? "FINISHED" : "FAILED";
        String testName = result.getMethod().getMethodName();
        String message = status.equals("FAILED") ? "Тест завершился с ошибкой" : "Тест завершён успешно";

        Logger.log(testName, status, message);
    }
}
