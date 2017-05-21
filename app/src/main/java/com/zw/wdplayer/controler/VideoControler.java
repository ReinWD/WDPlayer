package com.zw.wdplayer.controler;

import android.util.Log;
import android.view.SurfaceHolder;

import com.zw.wdplayer.VideoPlayer;
import com.zw.wdplayer.pojo.VideoItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by reinwd on 5/21/17.
 */

public class VideoControler {
    private static final String TAG = "VideoControler";
    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(40,80,5, TimeUnit.MINUTES,
            new ArrayBlockingQueue<Runnable>(40));
    ArrayList<VideoPlayer>playerList;

    public VideoControler(){
        init();
    }

    public void addVideoPlayer(final VideoPlayer videoPlayer, final int index) {
        playerList.set(index,videoPlayer);
        Log.d(TAG, "addVideoPlayer: prepareing "+(index));
    }

    public void stop(int position){
        playerList.get(position).stop();
    }

    public void play(int position){playerList.get(position).play();}

    public void addPage(){
        for (int i = 0; i < 20; i++) {
            playerList.add(null);
        }
    }

    private void init() {
        playerList = new ArrayList<>();
    }

    public void prepareMediaPlayer(final int position) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                playerList.get(position).prepareMediaPlayer();
            }
        });
    }

    public void setDisplay(final SurfaceHolder holder, final int position) {
        playerList.get(position).setDisplay(holder);
    }

    public void pause(final int position) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                playerList.get(position).pause();
            }
        });
    }
}
