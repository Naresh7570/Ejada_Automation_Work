package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;

public class CheckoutPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By firstName = By.id("first-name");
    private final By lastName = By.id("last-name");
    private final By postalCode = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By cancelButton = By.id("cancel");
    private final By finishButton = By.id("finish");
    private final By errorMessage = By.cssSelector("[data-test='error']");
    private final By completeHeader = By.className("complete-header");
    private final By subtotal = By.className("summary_subtotal_label");
    private final By tax = By.className("summary_tax_label");
    private final By total = By.className("summary_total_label");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("explicit.wait.seconds", 15)));
    }

    public void enterFirstName(String first) {
        WebElement firstNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(firstName));
        firstNameField.clear();
        firstNameField.sendKeys(first == null ? "" : first);
    }

    public void enterLastName(String last) {
        WebElement lastNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(lastName));
        lastNameField.clear();
        lastNameField.sendKeys(last == null ? "" : last);
    }

    public void enterPostalCode(String zip) {
        WebElement zipField = wait.until(ExpectedConditions.visibilityOfElementLocated(postalCode));
        zipField.clear();
        zipField.sendKeys(zip == null ? "" : zip);
    }

    public void clickContinue() {
        wait.until(ExpectedConditions.elementToBeClickable(continueButton)).click();
    }

    public void cancel() {
        wait.until(ExpectedConditions.elementToBeClickable(cancelButton)).click();
    }

    public void finish() {
        wait.until(ExpectedConditions.elementToBeClickable(finishButton)).click();
    }

    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
    }

    public String getConfirmationMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(completeHeader)).getText();
    }

    public boolean overviewContainsProduct(String productName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("summary_info")));
        return driver.findElements(By.className("inventory_item_name"))
                .stream()
                .anyMatch(item -> item.getText().equals(productName));
    }

    public BigDecimal getSubtotal() {
        return moneyFromLabel(wait.until(ExpectedConditions.visibilityOfElementLocated(subtotal)).getText());
    }

    public BigDecimal getTax() {
        return moneyFromLabel(wait.until(ExpectedConditions.visibilityOfElementLocated(tax)).getText());
    }

    public BigDecimal getTotal() {
        return moneyFromLabel(wait.until(ExpectedConditions.visibilityOfElementLocated(total)).getText());
    }

    public boolean summaryMathIsCorrect() {
        return getSubtotal().add(getTax()).setScale(2, RoundingMode.HALF_UP).compareTo(getTotal()) == 0;
    }

    private BigDecimal moneyFromLabel(String label) {
        return new BigDecimal(label.replaceAll("[^0-9.]", ""));
    }
}
