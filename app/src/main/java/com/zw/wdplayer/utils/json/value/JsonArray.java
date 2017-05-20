package com.zw.wdplayer.utils.json.value;

import java.util.ArrayList;

/**
 * Created by ZW on 2017/5/14.
 */
public class JsonArray implements JsonBase<ArrayList<JsonBase>> {
    private ArrayList<JsonBase> content;

    public JsonArray(){
        content = new ArrayList<>();
    }

    public void add(JsonBase jsonBase){content.add(jsonBase);}
    public void remove(JsonBase jsonPair){content.remove(jsonPair);}
    public void remove(int index){content.remove(index);}
    public int getContentSize(){return content.size();}

    @Override
    public String getType() {
        return TYPE_ARRAY;
    }

    @Override
    public ArrayList<JsonBase> getContent() {
        return content;
    }
}