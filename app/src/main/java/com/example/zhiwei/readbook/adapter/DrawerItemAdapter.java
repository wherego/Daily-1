package com.example.zhiwei.readbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhiwei.readbook.Model.DrawerItem;
import com.example.zhiwei.readbook.R;

import java.util.ArrayList;

/**
 * Created by zhiwei on 2016/5/20.
 */
public class DrawerItemAdapter extends ArrayAdapter<DrawerItem> {

    private int resourceId;

    public DrawerItemAdapter(Context context, int resourceId, ArrayList<DrawerItem> objects) {
        super(context, resourceId, objects);
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DrawerItem drawerItem = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.itemIcon = (ImageView) view.findViewById(R.id.drawerItem_icon_view);
            viewHolder.itemName = (TextView) view.findViewById(R.id.drawerItem_name_view);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.itemIcon.setImageResource(drawerItem.getItemIconId());
        viewHolder.itemName.setText(drawerItem.getItemName());
        return view;
    }

    class ViewHolder{
        private ImageView itemIcon;
        private TextView itemName;
    }
}
