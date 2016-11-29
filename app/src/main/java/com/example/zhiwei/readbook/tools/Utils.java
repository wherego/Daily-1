package com.example.zhiwei.readbook.tools;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;

/**
 * Created by zhiwei on 2016/7/6.
 */
public class Utils {
    private static int screenWidth = 0;
    private static int screenHeight = 0;
    private static int ANIMATED_ITEM_COUNT = 5;
    private static int lastAnimatePosition = -1;


    public static int getScreenWidth(Context context) {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }
        return screenWidth;
    }


    public static int getScreenHeight(Context context) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }
        return screenHeight;
    }


    public static void runAnimation(Context context, View view, int position) {
        if (position >= ANIMATED_ITEM_COUNT) return;
        if (position > lastAnimatePosition) {
            lastAnimatePosition = position;
            view.setTranslationY(getScreenHeight(context));
            view.animate()
                .translationY(0)
                .setDuration(700)
                .setStartDelay(100*position)
                .setInterpolator(new DecelerateInterpolator(2.f))
                .start();
        }
    }

    public static LayoutAnimationController getListAnim(){
        AnimationSet set = new AnimationSet(true);
        ScaleAnimation sa = new ScaleAnimation(0,1,0,1);
        sa.setDuration(700);
        set.addAnimation(sa);
        LayoutAnimationController lac = new LayoutAnimationController(set,0.5f);
        return lac;
    }
}
