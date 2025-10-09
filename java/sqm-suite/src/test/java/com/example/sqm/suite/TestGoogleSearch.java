package com.example.sqm.suite;

import com.example.sqm.web.WebClient;
import org.testng.annotations.Test;

public class TestGoogleSearch {
    @Test
    public void googleSearchAndClickFirst() {
        WebClient web = new WebClient();
        try {
            System.out.println(web.open("https://www.bing.com"));
            sleep(800);
            System.out.println(web.type("#sb_form_q", "Northern Trust"));
            System.out.println(web.pressEnter("#sb_form_q"));
            sleep(500);
            // Use a robust CSS selector for the first organic result link
            System.out.println(web.waitForVisible("#b_results .b_algo h2 a"));
            System.out.println(web.click("#b_results .b_algo h2 a"));
            // Wait until navigation leaves Bing results page
            for (int i = 0; i < 20; i++) { // up to ~10s
                String current = web.getCurrentUrl();
                if (current != null && !current.contains("bing.com")) {
                    break;
                }
                sleep(500);
            }
            System.out.println(web.screenshot("bing-first-result"));
        } finally {
            web.quit();
        }
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}