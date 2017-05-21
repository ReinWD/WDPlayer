package com.zw.wdplayer.controler;

import android.util.Log;
import android.view.SurfaceHolder;

import com.zw.wdplayer.VideoPlayer;

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

    public void setProgressUpdater(int position, final ProgressUpdater updater){
        final VideoPlayer target = playerList.get(position);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                while (true){
                    while (target.isPlaying()){
                        updater.updateProgress(target.getProgress());
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void init() {
        playerList = new ArrayList<>();
    }

    public void prepareMediaPlayer(final SurfaceHolder holder, final int position, final PreparedCallback callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "addVideoPlayer: prepareing "+(position));
                playerList.get(position).prepareMediaPlayer(holder,callback);
            }
        });
    }

    public void setDisplay(final SurfaceHolder holder, final int position) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                playerList.get(position).setDisplay(holder);
            }
        });
    }

    public void pause(final int position) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                playerList.get(position).pause();
            }
        });
    }

    public void seekTo(int position, int progress) {
        playerList.get(position).seekTo(progress);
    }
}
