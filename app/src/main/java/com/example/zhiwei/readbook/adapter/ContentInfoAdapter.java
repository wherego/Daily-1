package com.example.zhiwei.readbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.zhiwei.readbook.R;
import com.example.zhiwei.readbook.RxJava_Retrofit.model.GankContent;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by zhiwei on 2016/7/6.
 */
public class ContentInfoAdapter extends ArrayAdapter {

    private int resourceId;
    private ArrayList<GankContent> contentInfos;
    private Context context;
    private int[] backgroundColors = { R.color.colorPrimary, R.color.colorAccent, R.color.green,
        R.color.black, R.color.blue, R.color.dark_red, R.color.orange_button,
        R.color.background_blue, R.color.deep_green, R.color.primaryColor, R.color.color1,
        R.color.color2, R.color.color3, R.color.color4, R.color.color5, R.color.color6,
        R.color.color7, R.color.color8, R.color.color9, R.color.color10, R.color.color11,
        R.color.color12, R.color.color13, R.color.color14, R.color.color15, R.color.color16,
        R.color.color17, R.color.color18, };


    public ContentInfoAdapter(Context context, int resource,ArrayList<GankContent> contentInfos) {
        super(context, resource,contentInfos);
        this.context = context;
        resourceId = resource;
        this.contentInfos = contentInfos;
    }
    //public void setItems(ArrayList<GankContent> objects){
    //    contentInfos = objects;
    //    notifyDataSetChanged();
    //}

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        GankContent contentInfo = contentInfos.get(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.ContentTitle = (TextView) view.findViewById(R.id.txt_content_title);
            viewHolder.ContentAuthor = (TextView) view.findViewById(R.id.txt_content_author);
            viewHolder.backgroundImage = (ImageView) view.findViewById(R.id.img_content_img);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.ContentTitle.setText(contentInfo.desc);
        String date = contentInfo.createdAt.substring(0, 10);
        viewHolder.ContentAuthor.setText(contentInfo.who + "  " + date);
        Random random = new Random();
        viewHolder.backgroundImage.setBackgroundColor(
            getContext().getResources().getColor(backgroundColors[random.nextInt(28)]));
        return view;
    }


    class ViewHolder {
        TextView ContentTitle;
        TextView ContentAuthor;
        ImageView backgroundImage;
    }

}
