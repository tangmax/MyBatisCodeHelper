package com.ccnode.codegenerator.log.handler;

import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Author bruce.ge
 * @Date 2017/2/27
 * @Description
 */
public class HttpClient {
    private static OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS).build();

    private static ExecutorService executorService = Executors.newFixedThreadPool(1);
    private static String url = "http://brucege.com/mybatislog/add";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private static Gson gson = new Gson();

    public static void sendDataToLog(MybatisLogRequest logRequest) {
        executorService.submit(() -> {
            String json = gson.toJson(logRequest);
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                //ignore
            } finally {
                if (response != null) {
                    response.close();
                }
            }
        });
    }
}
