package com.example.zhiwei.readbook.RxJava_Retrofit.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

/**
 * Created by zhiwei on 2016/7/24.
 */
public class GankContentResult {
    public boolean error;
    public @SerializedName("results") ArrayList<GankContent> contents;
}
