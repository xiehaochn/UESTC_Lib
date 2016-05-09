package com.hawx.uestc_lib.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.hawx.uestc_lib.R;
import com.hawx.uestc_lib.base.BaseActivity;

/**
 * Created by Administrator on 2016/5/9.
 */
public class BookListActivity extends BaseActivity {
    private Toolbar toolbar;
    private String catalog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNeedLeftMenu(false);
        setContentView(R.layout.activity_booklist);
        initToolBar();
    }

    private void initToolBar() {
        toolbar= (Toolbar) findViewById(R.id.base_toolbar);
        catalog=getIntent().getExtras().getString("catalog");
        toolbar.setTitle(catalog);
        setSupportActionBar(toolbar);
    }
}
