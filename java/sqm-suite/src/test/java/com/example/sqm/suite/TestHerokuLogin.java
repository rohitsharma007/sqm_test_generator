package com.example.sqm.suite;

import com.example.sqm.web.WebClient;
import org.testng.annotations.Test;

public class TestHerokuLogin {
    @Test
    public void loginWithValidCredentials() {
        WebClient web = new WebClient();
        try {
            System.out.println(web.open("https://the-internet.herokuapp.com/login"));
            System.out.println(web.type("#username", "tomsmith"));
            System.out.println(web.type("#password", "SuperSecretPassword!"));
            System.out.println(web.click("button[type='submit']"));
            System.out.println(web.waitForVisible("#flash"));
            System.out.println(web.assertText("#flash", "You logged into a secure area!"));
            System.out.println(web.screenshot("heroku-login-success"));
        } finally {
            web.quit();
        }
    }
}