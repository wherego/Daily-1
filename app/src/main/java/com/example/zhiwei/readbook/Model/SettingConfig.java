package com.example.zhiwei.readbook.Model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zhiwei on 2016/5/23.
 */
public class SettingConfig {
    private boolean receiveNoticeMode;
    private String receiveNoticeTime;
    private int loadNum;
    private boolean loadCoverMode;
    private String loadCoverQuality;
    public SettingConfig(Context context){
        SharedPreferences preferences = context.getSharedPreferences("settings",Context.MODE_PRIVATE);
        receiveNoticeMode = preferences.getBoolean("receiveNoticeMode",false);
        receiveNoticeTime = preferences.getString("receiveNoticeTime","20:00");
        loadNum = preferences.getInt("loadNum",3);
        loadCoverMode = preferences.getBoolean("loadCoverMode",false);
        loadCoverQuality = preferences.getString("loadCoverQuality","一般");
    }

    public boolean isReceiveNoticeMode() {
        return receiveNoticeMode;
    }

    public void setReceiveNoticeMode(boolean receiveNoticeMode) {
        this.receiveNoticeMode = receiveNoticeMode;
    }

    public String getReceiveNoticeTime() {
        return receiveNoticeTime;
    }

    public void setReceiveNoticeTime(String receiveNoticeTime) {
        this.receiveNoticeTime = receiveNoticeTime;
    }

    public int getLoadNum() {
        return loadNum;
    }

    public void setLoadNum(int loadNum) {
        this.loadNum = loadNum;
    }

    public boolean isLoadCoverMode() {
        return loadCoverMode;
    }

    public void setLoadCoverMode(boolean loadCoverMode) {
        this.loadCoverMode = loadCoverMode;
    }

    public String getLoadCoverQuality() {
        return loadCoverQuality;
    }

    public void setLoadCoverQuality(String loadCoverQuality) {
        this.loadCoverQuality = loadCoverQuality;
    }
}
