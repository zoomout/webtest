package com.mycompany.webtest;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by zoomout on 5/23/16.
 */
class BrowserStackHelper {

    private static final String BROWSER_STACK_USER_NAME = "";
    private static final String BROWSER_STACK_AUTH_KEY = "";
    private static final String BROWSER_STACK_REMOTE_URL_TEMPLATE = "https://%s:%s@hub.browserstack.com/wd/hub";

    public static URL getRemoteUrl() {
        URL url;
        String urlStr = String.format(BROWSER_STACK_REMOTE_URL_TEMPLATE, BROWSER_STACK_USER_NAME, BROWSER_STACK_AUTH_KEY);
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            throw new RuntimeException(String.format("Malformed URL: %s", urlStr), e);
        }
        return url;
    }

    public static JsonNode setScore(String seleniumTestId, String score) throws UnirestException {
        // Mark a Selenium test as Pass/Fail
        HttpResponse<JsonNode> response = Unirest.put("http://crossbrowsertesting.com/api/v3/selenium/{seleniumTestId}")
                .basicAuth(BROWSER_STACK_USER_NAME, BROWSER_STACK_AUTH_KEY)
                .routeParam("seleniumTestId", seleniumTestId)
                .field("action", "set_score")
                .field("score", score)
                .asJson();
        return response.getBody();
    }

    public static String takeSnapshot(String seleniumTestId) throws UnirestException {
        /*
         * Takes a snapshot of the screen for the specified test.
         * The output of this function can be used as a parameter for setDescription()
         */
        HttpResponse<JsonNode> response = Unirest.post("http://crossbrowsertesting.com/api/v3/selenium/{seleniumTestId}/snapshots")
                .basicAuth(BROWSER_STACK_USER_NAME, BROWSER_STACK_AUTH_KEY)
                .routeParam("seleniumTestId", seleniumTestId)
                .asJson();
        // grab out the snapshot "hash" from the response
        String snapshotHash = (String) response.getBody().getObject().get("hash");
        System.out.println(snapshotHash);
        return snapshotHash;
    }

    public static JsonNode setDescription(String seleniumTestId, String snapshotHash, String description) throws UnirestException {
        /*
         * sets the description for the given seleniumTestId and snapshotHash
         */
        HttpResponse<JsonNode> response = Unirest.put("http://crossbrowsertesting.com/api/v3/selenium/{seleniumTestId}/snapshots/{snapshotHash}")
                .basicAuth(BROWSER_STACK_USER_NAME, BROWSER_STACK_AUTH_KEY)
                .routeParam("seleniumTestId", seleniumTestId)
                .routeParam("snapshotHash", snapshotHash)
                .field("description", description)
                .asJson();
        return response.getBody();
    }



}
