package com.zw.wdplayer.utils.json.value;

/**
 * Created by ZW on 2017/5/14.
 */
public class JsonValue<T> implements JsonBase<T> {
    private T value;

    public JsonValue(T value){
        this.value = value;
    }

    @Override
    public String getType() {
        return TYPE_VALUE;
    }

    @Override
    public T getContent() {
        return value;
    }

}
