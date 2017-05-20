package com.zw.wdplayer;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;

import com.zw.wdplayer.utils.http.Http;
import com.zw.wdplayer.utils.http.Request;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class IntroActivity extends AppCompatActivity implements Serializable{
    private static final int DURITION_MIN = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        LoadTask loadTask = new LoadTask();
        loadTask.execute();
    }

    private class LoadTask extends AsyncTask<String, Integer, Boolean> implements Serializable{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            long startTime = System.currentTimeMillis();
            long loadTime = System.currentTimeMillis() - startTime;

            if (loadTime < DURITION_MIN) {
                try {
                    Thread.sleep(DURITION_MIN - loadTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return true;
        }
    }
}
