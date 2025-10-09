package com.example.sqm.suite;

import com.example.sqm.web.WebClient;
import org.testng.annotations.Test;

public class TestOrangeHrmLogin {
    @Test
    public void loginAndVerifyDashboard() {
        WebClient web = new WebClient();
        try {
            System.out.println(web.open("https://opensource-demo.orangehrmlive.com/"));
            System.out.println(web.type("input[name='username']", "Admin"));
            System.out.println(web.type("input[name='password']", "admin123"));
            System.out.println(web.click("button[type='submit']"));
            // Dashboard header varies slightly; use robust CSS and assert text
            System.out.println(web.waitForVisible("[class*='oxd-topbar-header-title']"));
            System.out.println(web.assertText("[class*='oxd-topbar-header-title']", "Dashboard"));
            System.out.println(web.screenshot("orangehrm-dashboard"));
        } finally {
            web.quit();
        }
    }
}