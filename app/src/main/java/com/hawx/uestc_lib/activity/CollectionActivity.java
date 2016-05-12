package com.hawx.uestc_lib.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hawx.uestc_lib.R;
import com.hawx.uestc_lib.base.BaseActivity;

/**
 * Created by Administrator on 2016/5/12.
 */
public class CollectionActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTag("我的收藏");
        setContentView(R.layout.activity_collection);
    }
}
