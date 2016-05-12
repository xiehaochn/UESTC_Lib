package com.hawx.uestc_lib.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hawx.uestc_lib.R;
import com.hawx.uestc_lib.base.BaseActivity;

/**
 * Created by Administrator on 2016/5/12.
 */
public class UserCenterActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTag("个人中心");
        setContentView(R.layout.activity_usercenter);
    }
}
