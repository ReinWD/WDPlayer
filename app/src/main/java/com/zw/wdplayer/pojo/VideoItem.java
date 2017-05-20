package com.zw.wdplayer.pojo;

import android.graphics.Bitmap;

import java.io.InputStream;

/**
 * Created by reinwd on 5/20/17.
 */

public class VideoItem {

    public VideoItem(){}

        private String love;
        private String createTime;
        private InputStream videoUri;
        private Bitmap profileImage;
        private String name;
        private String hate;
        private String text;
        private String id;

        public String getLove(){return this.love;}
        public Bitmap getProfileImage(){return this.profileImage;}
        public InputStream getVideoUri(){return this.videoUri;}
        public String getCreateTime(){return this.createTime;}
        public String getName(){return this.name;}
        public String getHate(){return this.hate;}
        public String getText(){return this.text;}
        public String getId(){return this.id;}

        public void setLove(String love){this.love=love;}
        public void setProfileImage(Bitmap profileImage){this.profileImage=profileImage;}
        public void setVideoUri(InputStream videoUri){this.videoUri=videoUri;}
        public void setCreateTime(String createTime){this.createTime=createTime;}
        public void setName(String name){this.name=name;}
        public void setHate(String hate){this.hate=hate;}
        public void setText(String text){this.text=text;}
        public void setId(String id){this.id=id;}
    }

