package com.zw.wdplayer.utils.http;

import java.io.InputStream;

/**
 * Created by ZW on 2017/5/7.
 */


public interface Callback {
    /**
     * onSuccess: if a request was successfully responsed by target server, this method
     *  will pass an InputStream to this method.
     * @param inputStream InputStream of this connect
     */
    void onSuccess(InputStream inputStream);

    /**
     * if an error occurred, this method will be called.
     * @param e error
     */
    void onFailure(Exception e);

}
