package com.zw.wdplayer.controler;

import com.zw.wdplayer.pojo.VideoItem;

import java.io.Serializable;

/**
 * Created by reinwd on 5/19/17.
 */

public interface MainConnector extends Serializable{
    void onItemReady(VideoItem image, int index, int pages);
}
