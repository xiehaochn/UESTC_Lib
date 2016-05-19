package com.hawx.uestc_lib.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hawx.uestc_lib.R;
import com.hawx.uestc_lib.adapter.GlobalLeftMenuAdapter;

import org.w3c.dom.Text;

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
    private String tag;
    private SharedPreferences sharedPreferences;
    private String bookOwner;
    private View vHeader;

    public GlobalLeftMenu(Context context, String tag) {
        super(context);
        this.context=context;
        this.tag=tag;
        init();
    }

    private void init() {
        sharedPreferences=context.getSharedPreferences("book",0);
        bookOwner=sharedPreferences.getString("bookOwner","nodata");
        Log.d("test", "bookOwner: "+bookOwner);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            vHeader = LayoutInflater.from(getContext()).inflate(R.layout.base_globalmenu_header_v21, null);
        }else {
            vHeader = LayoutInflater.from(getContext()).inflate(R.layout.base_globalmenu_header, null);
        }
        vHeader.setOnClickListener(this);
        addHeaderView(vHeader);
        setupHeader();
        setupAdapter();
    }

    private void setupAdapter() {
        adapter=new GlobalLeftMenuAdapter(context,tag);
        setAdapter(adapter);
    }

    private void setupHeader() {
            globalmenuavatar = (ImageView) vHeader.findViewById(R.id.globalmenu_avatar);
            globalmenuavatar.setImageResource(R.mipmap.icon_globalmenuitem_userdefaultpic);
            bookOwner=sharedPreferences.getString("bookOwner","nodata");
            TextView name= (TextView) vHeader.findViewById(R.id.globalmenu_name);
            ImageButton jiantou= (ImageButton) vHeader.findViewById(R.id.globalmenu_jiantou);
            if(!bookOwner.equals("nodata")) {
                name.setText(bookOwner);
                jiantou.setVisibility(INVISIBLE);
            }else{
                name.setText("请登录");
                jiantou.setVisibility(VISIBLE);
            }
    }

    @Override
    public void onClick(View v) {
        if (onHeaderClickListener != null) {
            onHeaderClickListener.onGlobalMenuHeaderClick(v);
        }
    }
    public interface OnHeaderClickListener {
        void onGlobalMenuHeaderClick(View v);
    }
    public void setOnHeaderClickListener(OnHeaderClickListener onHeaderClickListener) {
        this.onHeaderClickListener = onHeaderClickListener;
    }

    @Override
    public GlobalLeftMenuAdapter getAdapter() {
        return adapter;
    }
    public void changeData(){
        setupHeader();
    }
}
