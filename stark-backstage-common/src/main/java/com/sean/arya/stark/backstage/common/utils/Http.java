package com.sean.arya.stark.backstage.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sean
 * @date 2018/5/17 19:12
 * @description http工具类
 * @vesion 1.0.0
 */
@Slf4j
public class Http {
    /**
     * socket 获取数据超时时间
     */
    private static int socketTimeout = 8000;
    /**
     * http连接超时时间
     */
    private static int connectTimeout = 3000;//3秒
    /**
     *设置从connectManager 获取Connection 超时时间
     */
    private static int connectionRequestTimeout = 1000;
    private static int maxTotal=100;
    private static int maxPerRoute=100;

    private static final String CHARSET="utf-8";

    /**
     * json上下文类型
     */
    public static final String JSON_CONTENT_TYPE = "application/json; charset="+CHARSET;
    /**
     * form上下文类型
     */
    public static final String FORM_CONTENT_TYPE = "application/x-www-form-urlencoded; charset="+CHARSET;
    /**
     * formData上下文类型
     */
    public static final String FORM_DATA_CONTENT_TYPE = "multipart/form-data;charset="+CHARSET;
    public static final String KEY_COOKIE="COOKIE";
    public static final String KEY_JSON="JSON";

    private static CloseableHttpClient http;
    static{
        /**
         * 线程池管理器
         */
        PoolingHttpClientConnectionManager phcm = new PoolingHttpClientConnectionManager();
        phcm.setMaxTotal(maxTotal);
        phcm.setDefaultMaxPerRoute(maxPerRoute);
        http = HttpClients.createDefault();
        HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception,int executionCount, HttpContext context) {
                if (executionCount >= 5) {// 假设已经重试了5次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 假设server丢掉了连接。那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    return false;
                }
                if (exception instanceof UnknownHostException) {// 目标server不可达
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {// SSL握手异常
                    return false;
                }
                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).setConnectionRequestTimeout(connectionRequestTimeout).build();
        http=HttpClients.custom().setConnectionManager(phcm).setDefaultRequestConfig(requestConfig).setRetryHandler(retryHandler).build();
    }

    /**
     * @param url
     * @param params
     * @param headers
     * @return
     * @throws IOException
     */
    public static String get(String url,Map<String,String> params, Map<String,String> headers) throws IOException {
        String uri=initUrl(url,params);
        HttpGet get=new HttpGet(uri);
        headers.forEach((key,value)->get.setHeader(key,value));
        CloseableHttpResponse resp = http.execute(get);
        String result=parse(resp);
        log.debug("get,result:{}",result);
        return result;
    }

    /**
     * FORM: Map<String,String> application/x-www-form-urlencoded
     * @param url 路径
     * @param params url 参数
     * @param headers 消息头  不能为null
     * @param data 数据  不能为null
     * @return
     * @throws IOException
     */
    public static String post(String url,Map<String,String> params, Map<String,String> headers,Map<String,String> data)throws IOException{
        String uri=initUrl(url,params);
        HttpPost post=new HttpPost(uri);
        headers.forEach((key,value)->post.addHeader(key,value));
        post.setHeader("Content-Type",FORM_CONTENT_TYPE);
        List<NameValuePair> pairs = initPair(data);
        post.setEntity(new UrlEncodedFormEntity(pairs,CHARSET));
        CloseableHttpResponse resp = http.execute(post);
        String result=parse(resp);
        return result;
    }
    /**
     * JSON String application/json
     * @param url 路径
     * @param params url 参数
     * @param headers 消息头  不能为null
     * @param json 数据 不能为null
     * @return
     * @throws IOException
     */
    public static String post(String url,Map<String,String> params, Map<String,String> headers,String json)throws IOException{
        String uri=initUrl(url,params);
        HttpPost post=new HttpPost(uri);
        headers.forEach((key,value)->post.addHeader(key,value));
        post.setHeader("Content-Type",JSON_CONTENT_TYPE);
        post.setEntity(new ByteArrayEntity(json.getBytes(CHARSET)));
        CloseableHttpResponse resp = http.execute(post);
        String result=parse(resp);
        return result;
    }
    /**
     * multipart/form-data
     * @param url 路径
     * @param headers 消息头  不能为null
     * @param data 数据  不能为null
     * @return
     * @throws IOException
     */
    public static String post(String url, Map<String,String> headers,Map<String,MultipartFile> data)throws IOException{
        HttpPost post=new HttpPost(url);
        headers.forEach((key,value)->post.addHeader(key,value));
        post.setHeader("Content-Type",FORM_DATA_CONTENT_TYPE);
        final MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        for (Map.Entry<String, MultipartFile> entry : data.entrySet()) {
            final InputStreamBody body = new InputStreamBody(entry.getValue().getInputStream(),entry.getKey());
            entityBuilder.addPart(entry.getKey(), body);
        }
        post.setEntity(entityBuilder.build());
        CloseableHttpResponse resp = http.execute(post);
        String result=parse(resp);
        return result;
    }

    /**
     * FORM: Map<String,String> application/x-www-form-urlencoded
     * @param url 路径
     * @param params url 参数
     * @param headers 消息头  不能为null
     * @param data 数据  不能为null
     * @return Map
     * COOKIE List<Cookie>
     * JSON   String
     * @throws IOException
     */
    public static Map<String,Object> login(String url, Map<String,String> params, Map<String,String> headers,Map<String,String> data)throws IOException{
        String uri=initUrl(url,params);
        HttpPost post=new HttpPost(uri);
        headers.forEach((key,value)->post.addHeader(key,value));
        post.setHeader("Content-Type",FORM_CONTENT_TYPE);
        List<NameValuePair> pairs = initPair(data);
        post.setEntity(new UrlEncodedFormEntity(pairs,CHARSET));
        HttpClientContext context = HttpClientContext.create();
        CloseableHttpResponse resp = http.execute(post,context);
        Map<String,Object> result=parse(resp,context);
        return result;
    }
    private static String initUrl(String url,Map<String,String> params) throws IOException {
        List<NameValuePair> pair=initPair(params);
        return pair==null?url:url+"?"+EntityUtils.toString(new UrlEncodedFormEntity(pair,CHARSET));
    }
    private static List<NameValuePair> initPair(Map<String,String> params){
        if (params != null && !params.isEmpty()) {
            List<NameValuePair> pairs = new ArrayList<>(params.size());
            for (String key : params.keySet()) {
                pairs.add(new BasicNameValuePair(key, params.get(key)));
            }
            return pairs;
        }
        return null;
    }
    private static String parse(CloseableHttpResponse response) throws IOException {
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            HttpEntity entity = response.getEntity();
            String respJson = EntityUtils.toString(entity, CHARSET);
            EntityUtils.consume(entity);
            return respJson;
        }else {
            log.error("parse fail, fail code:" + response.getStatusLine().getStatusCode());
            throw new RuntimeException("parse,get request failed code:" + response.getStatusLine().getStatusCode());
        }
    }

    /**
     * 将返回结果封装成Map
     * @param response
     * @param context
     * @return Map
     * COOKIE List<Cookie>
     * JSON   String
     * @throws IOException
     */
    private static Map<String,Object> parse(CloseableHttpResponse response,HttpClientContext context) throws IOException {
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            Map<String,Object> map = new HashMap<>();
            List<Cookie> list = context.getCookieStore().getCookies();
            map.put(KEY_COOKIE,list);
            HttpEntity entity = response.getEntity();
            String respJson = EntityUtils.toString(entity, CHARSET);
            EntityUtils.consume(entity);
            map.put(KEY_JSON,respJson);
            return map;
        }else {
            log.error("parse fail, fail code:" + response.getStatusLine().getStatusCode());
            throw new RuntimeException("parse,get request failed code:" + response.getStatusLine().getStatusCode());
        }
    }
}
