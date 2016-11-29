package com.example.zhiwei.readbook.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.example.zhiwei.readbook.tools.ActivityCollector;

public class BaseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
