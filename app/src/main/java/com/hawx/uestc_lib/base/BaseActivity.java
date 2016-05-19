package com.hawx.uestc_lib.base;




import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hawx.uestc_lib.R;
import com.hawx.uestc_lib.activity.LoginActivity;
import com.hawx.uestc_lib.adapter.GlobalLeftMenuAdapter;
import com.hawx.uestc_lib.utils.DrawerLayoutInstaller;
import com.hawx.uestc_lib.utils.Utils;
import com.hawx.uestc_lib.widget.GlobalLeftMenu;
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
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private int version;
    private boolean needLeftMenu=true;
    private String tag;
    private GlobalLeftMenu globalLeftMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        version=Build.VERSION.SDK_INT;
        if (version>= Build.VERSION_CODES.KITKAT&&version<=Build.VERSION_CODES.LOLLIPOP) {
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
    }

    @Override
    public void finish() {
        if(baseActivities.contains(this)) {
            baseActivities.remove(this);
        }
        super.finish();
    }

    protected void finishAll() {
        if(!baseActivities.isEmpty()){
            for (BaseActivity baseActivity:baseActivities){
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
        LinearLayout.LayoutParams fp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        slideFrameLayout.addView(contentView,fp);
        super.setContentView(slideFrameLayout);
        if(needLeftMenu) {
            setupDrawer();
        }
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
        LinearLayout.LayoutParams fp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        slideFrameLayout.addView(contentView,fp);
        super.setContentView(slideFrameLayout,params);
        if(needLeftMenu) {
            setupDrawer();
        }
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
        LinearLayout.LayoutParams fp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        slideFrameLayout.addView(contentView,fp);
        super.setContentView(slideFrameLayout);
        if(needLeftMenu) {
            setupDrawer();
        }
    }
    /**注入抽屉导航*/
    private void setupDrawer() {
        globalLeftMenu=new GlobalLeftMenu(this,tag);
        toolbar= (Toolbar) slideFrameLayout.findViewById(R.id.base_toolbar);
        toolbar.setSubtitle(tag);
        toolbar.setSubtitleTextAppearance(this,R.style.SubTitleText);
        setSupportActionBar(toolbar);
        drawerLayout = DrawerLayoutInstaller.from(this)
                .drawerRoot(R.layout.base_drawerlayout)
                .drawerLeftView(globalLeftMenu)
                .drawerLeftWidth(Utils.dpTopx(this,250))
                .withNavigationIconToggler(toolbar)
                .build(version);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
                switch (newState) {
                    case DrawerLayout.STATE_IDLE: {
                        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                            slideFrameLayout.setDoNotIntercept(true);
                        } else {
                            slideFrameLayout.setDoNotIntercept(false);
                        }
                        break;
                    }
                    case DrawerLayout.STATE_DRAGGING: {
                        slideFrameLayout.setDoNotIntercept(true);
                        break;
                    }
                    case DrawerLayout.STATE_SETTLING: {
                        slideFrameLayout.setDoNotIntercept(true);
                        break;
                    }
                }
            }
        };
        mActionBarDrawerToggle.syncState();
        drawerLayout.addDrawerListener(mActionBarDrawerToggle);
        globalLeftMenu.getAdapter().setListener(new GlobalLeftMenuAdapter.NeedCloseDrawerListener() {
            @Override
            public void needCloseDrawer() {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        globalLeftMenu.setOnHeaderClickListener(new GlobalLeftMenu.OnHeaderClickListener() {
            @Override
            public void onGlobalMenuHeaderClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent intent=new Intent(BaseActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setNeedLeftMenu(boolean needLeftMenu) {
        this.needLeftMenu = needLeftMenu;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    protected void onResume() {
        if(globalLeftMenu!=null)
        globalLeftMenu.changeData();
        super.onResume();
    }
    protected void setNeedSlideFinish(boolean onOff){
        slideFrameLayout.setOnOff(onOff);
    }
}
