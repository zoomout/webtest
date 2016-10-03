package com.mycompany.webtest;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

public class CrossBrowserTest {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(CrossBrowserTest.class.getSimpleName());

    private String testScore = "unset";
    private boolean doMaximize = false;
    private RemoteWebDriver driver;


    @Parameters(value = {"driverType", "driverConfig"})
    @BeforeClass
    public void beforeClass(String driverType, String driverConfig) throws MalformedURLException {
        boolean isMobile = driverType.equals("mobile");
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("browserstack.debug", "true");
        caps.setCapability("acceptSslCerts", "true");
        String[] driverConfigArray = driverConfig.split(":");

        if (isMobile) {
            Assert.assertEquals(driverConfigArray.length, 3, String.format("Wrong config format for %s WebDriver", driverType));
            caps.setCapability("browserName", driverConfigArray[0]);
            caps.setCapability("platform", driverConfigArray[1]);
            caps.setCapability("device", driverConfigArray[2]);
        } else {
            Assert.assertEquals(driverConfigArray.length, 5, String.format("Wrong config format for %s WebDriver", driverType));
            caps.setCapability("browser", driverConfigArray[0]);
            caps.setCapability("browser_version", driverConfigArray[1]);
            caps.setCapability("os", driverConfigArray[2]);
            caps.setCapability("os_version", driverConfigArray[3]);
            caps.setCapability("resolution", driverConfigArray[4]);
        }
        LOG.info("Configured Remote Web Driver " + caps.toString());

        driver = new WebDriverUtil().createRemoteDriver(caps, isMobile);
    }

    @Test
    public void testOne() throws Exception {

        LOG.info(driver.getSessionId().toString());
        // we wrap the test in a try catch loop so we can log assert failures in our system
        try {
            // load the page url
            LOG.info("Loading Url");
            driver.get("http://crossbrowsertesting.github.io/selenium_example_page.html");
            if (doMaximize) {
                // maximize the window - DESKTOPS ONLY
                LOG.info("Maximizing window");
                driver.manage().window().maximize();
            }
            // Check the page title (try changing to make the assertion fail!)
            Assert.assertEquals(driver.getTitle(), "Selenium Test Example Page");
            // if we get to this point, then all the assertions have passed
            // that means that we can set the score to pass in our system
            testScore = "pass";
        } catch (AssertionError ae) {
            // if we have an assertion error, take a snapshot of where the test fails
            // and set the score to "fail"
            String snapshotHash = BrowserStackHelper.takeSnapshot(driver.getSessionId().toString());
            BrowserStackHelper.setDescription(driver.getSessionId().toString(), snapshotHash, ae.toString());
            testScore = "fail";
        } finally {
            // here we make an api call to actually send the score
            BrowserStackHelper.setScore(driver.getSessionId().toString(), testScore);
        }

    }

    @Test
    public void testTwo() throws Exception {

        LOG.info(driver.getSessionId().toString());
        // we wrap the test in a try catch loop so we can log assert failures in our system
        try {
            // load the page url
            LOG.info("Loading Url");
            driver.get("https://www.wikipedia.org");
            if (doMaximize) {
                // maximize the window - DESKTOPS ONLY
                LOG.info("Maximizing window");
                driver.manage().window().maximize();
            }
            // Check the page title (try changing to make the assertion fail!)
            Assert.assertEquals(driver.getTitle(), "Wikipedia");
            // if we get to this point, then all the assertions have passed
            // that means that we can set the score to pass in our system
            testScore = "pass";
        } catch (AssertionError ae) {
            // if we have an assertion error, take a snapshot of where the test fails
            // and set the score to "fail"
            String snapshotHash = BrowserStackHelper.takeSnapshot(driver.getSessionId().toString());
            BrowserStackHelper.setDescription(driver.getSessionId().toString(), snapshotHash, ae.toString());
            testScore = "fail";
        } finally {
            // here we make an api call to actually send the score
            BrowserStackHelper.setScore(driver.getSessionId().toString(), testScore);
        }

    }

    @AfterClass
    public void afterClass() {
        // and quit the driver
        driver.quit();
    }
}