package com.hawx.uestc_lib.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Administrator on 2016/4/20.
 */
public class Utils {
    public static int dpTopx(Context context, int dp){
        // 获得像素密度
        final float scale = context.getResources().getDisplayMetrics().density;
         // 四舍五入dp值乘像素密度
        return (int) (dp * scale + 0.5f);
    }
    public static int getWindowWidth(Context context){
        WindowManager windowManager=(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }
    public static int getWindowHeight(Context context){
        WindowManager windowManager=(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }
}
