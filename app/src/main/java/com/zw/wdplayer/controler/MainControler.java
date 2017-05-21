package com.zw.wdplayer.controler;

import android.graphics.BitmapFactory;
import android.util.Log;

import com.zw.wdplayer.MainActivity;
import com.zw.wdplayer.pojo.VideoItem;
import com.zw.wdplayer.pojo.VideoList;
import com.zw.wdplayer.utils.http.Callback;
import com.zw.wdplayer.utils.http.Http;
import com.zw.wdplayer.utils.http.IndexedCallback;
import com.zw.wdplayer.utils.http.Request;
import com.zw.wdplayer.utils.json.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Created by reinwd on 5/19/17.
 */

public class MainControler {

    private static final String TAG = "MainControler";

    private MainConnector mMainConnector;
    private int mPage;
    private ArrayList<VideoItem> mVideoItems;

    public MainControler(MainActivity mainActivity, MainConnector connector) {
        this.mMainConnector = connector;
        init();
    }

    private void init() {

    }

    private void requestPage(int page) {
        this.mPage = page;
        Request request = new Request.Builder().get()
                .url("http://route.showapi.com/255-1")
                .addParams("showapi_sign", "45f60f0c98ee4622a2618830af25ba5e")
                .addParams("showapi_appid", "38537")
                .addParams("type", "41")
                .addParams("page", String.valueOf(page))
                .build();
        Http.sendRequest(request, new OnPageRequestFinishListenner(page));
    }

    private void handleItem(ArrayList pagebeanList) {
        int i = 0;
        mVideoItems = new ArrayList<>();
        VideoList.ShowapiResBody.Pagebean.Contentlist contentList;
        for (Object content :
                pagebeanList) {
            VideoItem videoItem = new VideoItem();
            contentList = ((VideoList.ShowapiResBody.Pagebean.Contentlist) content);
            videoItem.setCreateTime(contentList.getCreateTime());
            videoItem.setHate(contentList.getHate());
            videoItem.setLove(contentList.getLove());
            videoItem.setText(contentList.getText().replace("\\n", ""));
            videoItem.setName(contentList.getName());
            videoItem.setVideoUri(contentList.getVideoUri());
            mVideoItems.add(videoItem);
            requestData(contentList.getProfileImage(), new OnProfileImageFinishListener(i));
            i++;
        }
    }

    private void requestData(String url, Callback callback) {
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        Http.sendRequest(request, callback);
    }

    private class OnPageRequestFinishListenner extends IndexedCallback {
        public OnPageRequestFinishListenner(int index) {
            super(index, mPage);
        }

        @Override
        public void onSuccess(InputStream inputStream) {
            VideoList mVideoList;
            JsonReader jsonReader = new JsonReader();
            ArrayList pagebeanList;
            try {
                String json = decodeStream(inputStream);
                mVideoList = jsonReader.readJson(json, VideoList.class);
                Log.d(TAG, "onSuccess: read json finished");
                pagebeanList = ((ArrayList) mVideoList.getShowapiResBody().getPagebean().getContentlist());
                handleItem(pagebeanList);
            } catch (IOException e) {
                Log.e(TAG, "onSuccess: ", e);
            } catch (InvocationTargetException e) {
                Log.e(TAG, "onSuccess: ", e);
            } catch (NoSuchMethodException e) {
                Log.e(TAG, "onSuccess: ", e);
            } catch (InstantiationException e) {
                Log.e(TAG, "onSuccess: ", e);
            } catch (IllegalAccessException e) {
                Log.e(TAG, "onSuccess: ", e);
            }
        }

        @Override
        public void onFailure(Exception e) {
            Log.e(TAG, "onFailure: ", e);
        }
    }

    private class OnProfileImageFinishListener extends IndexedCallback {
        OnProfileImageFinishListener(int index) {
            super(index, mPage);
        }

        @Override
        public void onSuccess(InputStream inputStream) {
            mVideoItems.get(index).setProfileImage(BitmapFactory.decodeStream(inputStream));
            onDataReady(mVideoItems.get(index),index, mPage);
        }

        @Override
        public void onFailure(Exception e) {
            Log.e(TAG, "onFailure: ", e);
        }
    }

    private void onDataReady(VideoItem data,int index,int page){
        mMainConnector.onItemReady(mVideoItems.get(index), index, page);
    }

    private String decodeStream(InputStream is) throws IOException {
        InputStreamReader reader = new InputStreamReader(is);
        StringBuilder sb = new StringBuilder();
        int cache;
        while ((cache = reader.read()) != -1) {
            sb.appendCodePoint(cache);
        }
        return sb.toString();
    }

    public void switchPage(int page) {
        requestPage(page);
    }
}
