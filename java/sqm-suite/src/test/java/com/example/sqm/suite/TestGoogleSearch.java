package com.example.sqm.suite;

import com.example.sqm.web.WebClient;
import org.testng.annotations.Test;

public class TestGoogleSearch {
    @Test
    public void googleSearchAndClickFirst() {
        WebClient web = new WebClient();
        try {
            System.out.println(web.open("https://www.bing.com"));
            System.out.println(web.type("#sb_form_q", "Northern Trust"));
            System.out.println(web.pressEnter("#sb_form_q"));
            System.out.println(web.waitForVisibleXpath("//ol[@id='b_results']//li[contains(@class,'b_algo')]//h2"));
            System.out.println(web.tryClickXpath("(//ol[@id='b_results']//li[contains(@class,'b_algo')]//h2)[1]/ancestor::a[1]"));
        } finally {
            web.quit();
        }
    }
}