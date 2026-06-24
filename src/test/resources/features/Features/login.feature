Feature: SauceDemo User Personas

  Scenario: Standard User Purchase Flow
    Given user is on login page
    When user logs in with "standard_user" and "secret_sauce"
    Then user completes purchase successfully

  Scenario: Locked User Validation
    Given user is on login page
    When user logs in with "locked_out_user" and "secret_sauce"
    Then user should see login error message "Epic sadface: Sorry, this user has been locked out."

  Scenario: Problem User Validation
    Given user is on login page
    When user logs in with "problem_user" and "secret_sauce"
    Then user should experience checkout validation issue

  Scenario: Performance User Validation
    Given user is on login page
    When user logs in with "performance_glitch_user" and "secret_sauce"
    Then user should reach inventory page despite delayed loading

  Scenario: Error User Validation
    Given user is on login page
    When user logs in with "error_user" and "secret_sauce"
    Then user should experience checkout completion anomaly

#  Scenario: Validate complete product catalog data
#    Given user is on login page
#    When user logs in with "visual_user" and "secret_sauce"
#    Then following products should be displayed
#      | Product                     | Price  |
#      | Sauce Labs Backpack         | $29.99 |
#      | Sauce Labs Bike Light       | $9.99  |
#      | Sauce Labs Bolt T-Shirt     | $15.99 |
#      | Sauce Labs Fleece Jacket    | $49.99 |
#      | Sauce Labs Onesie           | $7.99  |
#      | Test.allTheThings() T-Shirt | $15.99 |
#    Then verifyBackpackImage

  Scenario: Visual User Validation
    Given user is on login page
    When user logs in with "visual_user" and "secret_sauce"
    Then visual user page should differ from standard user page