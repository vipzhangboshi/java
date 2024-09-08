//package com.oyc.demo.util;
//
//import org.apache.http.HttpRequest;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//
//import java.net.URI;
//
//public class TokenManager {
//    private static final String REFRESH_TOKEN_URL = "https://example.com/api/refresh-token";
//    private String token;
//
//    public TokenManager(String initialToken) {
//        this.token = initialToken;
//    }
//
//    public String getToken() {
//        return token;
//    }
//
//    public void refresh() throws Exception {
//        HttpClient client = HttpClient.newHttpClient();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(REFRESH_TOKEN_URL))
//                .header("Authorization", "Bearer " + token)
//                .build();
//
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//        if (response.statusCode() == 200) {
//            // Assuming the new token is returned as plain text in the response body.
//            token = response.body().trim();
//        } else {
//            throw new RuntimeException("Failed to refresh token: " + response.statusCode());
//        }
//    }
//}