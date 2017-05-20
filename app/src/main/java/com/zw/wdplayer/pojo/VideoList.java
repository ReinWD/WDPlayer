package com.zw.wdplayer.pojo;

import com.zw.wdplayer.utils.json.SerializedName;
import com.zw.wdplayer.utils.json.value.JsonBase;

import java.util.ArrayList;

/**
 * Created by reinwd on 5/20/17.
 */

public class VideoList {

    public VideoList(){}

    @SerializedName("showapi_res_error")
    private String showapiResError;
    @SerializedName("showapi_res_body")
    private ShowapiResBody showapiResBody;
    @SerializedName("showapi_res_code")
    private String showapiResCode;

    public static class ShowapiResBody {

        public ShowapiResBody(){}

        @SerializedName("ret_code")
        private String retCode;
        private Pagebean pagebean;

        public static class Pagebean {

            public Pagebean(){}

            private String allPages;
            private String maxResult;
            private String currentPage;
            private ArrayList<JsonBase> contentlist;
            private String allNum;

            public static class Contentlist {

                public Contentlist(){}

                @SerializedName("weixin_url")
                private String weixinUrl;
                private String love;
                @SerializedName("create_time")
                private String createTime;
                private String videotime;
                private String type;
                private String voicetime;
                @SerializedName("video_uri")
                private String videoUri;
                @SerializedName("profile_image")
                private String profileImage;
                private String voicelength;
                private String voiceuri;
                private String width;
                private String name;
                private String hate;
                private String text;
                private String id;
                private String height;

                public String getLove(){return this.love;}
                public String getVideotime(){return this.videotime;}
                public String getProfileImage(){return this.profileImage;}
                public String getType(){return this.type;}
                public String getVoicetime(){return this.voicetime;}
                public String getVideoUri(){return this.videoUri;}
                public String getVoicelength(){return this.voicelength;}
                public String getCreateTime(){return this.createTime;}
                public String getVoiceuri(){return this.voiceuri;}
                public String getWeixinUrl(){return this.weixinUrl;}
                public String getWidth(){return this.width;}
                public String getName(){return this.name;}
                public String getHate(){return this.hate;}
                public String getText(){return this.text;}
                public String getId(){return this.id;}
                public String getHeight(){return this.height;}

                public void setLove(String love){this.love=love;}
                public void setVideotime(String videotime){this.videotime=videotime;}
                public void setProfileImage(String profileImage){this.profileImage=profileImage;}
                public void setType(String type){this.type=type;}
                public void setVoicetime(String voicetime){this.voicetime=voicetime;}
                public void setVideoUri(String videoUri){this.videoUri=videoUri;}
                public void setVoicelength(String voicelength){this.voicelength=voicelength;}
                public void setCreateTime(String createTime){this.createTime=createTime;}
                public void setVoiceuri(String voiceuri){this.voiceuri=voiceuri;}
                public void setWeixinUrl(String weixinUrl){this.weixinUrl=weixinUrl;}
                public void setWidth(String width){this.width=width;}
                public void setName(String name){this.name=name;}
                public void setHate(String hate){this.hate=hate;}
                public void setText(String text){this.text=text;}
                public void setId(String id){this.id=id;}
                public void setHeight(String height){this.height=height;}

            }
            public String getAllPages(){return this.allPages;}
            public String getMaxResult(){return this.maxResult;}
            public String getCurrentPage(){return this.currentPage;}
            public ArrayList<JsonBase> getContentlist(){return this.contentlist;}
            public String getAllNum(){return this.allNum;}

            public void setAllPages(String allPages){this.allPages=allPages;}
            public void setMaxResult(String maxResult){this.maxResult=maxResult;}
            public void setCurrentPage(String currentPage){this.currentPage=currentPage;}
            public void setContentlist(ArrayList<JsonBase> contentlist){this.contentlist=contentlist;}
            public void setAllNum(String allNum){this.allNum=allNum;}

        }
        public String getRetCode(){return this.retCode;}
        public Pagebean getPagebean(){return this.pagebean;}

        public void setRetCode(String retCode){this.retCode=retCode;}
        public void setPagebean(Pagebean pagebean){this.pagebean=pagebean;}

    }
    public String getShowapiResError(){return this.showapiResError;}
    public ShowapiResBody getShowapiResBody(){return this.showapiResBody;}
    public String getShowapiResCode(){return this.showapiResCode;}

    public void setShowapiResError(String showapiResError){this.showapiResError=showapiResError;}
    public void setShowapiResBody(ShowapiResBody showapiResBody){this.showapiResBody=showapiResBody;}
    public void setShowapiResCode(String showapiResCode){this.showapiResCode=showapiResCode;}

}

