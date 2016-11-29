package com.example.zhiwei.readbook.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.zhiwei.readbook.R;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvLoadCoverMode;
    private Switch swLoadCoverMode;
    private TextView tvLoadCoverQuality;
    private TextView tvrLoadCoverQuality;
    private TextView tvLoadNum;
    private TextView tvrLoadNum;
    private TextView tvReceiveNoticeMode;
    private Switch swReceiveNoticeMode;
    private TextView tvReceiveNoticeTime;
    private TextView tvrReceiveNoticeTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initView();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        GetSettingsConfig();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveSettingsConfig();
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


    private void saveSettingsConfig() {
        SharedPreferences.Editor editor = getSharedPreferences("settings",MODE_PRIVATE).edit();
        editor.putBoolean("receiveNoticeMode",swReceiveNoticeMode.isChecked());
        editor.putString("receiveNoticeTime",tvrReceiveNoticeTime.getText().toString());
        editor.putInt("loadNum",Integer.parseInt(tvrLoadNum.getText().toString()));
        editor.putBoolean("loadCoverMode",swLoadCoverMode.isChecked());
        editor.putString("loadCoverQuality",tvrLoadCoverQuality.getText().toString());
        editor.apply();
    }

    private void GetSettingsConfig(){
        SharedPreferences preferences = getSharedPreferences("settings",MODE_PRIVATE);
        swReceiveNoticeMode.setChecked(preferences.getBoolean("receiveNoticeMode",false));
        tvrReceiveNoticeTime.setText(preferences.getString("receiveNoticeTime","20:00"));
        tvrLoadNum.setText(String.valueOf(preferences.getInt("loadNum",3)));
        swLoadCoverMode.setChecked(preferences.getBoolean("loadCoverMode",false));
        tvrLoadCoverQuality.setText(preferences.getString("loadCoverQuality","一般"));

        setLoadCover(swLoadCoverMode.isChecked());
        setReceiveNoticeMode(swReceiveNoticeMode.isChecked());
    }

    private void initView() {
        //load cover mode
        tvLoadCoverMode = (TextView) findViewById(R.id.tv_load_cover_mode);
        swLoadCoverMode = (Switch) findViewById(R.id.sw_load_cover_mode);
        //load cover quality
        tvLoadCoverQuality = (TextView) findViewById(R.id.tv_load_cover_quality);
        tvrLoadCoverQuality = (TextView) findViewById(R.id.tvr_load_cover_quality);
        //load number
        tvLoadNum = (TextView) findViewById(R.id.tv_load_num);
        tvrLoadNum = (TextView) findViewById(R.id.tvr_load_num);
        //receive notice mode
        tvReceiveNoticeMode = (TextView) findViewById(R.id.tv_receive_notice_mode);
        swReceiveNoticeMode = (Switch) findViewById(R.id.sw_receive_notice_mode);
        //receive notice time
        tvReceiveNoticeTime = (TextView) findViewById(R.id.tv_receive_notice_time);
        tvrReceiveNoticeTime = (TextView) findViewById(R.id.tvr_receive_notice_time);

        tvLoadCoverMode.setOnClickListener(this);
        tvLoadCoverQuality.setOnClickListener(this);
        tvLoadNum.setOnClickListener(this);
        tvReceiveNoticeMode.setOnClickListener(this);
        tvReceiveNoticeTime.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_load_cover_mode:
                swLoadCoverMode.setChecked(!swLoadCoverMode.isChecked());
                setLoadCover(swLoadCoverMode.isChecked());
                break;
            case R.id.tv_load_cover_quality:
                String[] coverQualityItems = getResources().getStringArray(R.array.array_load_cover_quality);
                showItemsDialog(coverQualityItems, tvrLoadCoverQuality);
                break;
            case R.id.tv_load_num:
                showItemsDialog(new String[]{"3", "5", "10"}, tvrLoadNum);
                break;
            case R.id.tv_receive_notice_mode:
                swReceiveNoticeMode.setChecked(!swReceiveNoticeMode.isChecked());
                setReceiveNoticeMode(swReceiveNoticeMode.isChecked());
                break;
            case R.id.tv_receive_notice_time:
                ShowTimePickerDialog(tvrReceiveNoticeTime);
                break;
            default:
                break;
        }
    }

    private void ShowTimePickerDialog(final TextView changeTextView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this, R.style.dialogTheme);
        View view = getLayoutInflater().inflate(R.layout.dialog_timepicker, null);
        final TimePicker picker = (TimePicker) view.findViewById(R.id.tp_time);
        builder.setView(view)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        android.text.format.Time time = new android.text.format.Time();
                        time.hour = picker.getCurrentHour();
                        time.minute = picker.getCurrentMinute();
                        String timeStr = (time.hour < 10 ? "0" + time.hour : time.hour) +":"
                                + (time.minute < 10 ? "0" + time.minute : time.minute);
                        changeTextView.setText(timeStr);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showItemsDialog(final String[] items, final TextView changeTextView) {
        final AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.listview_of_dialog_items, null);
        ListView dialogList = (ListView) view.findViewById(R.id.listView_dialog);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(SettingsActivity.this, R.layout.textview_of_dialog_items, items);
        dialogList.setAdapter(adapter);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
        dialogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                changeTextView.setText(items[position]);
                dialog.dismiss();
            }
        });
    }

    private void setLoadCover(boolean flag) {
        SetViewAble(tvLoadCoverQuality, flag, true);
        SetViewAble(tvrLoadCoverQuality, flag, false);
    }

    private void setReceiveNoticeMode(boolean flag) {
        SetViewAble(tvReceiveNoticeTime, flag, true);
        SetViewAble(tvrReceiveNoticeTime, flag, false);
    }

    private void SetViewAble(View view, boolean able, boolean leftTextView) {
        if (able) {
            view.setClickable(true);
            if (view.getClass() == TextView.class) {
                TextView textView = (TextView) view;
                if (leftTextView) {
                    textView.setTextColor(this.getResources().getColor(R.color.black));
                } else {
                    textView.setTextColor(this.getResources().getColor(R.color.deep_green));
                }
            }
        } else {
            view.setClickable(false);
            if (view.getClass() == TextView.class) {
                TextView textView = (TextView) view;
                textView.setTextColor(this.getResources().getColor(R.color.grey_title));
            }
        }
    }


}
