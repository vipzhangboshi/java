package com.oyc.demo.util;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class TokenAwareHttpClient {

    private OkHttpClient client;
    private String refreshTokenUrl;
    private String currentToken;

    public TokenAwareHttpClient(String refreshTokenUrl) {
        this.refreshTokenUrl = refreshTokenUrl;
        this.client = new OkHttpClient.Builder()
                .addInterceptor(new TokenInterceptor())
                .build();
    }

    private class TokenInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();

            // 假设token在Header的Authorization字段中
            Request request = original.newBuilder()
                    .header("Authorization", "Bearer " + currentToken)
                    .build();

            try (Response response = chain.proceed(request)) {
                if (!response.isSuccessful() && response.code() == 401) {
                    // Token过期，尝试刷新Token
                    String newToken = refreshToken();
                    if (newToken != null) {
                        // 使用新Token重新发起请求
                        return chain.proceed(original.newBuilder()
                                .header("Authorization", "Bearer " + newToken)
                                .build());
                    }
                }
                return response;
            }
        }

        private String refreshToken() {
            // 这里实现调用刷新Token的API，并解析返回的新Token
            // 模拟返回新Token，实际应替换为网络请求
            return "new_token_here";
        }
    }

    public Response execute(Request request) throws IOException {
        return client.newCall(request).execute();
    }

    // 示例方法，用于发起GET请求
    public Response get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        return execute(request);
    }

    public static void main(String[] args) {
        TokenAwareHttpClient client = new TokenAwareHttpClient("http://example.com/refresh_token");
        try (Response response = client.get("http://example.com/protected_resource")) {
            if (response.isSuccessful()) {
                // 处理成功响应
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}