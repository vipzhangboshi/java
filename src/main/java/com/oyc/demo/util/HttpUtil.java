package com.oyc.demo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.UnsupportedCharsetException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;


/**
 * Created by yueguangli on 18/6/12. Http访问工具类
 */
@Slf4j
public class HttpUtil {

    private static final String DEFAULT_CHARSET = "UTF-8";

    private static final String GB2312 = "GB2312";

    private static final String METHOD_GET = "GET";

    private static final int CONNECTTIMEOUT = 3000;

    private static final int READTIMEOUT = 3000;

    @Value("${shitu.app.key}")
    private static String appKey;
    @Value("${shitu.app.secret}")
    private static String appSecret;

    /**
     * 证书管理
     */
    private static class DefaultTrustManager implements X509TrustManager {

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] cert, String oauthType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] cert, String oauthType) throws CertificateException {
        }
    }

    /**
     * 创建连接
     *
     * @param url
     * @param method
     * @param contentType
     * @return
     * @throws IOException
     */
    private static HttpURLConnection getConnection(URL url, String method, String contentType) throws IOException {
        HttpURLConnection conn;
        // 判断连接协议
        if ("https".equals(url.getProtocol())) {
            SSLContext ctx;
            try {
                // 用TLS安全传输层协议
                ctx = SSLContext.getInstance("TLS");
                ctx.init(new KeyManager[0], new TrustManager[]{new DefaultTrustManager()},
                        new SecureRandom());
            } catch (Exception e) {
                throw new IOException(e);
            }
            HttpsURLConnection connHttps = (HttpsURLConnection) url.openConnection();
            connHttps.setSSLSocketFactory(ctx.getSocketFactory());
            connHttps.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    // 默认都认证通过
                    return true;
                }
            });
            conn = connHttps;
        } else {
            conn = (HttpURLConnection) url.openConnection();
        }
        conn.setRequestMethod(method);
        conn.setDoInput(true); // 允许输入
        conn.setDoOutput(true);// 允许输出

        conn.setRequestProperty("Content-Type", contentType);
        conn.setRequestProperty("Connection", "Keep-Alive");    // 设置连接持续有效
        return conn;

    }

    /**
     * 请求数据放在body中请求
     *
     * @param path
     * @param data
     * @return
     * @throws Exception
     */
    public static String post(String path, String data) {
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(path);
            post.addHeader("Content-Type", "application/json; charset=UTF-8");
            StringEntity entity = new StringEntity(data, "utf-8");
            post.setEntity(entity);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECTTIMEOUT).setConnectTimeout(READTIMEOUT).build();
            post.setConfig(requestConfig);
            HttpResponse response = client.execute(post);
            String result = EntityUtils.toString(response.getEntity());
            log.info("httputil-调用接口-path-{} data-{} result-{}", path, data, result);
            return result;
        } catch (UnsupportedCharsetException | ParseException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 请求数据放在body中请求
     *
     * @param path
     * @param data
     * @return
     * @throws Exception
     */
    public static String specialPost(String path, String data, int connectTimeOut, int readTimeOut) {
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(path);
            post.addHeader("Content-Type", "application/json; charset=UTF-8");
            StringEntity entity = new StringEntity(data, "utf-8");
            post.setEntity(entity);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(connectTimeOut).setConnectTimeout(readTimeOut).build();
            post.setConfig(requestConfig);
            HttpResponse response = client.execute(post);
            String result = EntityUtils.toString(response.getEntity());
            log.info("httputil-调用接口-path-{} data-{} result-{}", path, data, result);
            return result;
        } catch (UnsupportedCharsetException | ParseException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String headerPost(String path, String data, String token) {
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(path);
            post.addHeader("Content-Type", "application/json; charset=UTF-8");
            post.addHeader("Authorization", "Bearer " + token);
            StringEntity entity = new StringEntity(data, "utf-8");
            post.setEntity(entity);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECTTIMEOUT).setConnectTimeout(READTIMEOUT).build();
            post.setConfig(requestConfig);
            HttpResponse response = client.execute(post);
            return EntityUtils.toString(response.getEntity());
        } catch (UnsupportedCharsetException | ParseException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String headerAutoPost(String data, HttpPost post) {
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            StringEntity entity = new StringEntity(data, "utf-8");
            post.setEntity(entity);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECTTIMEOUT).setConnectTimeout(READTIMEOUT).build();
            post.setConfig(requestConfig);
            HttpResponse response = client.execute(post);
            return EntityUtils.toString(response.getEntity());
        } catch (UnsupportedCharsetException | ParseException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 请求数据放在body中请求
     *
     * @param path
     * @param params
     * @return
     * @throws Exception
     */
    public static String postBodyWithHeader(String path, Map<String, Object> params, String token) throws IOException {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(path);
        post.addHeader("Authorization", token);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECTTIMEOUT).setConnectTimeout(READTIMEOUT).build();
        post.setConfig(requestConfig);
        if (params != null && params.size() > 0) {
            List<NameValuePair> nvps = new ArrayList<>();
            Set<String> keySet = params.keySet();
            for (String key : keySet) {
                nvps.add(new BasicNameValuePair(key, (String) params.get(key)));
            }
            post.setEntity(new UrlEncodedFormEntity(nvps, DEFAULT_CHARSET));
        }
        HttpResponse response = client.execute(post);
        return EntityUtils.toString(response.getEntity());
    }

    public static String headerPostByVersion(String path, String data) {
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(path);
            post.addHeader("Content-Type", "application/json; charset=UTF-8");
            post.addHeader("version", "1.0.0");
            StringEntity entity = new StringEntity(data, "utf-8");
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            return EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (UnsupportedCharsetException | ParseException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String headerPostByMessageHeaders(String path, String data, Map<String, String> map) {
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(path);
            for (String key : map.keySet()) {
                post.addHeader(key, map.get(key));
            }
            StringEntity entity = new StringEntity(data, "utf-8");
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            return EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (UnsupportedCharsetException | ParseException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 请求数据放在body中请求
     *
     * @param path
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] post(String path, byte[] data) throws Exception {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(path);
        if (data != null && data.length > 0) {
            ByteArrayEntity entity = new ByteArrayEntity(data);
            post.setEntity(entity);
        }
        HttpResponse response = client.execute(post);
        return EntityUtils.toByteArray(response.getEntity());
    }

    /**
     * 请求数据放在body中请求
     *
     * @param path
     * @param params
     * @return
     * @throws Exception
     */
    public static String postBody(String path, Map<String, String> params) throws IOException {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(path);
        if (params != null && params.size() > 0) {
            List<NameValuePair> nvps = new ArrayList<>();
            Set<String> keySet = params.keySet();
            for (String key : keySet) {
                nvps.add(new BasicNameValuePair(key, params.get(key)));
            }
            post.setEntity(new UrlEncodedFormEntity(nvps, DEFAULT_CHARSET));
        }
        HttpResponse response = client.execute(post);
        return EntityUtils.toString(response.getEntity());
    }

    /**
     * 通过get访问 默认编码设置为UTF-8
     *
     * @param url    连接地址
     * @param params 参数
     * @return
     * @throws IOException
     */
    public static String doGet(String url, Map<String, Object> params) throws IOException {
        return doGet(url, params, DEFAULT_CHARSET);
    }

    public static String doGetLowerCase(String url, Map<String, Object> params) throws IOException {
        return doGetLowerCase1(url, params);
    }

    /**
     * 通过get请求 指定编码
     *
     * @param url     连接地址
     * @param params  参数
     * @param charset 编码
     * @return
     * @throws IOException
     */
    public static String doGet(String url, Map<String, Object> params, String charset) throws IOException {
        // 确定连接地址不能为空
        if (StringUtils.isEmpty(url) || params == null) {
            return null;
        }
        String response;
        String paramUrl = buildQuery(params, charset);
        if (StringUtils.isNotBlank(paramUrl)) {
            url += "?" + paramUrl;
        }
        String ctype = "text/html;charset=" + charset;
        HttpURLConnection conn;
        conn = getConnection(new URL(url), METHOD_GET, ctype);
        response = getResponseAsString(conn);
        return response;
    }

    public static String doGetLowerCase1(String url, Map<String, Object> params) throws IOException {
        // 确定连接地址不能为空
        if (StringUtils.isEmpty(url) || params == null) {
            return null;
        }
        String response;
        String paramUrl = buildQueryLowerCase(params);
        if (StringUtils.isNotBlank(paramUrl)) {
            url += "?" + paramUrl;
        }
        String ctype = "text/html;charset=" + GB2312;
        HttpURLConnection conn;
        conn = getConnection(new URL(url), METHOD_GET, ctype);
        response = getResponseAsString(conn);
        return response;
    }

    /**
     * 处理请求参数
     *
     * @param params 请求参数
     * @return 构建query
     */
    public static String buildQuery(Map<String, Object> params, String charset) {
        if (params == null || params.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Entry<String, Object> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                if (sb.length() > 0) {
                    sb.append("&");
                }
            }
            String key = entry.getKey();
            String value = String.valueOf(entry.getValue());
            if (aryNotEmpty(key, value)) {
                try {
                    sb.append(key).append("=").append(URLEncoder.encode(value, charset));
                } catch (UnsupportedEncodingException e) {
                }
            }
        }
        return sb.toString();

    }

    public static String buildQueryLowerCase(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Entry<String, Object> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                if (sb.length() > 0) {
                    sb.append("&");
                }
            }
            String key = entry.getKey().toLowerCase();
            String value = String.valueOf(entry.getValue()).toLowerCase();
            if (aryNotEmpty(key, value)) {
                sb.append(key).append("=").append(value);

            }
        }
        return sb.toString();
    }

    public static String buildQuery(Object obj) {
        StringBuffer sb = new StringBuffer();
        Class javaBean = obj.getClass();
        Field[] fields = javaBean.getDeclaredFields();
        Arrays.stream(fields).forEach(f -> {
            f.setAccessible(true);
            try {
                sb.append(f.getName() + "=" + f.get(obj) + "&");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return sb.toString();
    }

    /**
     * 判断字符数组，不为空
     *
     * @param values 字符数组
     * @return true or false
     */
    public static boolean aryNotEmpty(String... values) {
        boolean result = true;
        if (values == null || values.length == 0) {
            result = false;
        } else {
            for (String value : values) {
                result = StringUtils.isNotEmpty(value);
                if (!result) {
                    return result;
                }
            }
        }
        return result;
    }

    private static String getResponseAsString(HttpURLConnection conn) throws IOException {
        // 获取连接的编码格式
        String charset = getResponseCharset(conn.getContentType());
        InputStream es = conn.getErrorStream();
        // 判断连接是否失败
        if (es != null) {
            // 抛出错误异常
            String msg = getStreamAsString(es, charset);
            if (StringUtils.isEmpty(msg)) {
                throw new IOException(conn.getResponseCode() + " : " + conn.getResponseMessage());
            } else {
                throw new IOException(msg);
            }
        } else {
            // 返回连接成功的数据信息
            return getStreamAsString(conn.getInputStream(), charset);
        }
    }

    /**
     * 把流转换为字符串
     *
     * @param input
     * @param charset
     * @return
     * @throws IOException
     */
    private static String getStreamAsString(InputStream input, String charset) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader bf = null;
        try {
            bf = new BufferedReader(new InputStreamReader(input, charset));
            String str;
            while ((str = bf.readLine()) != null) {
                sb.append(str);
            }
            return sb.toString();
        } finally {
            if (bf != null) {
                bf.close();
                bf = null;
            }
        }
    }

    /**
     * 获取字符串的编码
     *
     * @param ctype
     * @return
     */
    private static String getResponseCharset(String ctype) {
        String charset = DEFAULT_CHARSET;
        if (!StringUtils.isEmpty(ctype)) {
            String[] params = ctype.split("\\;");
            for (String param : params) {
                param = param.trim();
                if (param.startsWith("charset")) {
                    String[] pair = param.split("\\=");
                    if (pair.length == 2) {
                        charset = pair[1].trim();
                    }
                }
            }
        }
        return charset;
    }


    private static String buildUrl(String host, String path, Map<String, String> querys) throws UnsupportedEncodingException {
        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append(host);
        if (!StringUtils.isBlank(path)) {
            sbUrl.append(path);
        }
        if (null != querys) {
            StringBuilder sbQuery = new StringBuilder();
            for (Entry<String, String> query : querys.entrySet()) {
                if (0 < sbQuery.length()) {
                    sbQuery.append("&");
                }
                if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
                    sbQuery.append(query.getValue());
                }
                if (!StringUtils.isBlank(query.getKey())) {
                    sbQuery.append(query.getKey());
                    if (!StringUtils.isBlank(query.getValue())) {
                        sbQuery.append("=");
                        sbQuery.append(URLEncoder.encode(query.getValue(), "utf-8"));
                    }
                }
            }
            if (0 < sbQuery.length()) {
                sbUrl.append("?").append(sbQuery);
            }
        }

        return sbUrl.toString();
    }

    public static String post(String path, Map<String, Object> data) {
        try {
            return cn.hutool.http.HttpUtil.post(path, data);
        } catch (Exception e) {
            log.error("异常:Exception", e);
        }
        return null;
    }

    public static String postJson(String path, String data) {
        try {
            return cn.hutool.http.HttpUtil.post(path, data);
        } catch (Exception e) {
            log.error("异常:Exception", e);
        }
        return null;
    }
}
