package com.example.zhiwei.readbook.tools;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by zhiwei on 2016/5/20.
 */
public class ActivityCollector {
    public static ArrayList<Activity> activities = new ArrayList<>();
    public static void addActivity(Activity activity){
        activities.add(activity);
    }
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }
    public static void finishAll(){
        for (Activity activity : activities){
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
