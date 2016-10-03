package com.bogdan.project;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

/**
 * Created by zoomout on 6/7/16.
 */
public class DebuggerDetectionTest {
    private WebDriver driver;

    @Test
    public void testDebugger() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "/Users/zoomout/work/chromedriver");
        driver = new ChromeDriver();
        driver.get("https://jsfiddle.net/evnrorea/embedded/result/");
        WebElement iframe = driver.findElement(By.xpath("//iframe"));
        driver.switchTo().frame(iframe);
        WebElement element = driver.findElement(By.id("devtool-status"));
        Thread.sleep(200);
        System.out.println("Status is: " + element.getText());
    }

    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
}
