package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;

import java.time.Duration;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By username = By.id("user-name");
    private final By password = By.id("password");
    private final By loginBtn = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;

        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(
                        ConfigReader.getInt(
                                "explicit.wait.seconds",
                                15)));
    }

    public void open() {
        driver.get(ConfigReader.get("url"));
    }

    public void login(String user, String pass) {

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(username));

        driver.findElement(username).clear();
        driver.findElement(username)
                .sendKeys(user == null ? "" : user);

        driver.findElement(password).clear();
        driver.findElement(password)
                .sendKeys(pass == null ? "" : pass);

        driver.findElement(loginBtn).click();
    }

    public String getErrorMessage() {
        return wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                errorMessage))
                .getText();
    }

    public boolean isDisplayed() {
        return wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                loginBtn))
                .isDisplayed();
    }
}