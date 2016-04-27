package com.hawx.uestc_lib.base;



import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hawx.uestc_lib.R;
import com.hawx.uestc_lib.widget.SlideFrameLayout;

import java.util.ArrayList;

/**
 * BaseActivity:适配status bar在不同版本的呈现效果，加入左滑退出Activity
 * @author Hawx
 * @version 1.0
 *
 */
public class BaseActivity extends AppCompatActivity {
    /** 当前activity列表*/
    private static ArrayList<BaseActivity> baseActivities=new ArrayList<BaseActivity>();
    private SlideFrameLayout slideFrameLayout;
    private View contentView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        add();
    }
    protected void add(){
        baseActivities.add(this);
        log("baseActivities add:"+this);
    }

    @Override
    public void finish() {
        if(baseActivities.contains(this)) {
            baseActivities.remove(this);
            log("baseActivities remove:"+this);
        }
        super.finish();
    }

    protected void finishAll() {
        if(!baseActivities.isEmpty()){
            log("baseActivities finishAll:");
            for (BaseActivity baseActivity:baseActivities){
                log("baseActivities Remove:"+baseActivity);
                baseActivity.finish();
            }
        }
    }
    protected void toast(String text){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }
    protected void log(String text){
        Log.d("EasyLog",text);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if(slideFrameLayout ==null){
            slideFrameLayout =new SlideFrameLayout(this,BaseActivity.this);
        }else {
            slideFrameLayout.removeAllViews();
        }
        LayoutInflater layoutInflater=LayoutInflater.from(this);
        contentView=layoutInflater.inflate(layoutResID,null);
        contentView.setBackgroundResource(R.drawable.base_background);
        LinearLayout.LayoutParams fp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        slideFrameLayout.addView(contentView,fp);
        super.setContentView(slideFrameLayout);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if(slideFrameLayout ==null){
            slideFrameLayout =new SlideFrameLayout(this,BaseActivity.this);
        }else {
            slideFrameLayout.removeAllViews();
        }
        contentView=view;
        contentView.setBackgroundResource(R.drawable.base_background);
        LinearLayout.LayoutParams fp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        slideFrameLayout.addView(contentView,fp);
        super.setContentView(slideFrameLayout,params);
    }

    @Override
    public void setContentView(View view) {
        if(slideFrameLayout ==null){
            slideFrameLayout =new SlideFrameLayout(this,BaseActivity.this);
        }else {
            slideFrameLayout.removeAllViews();
        }
        contentView=view;
        contentView.setBackgroundResource(R.drawable.base_background);
        LinearLayout.LayoutParams fp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        slideFrameLayout.addView(contentView,fp);
        super.setContentView(slideFrameLayout);
    }
}
