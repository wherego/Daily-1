package com.example.zhiwei.readbook.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.zhiwei.readbook.R;
import com.example.zhiwei.readbook.RxJava_Retrofit.NetWork;
import com.example.zhiwei.readbook.RxJava_Retrofit.model.GankContent;
import com.example.zhiwei.readbook.RxJava_Retrofit.model.GankContentResult;
import com.example.zhiwei.readbook.adapter.ContentInfoAdapter;
import java.util.ArrayList;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ContentInfoActivity extends BaseActivity {

    private ListView contentListView;
    private ProgressBar progressBar;
    private String mType;
    private ContentInfoAdapter adapter;
    //private Handler handler = new Handler();
    private ArrayList<GankContent> contentInfos = new ArrayList<>();

    private Subscription subscription;

    public static final String TYPE_KEY = "type";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_info);
        contentListView = (ListView) findViewById(R.id.content_listView);
        contentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri uri = Uri.parse(contentInfos.get(position).url);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.content_progressBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        mType = intent.getStringExtra(TYPE_KEY);
        getSupportActionBar().setTitle(mType);
;
        progressBar.setVisibility(View.VISIBLE);

        subscription = NetWork.getGankApi().getGankContent(mType, 50, 1)
            .map(new Func1<GankContentResult, ArrayList<GankContent>>() {
                @Override public ArrayList<GankContent> call(GankContentResult gankContentResult) {
                    contentInfos = gankContentResult.contents;
                    return contentInfos;
                }
            }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<ArrayList<GankContent>>() {
                @Override public void onCompleted() {
                }


                @Override public void onError(Throwable e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ContentInfoActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                }


                @Override public void onNext(ArrayList<GankContent> gankContents) {
                    progressBar.setVisibility(View.GONE);
                    adapter = new ContentInfoAdapter(ContentInfoActivity.this, R.layout.content_item,gankContents);
                    contentListView.setAdapter(adapter);
                }
            });

        //new Thread(runnable).start();
    }


    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override protected void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    //Runnable runnable = new Runnable() {
    //    @Override public void run() {
    //        try {
    //            contentInfos = JsonParse.getContentInfoByKey(mType);
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        } catch (JSONException e) {
    //            e.printStackTrace();
    //        }
    //        handler.post(new Runnable() {
    //            @Override public void run() {
    //                adapter = new ContentInfoAdapter(ContentInfoActivity.this,R.layout.content_item);
    //                progressBar.setVisibility(View.GONE);
    //                contentListView.setAdapter(adapter);
    //            }
    //        });
    //    }
    //};
}
