package com.hawx.uestc_lib.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hawx.uestc_lib.R;
import com.hawx.uestc_lib.base.BaseActivity;

/**
 * Created by Administrator on 2016/5/12.
 */
public class AboutActivity extends BaseActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNeedLeftMenu(false);
        setContentView(R.layout.activity_about);
        initToolBar();
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.base_toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setSubtitle("关于");
        toolbar.setSubtitleTextAppearance(this,R.style.SubTitleText);
        toolbar.setNavigationIcon(R.mipmap.icon_navigation_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
