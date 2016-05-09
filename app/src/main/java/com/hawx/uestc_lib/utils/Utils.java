package com.hawx.uestc_lib.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Administrator on 2016/4/20.
 */
public class Utils  {
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
    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }
    public static int getStatusBarHeight(Activity activity){
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }
}
