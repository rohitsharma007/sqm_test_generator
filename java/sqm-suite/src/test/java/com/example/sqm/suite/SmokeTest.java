package com.example.sqm.suite;

import org.testng.annotations.Test;
import com.example.sqm.api.ApiClient;
import com.example.sqm.web.WebClient;

public class SmokeTest {
    @Test
    public void smoke() {
        ApiClient api = new ApiClient();
        WebClient web = new WebClient();
        api.get("/health");
        web.open("/");
    }
}
