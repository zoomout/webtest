package com.mycompany.webtest;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

/**
 * Created by zoomout on 6/7/16.
 */
public class LocalTest {
    private WebDriver driver;

    @Test
    public void testChrome() {
        if (OsCheck.isWindows()) {
            System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
        }
        if (OsCheck.isMac()) {
            System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver");
        }
        driver = new ChromeDriver();
        driver.get("http://crossbrowsertesting.github.io/selenium_example_page.html");
        Assert.assertEquals(driver.getTitle(), "Selenium Test Example Page");
    }

    @Test(enabled = false)
    public void testFirefox() {
        driver = new FirefoxDriver(); //for firefox you don't have to specify driver file, it's built into Firefox browser
        driver.get("http://crossbrowsertesting.github.io/selenium_example_page.html");
        Assert.assertEquals(driver.getTitle(), "Selenium Test Example Page");

    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
