package com.zw.wdplayer.utils.json.value;

import java.util.HashMap;

/**
 * Created by ZW on 2017/5/14.
 */
public class JsonObject implements JsonBase<HashMap<String,JsonBase>> {
    private HashMap<String,JsonBase> content;

    public JsonObject(){content = new HashMap<>();}

    public void put(String name,JsonBase pair){
        content.put(name,pair);
    }
    public void remove(String name){
        content.remove(name);
    }

    @Override
    public String getType() {
        return TYPE_OBJECT;
    }

    @Override
    public HashMap<String,JsonBase> getContent() {
        return content;
    }
}