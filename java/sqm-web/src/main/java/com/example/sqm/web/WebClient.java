package com.example.sqm.web;

public class WebClient {
    public WebClient() {}

    public String open(String pathOrUrl) {
        return "OPEN:" + pathOrUrl;
    }

    public String click(String cssSelector) {
        return "CLICK:" + cssSelector;
    }

    public String tryClick(String cssSelector) {
        return "TRY_CLICK:" + cssSelector + ":MISS";
    }

    public String tryClickXpath(String xpath) {
        return "TRY_CLICK_XPATH:" + xpath + ":MISS";
    }

    public String type(String cssSelector, String text) {
        return "TYPE:" + cssSelector + ":" + text;
    }

    public String pressEnter(String cssSelector) {
        return "ENTER:" + cssSelector;
    }

    public String waitForVisible(String cssSelector) {
        return "WAIT_VISIBLE:" + cssSelector;
    }

    public String waitForVisibleXpath(String xpath) {
        return "WAIT_VISIBLE_XPATH:" + xpath;
    }

    public String assertText(String cssSelector, String expected) {
        return "ASSERT_TEXT:" + cssSelector + ":" + expected;
    }

    public String screenshot(String name) {
        return "SCREENSHOT:" + name;
    }

    public String login(String user, String pass) {
        return "LOGIN:" + user;
    }

    public String logout() {
        return "LOGOUT";
    }

    public String navigate(String path) {
        return "NAVIGATE:" + path;
    }

    public void quit() {
        // no-op stub
    }
}