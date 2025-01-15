import services.EmployeeManager;
import ui.LoginScreen;

public class App {
    public static void main(String[] args) {
        EmployeeManager employeeManager = new EmployeeManager();
        new LoginScreen(employeeManager).setVisible(true);
    }
}
