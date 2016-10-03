package com.bogdan.project; /**
 */

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.testng.Assert.assertEquals;


public class TestCase {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(TestCase.class.getSimpleName());

    private String testScore = "unset";
    private boolean doMaximize = false;
    private RemoteWebDriver driver;

    @Parameters(value = {"browser_api_name", "os_api_name", "screen_resolution"})
    @BeforeClass
    public void beforeClass(String browser_api_name, String os_api_name, String screen_resolution) throws MalformedURLException {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
        String formattedDate = sdf.format(date);

        DesiredCapabilities caps = new DesiredCapabilities();

        caps.setCapability("name", "Selenium Test Example");
        caps.setCapability("build", formattedDate);
        caps.setCapability("browser_api_name", browser_api_name);
        caps.setCapability("os_api_name", os_api_name);
        caps.setCapability("screen_resolution", screen_resolution);
        caps.setCapability("record_video", "true");
        caps.setCapability("record_network", "true");

        if (!browser_api_name.contains("Mbl")) {
            doMaximize = true;
        }

        driver = new RemoteWebDriver(
                new URL("http://" + Helper.username + ":" + Helper.authkey + "@hub.crossbrowsertesting.com:80/wd/hub"), caps);
//        driver = new FirefoxDriver();

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
            assertEquals(driver.getTitle(), "Selenium Test Example Page");
            // if we get to this point, then all the assertions have passed
            // that means that we can set the score to pass in our system
            testScore = "pass";
        } catch (AssertionError ae) {
            // if we have an assertion error, take a snapshot of where the test fails
            // and set the score to "fail"
            String snapshotHash = Helper.takeSnapshot(driver.getSessionId().toString());
            Helper.setDescription(driver.getSessionId().toString(), snapshotHash, ae.toString());
            testScore = "fail";
        } finally {
            // here we make an api call to actually send the score
            Helper.setScore(driver.getSessionId().toString(), testScore);
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
            assertEquals(driver.getTitle(), "Wikipedia");
            // if we get to this point, then all the assertions have passed
            // that means that we can set the score to pass in our system
            testScore = "pass";
        } catch (AssertionError ae) {
            // if we have an assertion error, take a snapshot of where the test fails
            // and set the score to "fail"
            String snapshotHash = Helper.takeSnapshot(driver.getSessionId().toString());
            Helper.setDescription(driver.getSessionId().toString(), snapshotHash, ae.toString());
            testScore = "fail";
        } finally {
            // here we make an api call to actually send the score
            Helper.setScore(driver.getSessionId().toString(), testScore);
        }

    }

    @AfterClass
    public void afterClass() {
        // and quit the driver
        driver.quit();
    }
}