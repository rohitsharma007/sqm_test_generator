package com.example.sqm.suite;

import org.testng.annotations.Test;
import com.example.sqm.web.WebClient;

public class TestOpenCartAdminLogo {
    private final WebClient web = new WebClient();

    @Test
    public void verifyAdminLogoVisible() {
        try {
            // Launch and navigate (headed)
            System.out.println(web.open("https://demo.opencart.com/admin/"));
            sleep(1500);

            // Wait explicitly for the logo image (robust XPath using alt contains)
            System.out.println(web.waitForVisibleXpath("//img[contains(@alt,'OpenCart')]"));
            sleep(1000);

            // Screenshot for reporting
            System.out.println(web.screenshot("opencart_admin_logo.png"));

            // Keep the browser visible briefly so you can see it
            sleep(2000);
        } finally {
            web.quit();
        }
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}