package com.hawx.uestc_lib.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.hawx.uestc_lib.R;
import com.hawx.uestc_lib.adapter.BookListAdapter;
import com.hawx.uestc_lib.base.BaseActivity;
import com.hawx.uestc_lib.data.BookDetailData;
import com.hawx.uestc_lib.data.ResponseData;
import com.hawx.uestc_lib.data.ResultData;
import com.hawx.uestc_lib.widget.PullUpToRefresh;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/9.
 */
public class BookListActivity extends BaseActivity {
    private Toolbar toolbar;
    private String catalog;
    private ArrayList<BookDetailData> bookDetailDatas=new ArrayList<BookDetailData>();
    private BookListAdapter adapter;
    private LinearLayoutManager layoutManager;
    @BindView(R.id.activity_booklist_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.activity_booklist_pulluptorefresh)
    PullUpToRefresh refreshCircle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNeedLeftMenu(false);
        setContentView(R.layout.activity_booklist);
        ButterKnife.bind(this);
        initToolBar();
        try {
            initData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initData() throws JSONException {
        String responseData_string=getIntent().getExtras().getString("data");
        log(responseData_string);
        JSONObject jsonObject=new JSONObject(responseData_string);
        ResultData resultData=new ResultData();
        resultData=ResultData.jsonToData(jsonObject);
        bookDetailDatas.clear();
        JSONArray jsonArray=resultData.getData();
        for(int i=0;i<jsonArray.length();i++){
            BookDetailData bookDetailData=BookDetailData.jsonToData((JSONObject) jsonArray.get(i));
            bookDetailDatas.add(bookDetailData);
        }
        adapter=new BookListAdapter(bookDetailDatas,this);
        recyclerView.setAdapter(adapter);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState== RecyclerView.SCROLL_STATE_IDLE){
                    if(layoutManager.findLastCompletelyVisibleItemPosition()==adapter.getItemCount()-1){
                        refreshCircle.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        refreshCircle.setStartRefreshingListener(new PullUpToRefresh.startRefreshingListener() {
            @Override
            public void startRefreshing() {
                toast("Start Refreshing");
                refreshCircle.postDelayed(new Runnable() {
                    @Override
                      public void run() {
                        refreshCircle.setRefreshing(false);
                        toast("End Refreshing");
                    }
                },5000);
            }
        });
    }

    private void initToolBar() {
        toolbar= (Toolbar) findViewById(R.id.base_toolbar);
        catalog=getIntent().getExtras().getString("catalog");
        toolbar.setTitle(catalog);
        toolbar.setNavigationIcon(R.mipmap.icon_navigation_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        bookDetailDatas.clear();
        super.onDestroy();
    }

}
