package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;

import java.time.Duration;
import java.util.List;

public class CartPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By cartItems = By.cssSelector(".cart_item");
    private final By checkoutButton = By.cssSelector("#checkout");
    private final By continueShoppingButton = By.id("continue-shopping");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("explicit.wait.seconds", 15)));
    }

    public boolean containsProduct(String productName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("cart_list")));
        return driver.findElements(By.className("inventory_item_name"))
                .stream()
                .anyMatch(item -> item.getText().equals(productName));
    }

    public int itemCount() {
        return driver.findElements(cartItems).size();
    }

    public void removeProduct(String productName) {
        WebElement item = driver.findElements(cartItems)
                .stream()
                .filter(cartItem -> cartItem.findElement(By.className("inventory_item_name")).getText().equals(productName))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Cart product not found: " + productName));
        item.findElement(By.tagName("button")).click();
    }

    public void checkout() {
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();
    }

    public void continueShopping() {
        wait.until(ExpectedConditions.elementToBeClickable(continueShoppingButton)).click();
    }

    public List<String> productNames() {
        return driver.findElements(By.className("inventory_item_name"))
                .stream()
                .map(WebElement::getText)
                .toList();
    }
}
