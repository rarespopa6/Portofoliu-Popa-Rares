import Controller.Controller;
import Repo.*;
import Tests.Tests;
import UI.UI;

import org.junit.runner.JUnitCore;

public class Main {
    public static void main(String[] args) {
        // Tests
        JUnitCore.runClasses(Tests.class);

        Repository repo = new Repository("vehicles.csv");
        Controller controller = new Controller(repo);
        UI ui = new UI(controller);
        ui.displayMenu();
    }
}