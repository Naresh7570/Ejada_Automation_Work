package pages;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class InventoryPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By cartIcon = By.className("shopping_cart_link");

    private final By backpackAddButton =
            By.id("add-to-cart-sauce-labs-backpack");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(
                        ConfigReader.getInt("explicit.wait.seconds", 15)
                )
        );
    }

    public boolean isLoaded() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(cartIcon)
        ).isDisplayed();
    }

    public void addBackpackToCart() {
        wait.until(
                ExpectedConditions.elementToBeClickable(backpackAddButton)
        ).click();
    }

    public void openCart() {
        wait.until(
                ExpectedConditions.elementToBeClickable(cartIcon)
        ).click();
    }

    public void clickCart() {
        openCart();
    }
    public String getProductPrice(String productName) {

        String xpath =
                "//div[@class='inventory_item_name ' and text()='"
                        + productName
                        + "']/ancestor::div[@class='inventory_item_description']//div[@class='inventory_item_price']";

        return driver.findElement(By.xpath(xpath)).getText().trim();
    }
    public String getProductImageSrc(String productName) {

        String xpath =
                "//div[text()='"
                        + productName
                        + "']/ancestor::div[@class='inventory_item']//img";

        return driver.findElement(By.xpath(xpath))
                .getAttribute("src");
    }
    public boolean checkForVisualAnomalies() {

        String srcAttribute = driver
                .findElement(By.className("inventory_item_img"))
                .getAttribute("src");

        return srcAttribute.contains("sl-404")
                || srcAttribute.contains("sluka")
                || srcAttribute.contains("with-it-whte-shirt");
    }
    public File captureScreenshot(String fileName) {

        try {
            File screenshot =
                    ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            File destination = new File(
                    "src/test/resources/screenshots/" + fileName + ".png");

            FileUtils.copyFile(screenshot, destination);

            return destination;

        } catch (IOException e) {
            throw new RuntimeException("Failed to capture screenshot", e);
        }
    }
}