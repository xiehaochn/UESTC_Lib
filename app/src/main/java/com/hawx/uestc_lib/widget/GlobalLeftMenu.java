package com.hawx.uestc_lib.widget;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.hawx.uestc_lib.R;
import com.hawx.uestc_lib.adapter.GlobalLeftMenuAdapter;

/**
 * 抽屉导航
 * @author frogermcs
 * @version 1.0
 *
 */
public class GlobalLeftMenu  extends ListView implements View.OnClickListener{
    private OnHeaderClickListener onHeaderClickListener;
    private Context context;
    private GlobalLeftMenuAdapter adapter;
    private ImageView globalmenuavatar;

    public GlobalLeftMenu(Context context) {
        super(context);
        this.context=context;
        init();
    }

    private void init() {
        setChoiceMode(CHOICE_MODE_SINGLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setDivider(getResources().getDrawable(android.R.color.transparent,null));
        }else {
            setDivider(getResources().getDrawable(android.R.color.transparent));
        }
        setDividerHeight(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark,null));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        else{
            setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }

        setupHeader();
        setupAdapter();
    }

    private void setupAdapter() {
        adapter=new GlobalLeftMenuAdapter(context);
        setAdapter(adapter);
    }

    private void setupHeader() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View vHeader = LayoutInflater.from(getContext()).inflate(R.layout.base_globalmenu_header_v21, null);
            globalmenuavatar = (ImageView) vHeader.findViewById(R.id.globalmenu_avatar);
            globalmenuavatar.setImageResource(R.mipmap.icon_globalmenuitem_userdefaultpic);
            addHeaderView(vHeader);
            vHeader.setOnClickListener(this);
        }else{
            View vHeader = LayoutInflater.from(getContext()).inflate(R.layout.base_globalmenu_header, null);
            globalmenuavatar = (ImageView) vHeader.findViewById(R.id.globalmenu_avatar);
            globalmenuavatar.setImageResource(R.mipmap.icon_globalmenuitem_userdefaultpic);
            addHeaderView(vHeader);
            vHeader.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        if (onHeaderClickListener != null) {
            onHeaderClickListener.onGlobalMenuHeaderClick(v);
        }
    }
    public interface OnHeaderClickListener {
        public void onGlobalMenuHeaderClick(View v);
    }
    public void setOnHeaderClickListener(OnHeaderClickListener onHeaderClickListener) {
        this.onHeaderClickListener = onHeaderClickListener;
    }
}
