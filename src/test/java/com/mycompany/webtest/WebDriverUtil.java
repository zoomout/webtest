package com.mycompany.webtest;

import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by zoomout on 10/4/16.
 */
public class WebDriverUtil {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(WebDriverUtil.class.getSimpleName());

    private static final int
            REMOTE_RETRY = 2,
            RETRY_DELAY_MS = 5 * 1000;
    public static final int WEB_DRIVER_TIMEOUT_IN_SECONDS = 15;

    public RemoteWebDriver createRemoteDriver(DesiredCapabilities capabilities, boolean isMobile) {
        RemoteWebDriver driver = null;
        Exception exception = null;
        int retries = 0;
        while (driver == null && retries < REMOTE_RETRY) {
            retries++;
            try {
                driver = new RemoteWebDriver(BrowserStackHelper.getRemoteUrl(), capabilities);
            } catch (WebDriverException e) {
                driver = null;
                exception = e;
                try {
                    Thread.sleep(RETRY_DELAY_MS);
                } catch (InterruptedException e1) {
                    throw new RuntimeException(e1);
                }
            }
        }

        if (driver == null) {
            throw new RuntimeException(String.format("Unable to init RemoteWebDriver after [%s] retries", REMOTE_RETRY), exception);
        }

        LOG.info("Loaded remote web driver after [{}] retries", retries);

        if (!isMobile) {
            driver.manage().window().maximize();
        }

        driver.manage().timeouts().implicitlyWait(WEB_DRIVER_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
        return driver;
    }
}
