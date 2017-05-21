package com.zw.wdplayer;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.SurfaceHolder;

import com.zw.wdplayer.controler.PreparedCallback;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by reinwd on 5/20/17.
 */

public class VideoPlayer {
    private static final String TAG = "VideoPlayer";
    private MediaPlayer mediaPlayer;
    private double position = 0;
    private double totalLength;

    private boolean isPaused;
    private boolean isPrePared;

    private String url;

    public VideoPlayer(String url) throws IOException {
        this.url = url;
        init();
    }

    private void init() throws IOException {
        mediaPlayer = new MediaPlayer();
        isPaused = false;
        isPrePared = false;
    }

    public void play() {
        if (isPlaying()) {
            pause();
            return;
        }
        mediaPlayer.start();
        isPaused = false;
    }

    public void stop() {
    }

    public void prepareMediaPlayer(SurfaceHolder holder,PreparedCallback callback) {
        if (!isPrePared) {
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.seekTo(1);
                        Log.d(TAG, "run: prepare finish");
                        isPrePared = true;
                    }
                });
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.seekTo(1);
                    }
                });
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepare();
                totalLength = mediaPlayer.getDuration();
                callback.onPrepared();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalStateException e2) {
            }
        }
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void pause() {
        mediaPlayer.pause();
        this.isPaused = true;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isPrePared() {
        return isPrePared;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void reset() {
        mediaPlayer.reset();
    }

    public double getProgress(){
        position = mediaPlayer.getCurrentPosition();
        return position/totalLength;
    }

    public void setDisplay(SurfaceHolder holder) {
        mediaPlayer.setDisplay(holder);
    }

    private void calcSize(){}


    public void seekTo(int progress) {
        double target = (((double) progress)/100)*totalLength;
        mediaPlayer.seekTo(((int) target));
        mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                mp.start();
                mp.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                    @Override
                    public void onSeekComplete(MediaPlayer mp) {

                    }
                });
            }
        });
    }
}
