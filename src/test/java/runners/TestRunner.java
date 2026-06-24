package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = "src/test/resources/features",
        // Add "api" to the glue array below
        glue = {"stepdefinitions", "hooks", "api"},

                plugin = {
                        "pretty",
                        "html:target/cucumber-report.html",
                        "json:target/cucumber.json"
                },
        monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}