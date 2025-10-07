package com.example.sqm.web;

public class WebClient {
    // Dummy reusable methods for SQM Web library (10 methods)
    public String open(String url) { return "OPEN:" + url; }
    public String click(String selector) { return "CLICK:" + selector; }
    public String type(String selector, String text) { return "TYPE:" + selector + ":" + text; }
    public String select(String selector, String value) { return "SELECT:" + selector + ":" + value; }
    public String waitForVisible(String selector) { return "WAIT_VISIBLE:" + selector; }
    public String assertText(String selector, String expected) { return "ASSERT_TEXT:" + selector + ":" + expected; }
    public String screenshot(String name) { return "SCREENSHOT:" + name; }
    public String login(String user, String pass) { return "LOGIN:" + user; }
    public String logout() { return "LOGOUT"; }
    public String navigate(String path) { return "NAVIGATE:" + path; }
}