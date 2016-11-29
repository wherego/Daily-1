package com.example.zhiwei.readbook.Model;

/**
 * Created by zhiwei on 2016/5/20.
 */
public class DrawerItem {
    private int itemIconId;
    private String itemName;

    public DrawerItem(int itemIconId, String itemName) {
        this.itemIconId = itemIconId;
        this.itemName = itemName;
    }

    public int getItemIconId() {
        return itemIconId;
    }

    public String getItemName() {
        return itemName;
    }
}
