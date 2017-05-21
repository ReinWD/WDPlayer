package com.zw.wdplayer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.zw.wdplayer.adapter.MainRecyclerAdapter;
import com.zw.wdplayer.adapter.MainRecyclerAdapter.UiInterface;
import com.zw.wdplayer.controler.MainConnector;
import com.zw.wdplayer.controler.MainControler;
import com.zw.wdplayer.pojo.VideoItem;

public class MainActivity extends Activity implements UiInterface {

    private static final String TAG = "MainActivity";

    private MainControler mainControler;
    private RecyclerView mMainRecyclerView;
    private MainRecyclerAdapter mRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        mainControler.switchPage(1);
    }

    @Override
    public void requestNextPage(int targetPage) {
        mainControler.switchPage(targetPage);

    }

    private class MainListener implements MainConnector{

        @Override
        public void onItemReady(final VideoItem image, final int index, final int pages) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRecyclerAdapter.setData(image,index,pages);
                }
            });
        }
    }

    void init() {
        mainControler = new MainControler(this,new MainListener());
        mMainRecyclerView = (RecyclerView) findViewById(R.id.recycler_main);
        mRecyclerAdapter = new MainRecyclerAdapter(this,this);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        mMainRecyclerView.setLayoutManager(manager);
        mMainRecyclerView.setAdapter(mRecyclerAdapter);
        Log.i(TAG, "init: finished");
    }


}
