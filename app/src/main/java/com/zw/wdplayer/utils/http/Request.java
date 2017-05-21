package com.zw.wdplayer.utils.http;

import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by ZW on 2017/5/14.
 */

/**
 * information of a specified http request
 * Generate a request using {@link Builder}
 */
public class Request {
//    @IntDef({SharedValue.METHOD_POST, SharedValue.METHOD_GET})
//    @Retention(RetentionPolicy.SOURCE)
//    public @Interface Method{}

    private String url;
    private String method;
    private String[] paramKeys;
    private String[] paramValues;
    private int readTimeOut;
    private int connectionTimeOut;


    private Request(String url, String method, String[] paramKeys,
                    String[] paramValues, int readTimeOut, int connectionTimeOut) {
        this.url = url;
        this.method = method;
        this.paramKeys = paramKeys;
        this.paramValues = paramValues;
        this.readTimeOut = readTimeOut;
        this.connectionTimeOut = connectionTimeOut;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getReadTimeOut() {
        return readTimeOut;
    }

    public void setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    public int getConnectionTimeOut() {
        return connectionTimeOut;
    }

    public void setConnectionTimeOut(int connectionTimeOut) {
        this.connectionTimeOut = connectionTimeOut;
    }

    public String[] getParamKeys() {
        return paramKeys;
    }

    public String[] getParamValues() {
        return paramValues;
    }

    public String getProtocol() {
        if (this.url.toLowerCase().startsWith("http://")) return SharedValue.PROTOCAL_HTTP;
        else return SharedValue.PROTOCAL_HTTPS;
    }

    /**
     *
     */

    public static class Builder {
        private String url;
        private String method = SharedValue.METHOD_GET;
        private int connectionTimeOut = 5000;
        private int readTimeOut = 5000;
        private ArrayList<String> keys;
        private ArrayList<String> values;

        public Builder() {
            keys = new ArrayList<>();
            values = new ArrayList<>();
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder get() {
            method = SharedValue.METHOD_GET;
            return this;
        }

        public Builder post() {
            method = SharedValue.METHOD_POST;
            return this;
        }

        public Builder addParams(String key, String value) {
            this.keys.add(key);
            this.values.add(value);
            return this;
        }

        public Builder removeParam(String key) {
            int index;
            if ((index = this.keys.indexOf(key)) != -1) {
                keys.remove(index);
                values.remove(index);
            }
            return this;
        }

        public Builder connectionTimeOut(int mills) {
            this.connectionTimeOut = mills;
            return this;
        }

        public Builder readTimeout(int mills) {
            this.readTimeOut = mills;
            return this;
        }

        public Request build() {
            String[] keys = null;
            String[] values = null;
            Iterator<String> iterator;
            if (this.keys != null) {
                iterator = this.keys.iterator();
                keys = new String[this.keys.size()];
                for (int i = 0; iterator.hasNext() ; i++) {
                    keys[i] = iterator.next();
                }
            }
            if (this.values != null) {
                iterator = this.values.iterator();
                values = new String[this.values.size()];
                for (int i = 0; iterator.hasNext(); i++) {
                    values[i]=iterator.next();
                }
            }
            return new Request(url, method, keys, values, this.readTimeOut, this.connectionTimeOut);
        }
    }
}