package com.example.zhiwei.readbook.Activity;

import android.os.Bundle;
import android.view.MenuItem;
import com.example.zhiwei.readbook.R;

public class AboutTeamActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_team);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
