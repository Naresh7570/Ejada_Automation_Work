package drivers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import utils.ConfigReader;

import java.time.Duration;

public class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    public static void initDriver(String browser) {
        WebDriver driver;
        String selectedBrowser = browser == null || browser.isBlank()
                ? ConfigReader.getOrDefault("browser", "chrome")
                : browser;
        boolean headless = Boolean.parseBoolean(ConfigReader.getOrDefault("headless", "false"));

        if (selectedBrowser.equalsIgnoreCase("chrome")) {

            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();

            options.addArguments("--disable-notifications");
            options.addArguments("--disable-save-password-bubble");
            options.addArguments("--disable-features=PasswordCheck");
            options.addArguments("--guest");
            if (headless) {
                options.addArguments("--headless=new");
            }
            options.addArguments("--remote-allow-origins=*", "--disable-notifications");
            driver = new ChromeDriver(options);

        } else if (selectedBrowser.equalsIgnoreCase("firefox")) {

            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            if (headless) {
                options.addArguments("-headless");
            }
            driver = new FirefoxDriver(options);

        } else {
            throw new RuntimeException("Browser not supported: " + selectedBrowser);
        }

        driver.manage().window().maximize();

        driver.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(ConfigReader.getInt("implicit.wait.seconds", 5)));

        DRIVER.set(driver);
    }

    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }
}
