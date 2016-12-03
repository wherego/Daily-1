package com.example.zhiwei.readbook.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhiwei.readbook.R;

public class BookInfoActivity extends BaseActivity {

    private TextView titleView, authorView,pagesView,summaryView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String author = intent.getStringExtra("author");
        int pages = intent.getIntExtra("pages",0);
        String summary = intent.getStringExtra("summary");


        titleView = (TextView) findViewById(R.id.tv_bookInfo_title);
        authorView = (TextView) findViewById(R.id.tv_bookInfo_author);
        pagesView = (TextView) findViewById(R.id.tv_bookInfo_pages);
        summaryView = (TextView) findViewById(R.id.tv_bookInfo_summary);
        imageView = (ImageView) findViewById(R.id.bookCoverView);

        titleView.setText(title);
        authorView.setText(author);
        pagesView.setText(pages+"页");
        summaryView.setText(summary);

    }
}
