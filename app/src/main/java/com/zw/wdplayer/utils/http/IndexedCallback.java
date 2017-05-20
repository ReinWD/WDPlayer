package com.zw.wdplayer.utils.http;

import java.io.InputStream;

/**
 * Created by reinwd on 5/20/17.
 */

public abstract class IndexedCallback implements Callback {
    public int index;
    public int pages;
    public IndexedCallback(int index,int pages){
        this.index = index;
        this.pages = pages;
    }
}
