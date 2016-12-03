package com.example.zhiwei.readbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhiwei.readbook.Model.BookInfo;
import com.example.zhiwei.readbook.R;

import com.example.zhiwei.readbook.tools.Utils;
import java.util.ArrayList;

/**
 * Created by zhiwei on 2016/5/23.
 */
public class BookInfoAdapter extends ArrayAdapter {

    private int resourceId;
    private Context context;
    private ArrayList<BookInfo> bookInfoList;

    public BookInfoAdapter(Context context, int resource, ArrayList<BookInfo> objects) {
        super(context, resource, objects);
        this.context = context;
        resourceId = resource;
        bookInfoList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BookInfo bookInfo = (BookInfo) getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) view.findViewById(R.id.tv_bookInfo_title);
            viewHolder.author = (TextView) view.findViewById(R.id.tv_bookInfo_author);
            viewHolder.pages = (TextView) view.findViewById(R.id.tv_bookInfo_pages);
            viewHolder.cover = (ImageView) view.findViewById(R.id.iv_bookInfo_cover);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.title.setText(bookInfo.getTitle());
        viewHolder.author.setText(bookInfo.getAuthor());
        viewHolder.pages.setText(bookInfo.getPages() + "页");
        if (bookInfo.getCover() != null) {
            viewHolder.cover.setImageBitmap(bookInfo.getCover());
        }
        return view;
    }

    class ViewHolder {
        private TextView title;
        private TextView author;
        private TextView pages;
        private ImageView cover;
    }

    public void addItems(ArrayList<BookInfo> newBooks) {
            bookInfoList.addAll(newBooks);
    }
}
