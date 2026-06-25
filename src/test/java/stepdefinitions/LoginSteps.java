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

    System.out.println("\n========== STANDARD USER TEST STARTED ==========");

    inventoryPage.captureScreenshot("standard_user");
    System.out.println("✓ Baseline screenshot captured");

    inventoryPage.addBackpackToCart();
    System.out.println("✓ Product added to cart");

    inventoryPage.clickCart();
    System.out.println("✓ Navigated to Cart page");

    cartPage.checkout();
    System.out.println("✓ Checkout initiated");

    checkoutPage.enterFirstName("John");
    checkoutPage.enterLastName("Doe");
    checkoutPage.enterPostalCode("12345");

    checkoutPage.clickContinue();
    checkoutPage.finish();

    Assert.assertTrue(
            checkoutPage.getConfirmationMessage().contains("Thank you"),
            "Order was not completed successfully");

    System.out.println("✅ STANDARD USER: Purchase completed successfully");
    System.out.println("=================================================\n");
}
// ==========================
// LOCKED USER
// ==========================

    @Then("user should see login error message {string}")
    public void user_should_see_login_error_message(String expectedMessage) {

        String actualMessage = loginPage.getErrorMessage();

        System.out.println("\n========== LOCKED USER TEST ==========");
        System.out.println("Expected Error : " + expectedMessage);
        System.out.println("Actual Error   : " + actualMessage);

        Assert.assertEquals(
                actualMessage.trim(),
                expectedMessage,
                "Login error message mismatch");

        System.out.println("✅ LOCKED USER validation successful");
        System.out.println("======================================\n");
    }

// ==========================
// PROBLEM USER
// ==========================

    @Then("user should experience checkout validation issue")
    public void user_should_experience_checkout_validation_issue() {

        System.out.println("\n========== PROBLEM USER TEST ==========");

        inventoryPage.addBackpackToCart();
        inventoryPage.clickCart();
        cartPage.checkout();

        checkoutPage.enterFirstName("John");
        checkoutPage.enterLastName("");
        checkoutPage.enterPostalCode("12345");

        checkoutPage.clickContinue();

        String actualError = checkoutPage.getErrorMessage().trim();

        System.out.println("Validation Error : " + actualError);

        Assert.assertEquals(
                actualError,
                "Error: Last Name is required");

        System.out.println("✅ PROBLEM USER validation completed");
        System.out.println("======================================\n");
    }
// ==========================
// PERFORMANCE USER
// ==========================

    @Then("user should reach inventory page despite delayed loading")
    public void user_should_reach_inventory_page_despite_delayed_loading() {

        System.out.println("\n========== PERFORMANCE USER TEST ==========");

        Assert.assertTrue(
                inventoryPage.isLoaded(),
                "Inventory page did not load within expected time");

        System.out.println("✅ PERFORMANCE USER page loaded successfully");
        System.out.println("===========================================\n");
    }
// ==========================
// ERROR USER
// ==========================

    @Then("user should experience checkout completion anomaly")
    public void user_should_experience_checkout_completion_anomaly() {

        System.out.println("\n========== ERROR USER TEST ==========");

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

            System.out.println("✅ ERROR USER completed checkout");
            System.out.println("Confirmation : " + confirmationMessage);

        } catch (Exception e) {

            System.out.println("⚠ Expected anomaly detected for ERROR USER");
            System.out.println("Exception : " + e.getMessage());
        }

        System.out.println("======================================\n");
    }
// ==========================
// VISUAL USER
// ==========================

    @Then("visual user page should differ from standard user page")
    public void visual_user_page_should_differ_from_standard_user_page()
            throws Exception {

        System.out.println("\n========== VISUAL USER TEST ==========");

        File standardScreenshot =
                new File("src/test/resources/screenshots/standard_user.png");

        File visualScreenshot =
                inventoryPage.captureScreenshot("visual_user");

        System.out.println("Baseline Screenshot : "
                + standardScreenshot.getAbsolutePath());

        System.out.println("Actual Screenshot   : "
                + visualScreenshot.getAbsolutePath());

        boolean areSame =
                ImageComparisonUtil.imagesAreSame(
                        standardScreenshot,
                        visualScreenshot);

        if (areSame) {

            System.out.println("❌ No visual differences detected");
            Assert.fail(
                    "Expected visual differences, but screenshots are identical.");
        }

        System.out.println("✅ Visual differences detected successfully");
        System.out.println("======================================\n");
    }
}
