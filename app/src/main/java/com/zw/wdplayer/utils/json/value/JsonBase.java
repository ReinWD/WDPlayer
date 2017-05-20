package com.zw.wdplayer.utils.json.value;

/**
 * Created by ZW on 2017/5/14.
 */
public interface JsonBase<T>{

    String TYPE_VALUE = "Value" ;
    String TYPE_ARRAY = "Array";
    String TYPE_OBJECT = "Object";

    String getType();
    T getContent();
}