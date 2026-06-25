# Ejada Automation Framework

## Overview

This project is a Hybrid Test Automation Framework developed using Selenium WebDriver, Java, Cucumber (BDD), TestNG, and Maven.

The framework automates both UI and API test scenarios and supports cross-browser execution, visual validation, and reporting.

---

## Tech Stack

* Java 21
* Selenium WebDriver
* Cucumber BDD
* TestNG
* Maven
* Rest Assured
* WebDriverManager
* Allure Reports
* Apache POI
* Git & GitHub

---

## Framework Features

### UI Automation

* Page Object Model (POM) design pattern
* Cross-browser execution (Chrome & Firefox)
* Explicit waits implementation
* Externalized configuration using properties files
* Screenshot capture
* Visual validation testing
* Parallel execution support

### API Automation

* GET requests validation
* POST requests validation
* PUT requests validation
* DELETE requests validation
* Authentication validation
* JSON response verification

### Reporting

* Cucumber HTML Reports
* TestNG Reports
* Allure Reports

---

## Project Structure

```text
src
├── main
│   └── java
│
├── test
│   ├── java
│   │   ├── drivers
│   │   ├── hooks
│   │   ├── pages
│   │   ├── runners
│   │   ├── stepdefinitions
│   │   └── utils
│   │
│   └── resources
│       ├── config.properties
│       ├── features
│       └── screenshots
│
target
pom.xml
README.md
```

---

## Test Coverage

### UI Scenarios

* Standard User Purchase Flow
* Locked User Validation
* Problem User Validation
* Performance User Validation
* Error User Validation
* Visual User Validation

### API Scenarios

* Get All Books
* Get Book by ID
* Invalid Book Validation
* Create Order
* Update Order
* Delete Order
* Authentication Validation

---

## Design Patterns Used

* Page Object Model (POM)
* Factory Pattern
* Singleton Pattern
* Data-Driven Approach

---

## Configuration

Update application details in:

```properties
src/test/resources/config.properties
```

Example:

```properties
browser=chrome
url=https://www.saucedemo.com/
explicit.wait.seconds=15
```

---

## Execute Tests

Run all tests:

```bash
mvn clean test
```

Run tests with a specific browser:

```bash
mvn clean test -Dbrowser=chrome
```

```bash
mvn clean test -Dbrowser=firefox
```

---

## Generate Allure Report

Execute tests:

```bash
mvn clean test
```

Generate report:

```bash
allure generate allure-results --clean -o allure-report
```

Open report:

```bash
allure open allure-report
```

---

## Sample Reports

* Cucumber HTML Report
* TestNG Report
* Allure Report

---

## Future Enhancements

* Jenkins CI/CD integration
* Docker support
* Selenium Grid execution
* Extent Reports integration
* GitHub Actions pipeline

---

## Author

Naresh Kumar

Automation Test Engineer
