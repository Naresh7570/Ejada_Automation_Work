package stepdefinitions;

import drivers.DriverFactory;
import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.CartPage;
import pages.CheckoutPage;
import pages.InventoryPage;
import pages.LoginPage;
import utils.ImageComparisonUtil;


import java.io.File;

public class LoginSteps {

    private final LoginPage loginPage;
    private final InventoryPage inventoryPage;
    private final CartPage cartPage;
    private final CheckoutPage checkoutPage;

    public LoginSteps() {
        WebDriver driver = DriverFactory.getDriver();

        this.loginPage = new LoginPage(driver);
        this.inventoryPage = new InventoryPage(driver);
        this.cartPage = new CartPage(driver);
        this.checkoutPage = new CheckoutPage(driver);
    }

    @Given("user is on login page")
    public void user_is_on_login_page() {
        loginPage.open();
    }

    @When("user logs in with {string} and {string}")
    public void user_logs_in_with_credentials(String username, String password) {
        loginPage.login(username, password);
    }

// ==========================
// STANDARD USER
// ==========================

    @Then("user completes purchase successfully")
    public void user_completes_purchase_successfully() {

        // Take baseline screenshot of inventory page
        inventoryPage.captureScreenshot("standard_user");

        inventoryPage.addBackpackToCart();

        inventoryPage.clickCart();

        cartPage.checkout();

        checkoutPage.enterFirstName("John");
        checkoutPage.enterLastName("Doe");
        checkoutPage.enterPostalCode("12345");

        checkoutPage.clickContinue();

        checkoutPage.finish();

        Assert.assertTrue(
                checkoutPage.getConfirmationMessage().contains("Thank you"),
                "Order was not completed successfully");
    }

// ==========================
// LOCKED USER
// ==========================

    @Then("user should see login error message {string}")
    public void user_should_see_login_error_message(String expectedMessage) {

        String actualMessage = loginPage.getErrorMessage();

        Assert.assertEquals(
                actualMessage.trim(),
                expectedMessage,
                "Login error message mismatch");
    }

// ==========================
// PROBLEM USER
// ==========================

    @Then("user should experience checkout validation issue")
    public void user_should_experience_checkout_validation_issue() {
        inventoryPage.addBackpackToCart();

        inventoryPage.clickCart();

        cartPage.checkout();

        checkoutPage.enterFirstName("John");
        checkoutPage.enterLastName("");
        checkoutPage.enterPostalCode("12345");

        checkoutPage.clickContinue();

        Assert.assertEquals(
                checkoutPage.getErrorMessage().trim(),
                "Error: Last Name is required");
    }

// ==========================
// PERFORMANCE USER
// ==========================

    @Then("user should reach inventory page despite delayed loading")
    public void user_should_reach_inventory_page_despite_delayed_loading() {

        Assert.assertTrue(
                inventoryPage.isLoaded(),
                "Inventory page did not load within expected time");
    }

// ==========================
// ERROR USER
// ==========================

    @Then("user should experience checkout completion anomaly")
    public void user_should_experience_checkout_completion_anomaly() {

        inventoryPage.addBackpackToCart();

        inventoryPage.clickCart();

        cartPage.checkout();

        checkoutPage.enterFirstName("John");
        checkoutPage.enterLastName("Doe");
        checkoutPage.enterPostalCode("12345");

        checkoutPage.clickContinue();

        try {

            checkoutPage.finish();

            String confirmationMessage =
                    checkoutPage.getConfirmationMessage();

            Assert.assertNotNull(
                    confirmationMessage,
                    "Confirmation message was not displayed");

        } catch (Exception e) {

            System.out.println(
                    "Expected error_user anomaly detected: "
                            + e.getMessage());
        }
    }

// ==========================
// VISUAL USER
// ==========================

    @Then("visual user page should differ from standard user page")
    public void visual_user_page_should_differ_from_standard_user_page()
            throws Exception {

        File standardScreenshot =
                new File("src/test/resources/screenshots/standard_user.png");

        File visualScreenshot =
                inventoryPage.captureScreenshot("visual_user");

        System.out.println("Standard: " + standardScreenshot.getAbsolutePath());
        System.out.println("Exists: " + standardScreenshot.exists());

        System.out.println("Visual: " + visualScreenshot.getAbsolutePath());
        System.out.println("Exists: " + visualScreenshot.exists());
        Assert.assertTrue(
                ImageComparisonUtil.imagesAreDifferent(
                        standardScreenshot,
                        visualScreenshot),
                "Visual user page appears identical to standard user page");
    }
}
