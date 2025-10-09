package com.example.sqm.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

public class WebClient {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public WebClient() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        // Headed mode: ensure no --headless flag
        options.addArguments("--start-maximized");
        this.driver = new ChromeDriver(options);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public String open(String pathOrUrl) {
        driver.get(pathOrUrl);
        return "OPEN:" + pathOrUrl;
    }

    public String click(String cssSelector) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(cssSelector)));
        el.click();
        return "CLICK:" + cssSelector;
    }

    public String tryClick(String cssSelector) {
        try {
            return click(cssSelector);
        } catch (Exception e) {
            return "TRY_CLICK:" + cssSelector + ":MISS";
        }
    }

    public String tryClickXpath(String xpath) {
        try {
            WebElement el = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
            el.click();
            return "CLICK_XPATH:" + xpath;
        } catch (Exception e) {
            return "TRY_CLICK_XPATH:" + xpath + ":MISS";
        }
    }

    public String type(String cssSelector, String text) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSelector)));
        el.clear();
        el.sendKeys(text);
        return "TYPE:" + cssSelector + ":" + text;
    }

    public String pressEnter(String cssSelector) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSelector)));
        el.sendKeys(Keys.ENTER);
        return "ENTER:" + cssSelector;
    }

    public String waitForVisible(String cssSelector) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSelector)));
        return "WAIT_VISIBLE:" + cssSelector;
    }

    public String waitForVisibleXpath(String xpath) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        return "WAIT_VISIBLE_XPATH:" + xpath;
    }

    public String assertText(String cssSelector, String expected) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSelector)));
        String actual = el.getText();
        if (!actual.contains(expected)) {
            throw new AssertionError("Expected text to contain '" + expected + "' but was '" + actual + "'");
        }
        return "ASSERT_TEXT:" + cssSelector + ":" + expected;
    }

    public String screenshot(String name) {
        try {
            Path target = Paths.get("target").resolve("screenshots")
                    .resolve(name.endsWith(".png") ? name : name + ".png");
            Files.createDirectories(target.getParent());
            byte[] data = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Files.write(target, data);
            return "SCREENSHOT:" + target.toAbsolutePath();
        } catch (Exception e) {
            return "SCREENSHOT_ERROR:" + e.getMessage();
        }
    }

    public String login(String user, String pass) {
        return "LOGIN:" + user; // left as framework convenience; specific flows live in tests
    }

    public String logout() {
        return "LOGOUT";
    }

    public String navigate(String path) {
        driver.navigate().to(path);
        return "NAVIGATE:" + path;
    }

    public String getCurrentUrl() {
        try {
            return driver.getCurrentUrl();
        } catch (Exception e) {
            return "URL_ERROR:" + e.getMessage();
        }
    }

    public String getTitle() {
        try {
            return driver.getTitle();
        } catch (Exception e) {
            return "TITLE_ERROR:" + e.getMessage();
        }
    }

    public void quit() {
        try { driver.quit(); } catch (Exception ignored) {}
    }
}