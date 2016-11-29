package com.example.zhiwei.readbook.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhiwei.readbook.Model.Book;
import com.example.zhiwei.readbook.R;
import com.example.zhiwei.readbook.tools.Utils;
import com.gc.materialdesign.views.ProgressBarDeterminate;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by zhiwei on 2016/5/20.
 */
public class ReadingBookAdapter extends ArrayAdapter<Book> {

    private int resourceId;
    private Context context;

    public ReadingBookAdapter(Context context, int resourceId, ArrayList<Book> objects) {
        super(context, resourceId, objects);
        this.context = context;
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Book readingBook = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.bookCoverView = (ImageView) view.findViewById(R.id.bookCoverView);
            viewHolder.bookName = (TextView) view.findViewById(R.id.bookNameView);
            viewHolder.bookAuthor = (TextView) view.findViewById(R.id.bookAuthorView);
            viewHolder.readProgress = (TextView) view.findViewById(R.id.readBookProgressView);
            viewHolder.readProgressBar = (ProgressBarDeterminate) view.findViewById(R.id.readProgressBar);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        Utils.runAnimation(context,view,position);
        File bookcoverFile = new File(readingBook.getCoverFile());
        if (bookcoverFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(readingBook.getCoverFile());
            viewHolder.bookCoverView.setImageBitmap(bitmap);
        }
        viewHolder.bookName.setText(readingBook.getName());
        viewHolder.bookAuthor.setText(readingBook.getAuthor());
        String progressText = readingBook.getReadPages() + "/" + readingBook.getPages();
        viewHolder.readProgress.setText(progressText);
        viewHolder.readProgressBar.setMax(readingBook.getPages());
        viewHolder.readProgressBar.setProgress(readingBook.getReadPages());
        return view;
    }

    class ViewHolder {
        ImageView bookCoverView;
        TextView bookName;
        TextView bookAuthor;
        TextView readProgress;
        ProgressBarDeterminate readProgressBar;
    }
}
