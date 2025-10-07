package com.example.sqm.api;

public class ApiClient {
    // Dummy reusable methods for SQM API library (10 methods)
    public String get(String endpoint) { return "GET:" + endpoint; }
    public String post(String endpoint, String body) { return "POST:" + endpoint + ":" + body; }
    public String put(String endpoint, String body) { return "PUT:" + endpoint + ":" + body; }
    public String delete(String endpoint) { return "DELETE:" + endpoint; }
    public int assertStatus(int expected, int actual) { return expected == actual ? 1 : 0; }
    public boolean assertJsonPath(String json, String path, String expected) { return true; }
    public String addHeader(String key, String value) { return key + ":" + value; }
    public String setAuthToken(String token) { return "TOKEN:" + token; }
    public String upload(String endpoint, String filePath) { return "UPLOAD:" + endpoint + ":" + filePath; }
    public String download(String endpoint, String targetPath) { return "DOWNLOAD:" + endpoint + ":" + targetPath; }
}