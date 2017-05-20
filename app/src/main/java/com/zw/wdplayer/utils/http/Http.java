package com.zw.wdplayer.utils.http;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownServiceException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by ZW on 2017/5/7.
 */

public class Http {
    /**
     * Main Method In this util.
     * Send http/https Request to a target.
     * The request will enqueue a Thread Pool, wait for executing.
     * when link finished, a callback will be sent to {@param callback}.
     * @param request see {@link Request}
     * @param callback see {@link Callback}
     */
    public static void sendRequest(Request request, Callback callback) {
        switch (request.getProtocol()) {
            case SharedValue.PROTOCAL_HTTP:
                new Thread(new HttpRequest(request, callback)).start();
                break;
            case SharedValue.PROTOCAL_HTTPS:
                new Thread(new HttpSRequest(request, callback)).start();
                break;
        }
    }

    private static class HttpRequest implements Runnable {
        private HttpURLConnection connection;
        private Request request;
        private Callback callback;

        HttpRequest(Request request, Callback callback){
            this.request = request;
            this.callback = callback;
        }

        @Override
        public void run() {
            String url;
            connection = null;

            //创建connection
            try {
                switch (request.getMethod()) {
                    case SharedValue.METHOD_GET:
                        url = addParams(request.getUrl(), request.getParamKeys(), request.getParamValues());
                        connection = (HttpURLConnection) new URL(url).openConnection();
                        connection.setRequestMethod(SharedValue.METHOD_GET);
                        break;
                    case SharedValue.METHOD_POST:
                        url = request.getUrl();
                        String[] keys = request.getParamKeys();
                        String[] values = request.getParamValues();
                        connection = (HttpURLConnection) new URL(url).openConnection();
                        connection.setRequestMethod(SharedValue.METHOD_POST);
                        for (int i = 0; i < keys.length; i++) {
                            connection.setRequestProperty(keys[i],values[i]);
                        }
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                callback.onFailure(e);
            }
            connection.setConnectTimeout(request.getConnectionTimeOut());
            connection.setReadTimeout(request.getReadTimeOut());

            //处理inputStream
            InputStream inputStream;
            try {
                inputStream = connection.getInputStream();
            } catch (UnknownServiceException e1) {
                e1.printStackTrace();
                System.out.println("Protocal don't support output.");
                callback.onFailure(e1);
                return;
            } catch (IOException e2) {
                e2.printStackTrace();
                System.out.println("Connection error");
                callback.onFailure(e2);
                return;
            }
            callback.onSuccess(inputStream);

        }
    }
    private static class HttpSRequest implements Runnable {
        private HttpsURLConnection connection;
        private Request request;
        private Callback callback;

        HttpSRequest(Request request, Callback callback) {
            this.request = request;
            this.callback = callback;
        }

        @Override
        public void run() {
            String url;
            connection = null;

            //创建connection
            try {
                switch (request.getMethod()) {
                    case SharedValue.METHOD_GET:
                        url = addParams(request.getUrl(), request.getParamKeys(), request.getParamValues());//加参数
                        connection = (HttpsURLConnection) new URL(url).openConnection();
                        connection.setRequestMethod(SharedValue.METHOD_GET);
                        break;
                    case SharedValue.METHOD_POST:
                        url = request.getUrl();
                        String[] keys = request.getParamKeys();
                        String[] values = request.getParamValues();
                        connection = (HttpsURLConnection) new URL(url).openConnection();
                        connection.setRequestMethod(SharedValue.METHOD_POST);
                        for (int i = 0; i < keys.length; i++) {
                            connection.setRequestProperty(keys[i],values[i]);
                        }
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                callback.onFailure(e);
            }
            connection.setConnectTimeout(request.getConnectionTimeOut());
            connection.setReadTimeout(request.getReadTimeOut());

            //处理inputStream
            InputStream inputStream;
            try {
                inputStream = connection.getInputStream();
            } catch (UnknownServiceException e1) {
                e1.printStackTrace();
                System.out.println("Protocal don't support output.");
                callback.onFailure(e1);
                return;
            } catch (IOException e2) {
                e2.printStackTrace();
                System.out.println("Connection error");
                callback.onFailure(e2);
                return;
            }
            callback.onSuccess(inputStream);

        }
    }

    private static String addParams(String url, String[] keys, String[] values) {
        if (keys.length==0) {
            return url;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(url).append("?").append(keys[0]).append("=").append(values[0]);
        for (int i = 1; i < keys.length; i++) {
            sb.append("&").append(keys[i]).append("=").append(values[i]);
        }
        return sb.toString();
    }


}