package hooks;

import drivers.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.testng.Reporter;
import utils.ConfigReader;

public class Hooks {

    @Before
    public void setUp() {
        String browser = Reporter.getCurrentTestResult() == null
                ? null
                : Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("browser");
        DriverFactory.initDriver(browser == null ? ConfigReader.getOrDefault("browser", "chrome") : browser);
    }

    @After
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
