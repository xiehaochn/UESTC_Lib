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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hawx.uestc_lib.R;
import com.hawx.uestc_lib.adapter.BookListAdapter;
import com.hawx.uestc_lib.base.BaseActivity;
import com.hawx.uestc_lib.data.BookDetailData;
import com.hawx.uestc_lib.data.ResponseData;
import com.hawx.uestc_lib.data.ResultData;
import com.hawx.uestc_lib.widget.PullUpToRefresh;
import com.hawx.uestc_lib.widget.PullUpToRefreshIMP;

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
    private RequestQueue requestQueue;
    private int catalog_id;
    private static final String URL ="http://apis.juhe.cn/goodbook/query";
    public static final String APPKEY ="2f0af81454a23554f12f800d259e5e60";
    @BindView(R.id.activity_booklist_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.activity_booklist_pulluptorefreshimp)
    PullUpToRefreshIMP pullUpToRefreshIMP;

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
        JSONObject jsonObject=new JSONObject(responseData_string);
        ResultData resultData;
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
                        pullUpToRefreshIMP.setShouldIntercept(true);
                        //refreshCircle.setVisibility(View.VISIBLE);
                    }else{
                        pullUpToRefreshIMP.setShouldIntercept(false);
                    }
                }
            }
        });
        /*
        refreshCircle.setStartRefreshingListener(new PullUpToRefresh.startRefreshingListener() {
            @Override
            public void startRefreshing() {
                sendRefreshRequest();
            }
        });
        */
        pullUpToRefreshIMP.setStartRefreshingListener(new PullUpToRefreshIMP.startRefreshingListener() {
            @Override
            public void startRefreshing() {
                sendRefreshRequest();
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
    public void sendRefreshRequest(){
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.start();
        catalog_id=getIntent().getExtras().getInt("catalog_id");
        final int currentNum=adapter.getItemCount();
        int refreshedNum=10;
        final String requestBody="catalog_id="+catalog_id+"&pn="+currentNum+"&rn="+refreshedNum+"&dtype=&key="+APPKEY;
        String requestURL=URL+"?"+requestBody;
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, requestURL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ResponseData responseData=new ResponseData();
                try {
                    responseData=ResponseData.jsonToData(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(responseData.getResultcode().equals("200")) {
                    try {
                        String result = responseData.getResult().toString();
                        JSONObject jsonObject = new JSONObject(result);
                        ResultData resultData;
                        resultData = ResultData.jsonToData(jsonObject);
                        JSONArray jsonArray = resultData.getData();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            BookDetailData bookDetailData = BookDetailData.jsonToData((JSONObject) jsonArray.get(i));
                            bookDetailDatas.add(bookDetailData);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();
                    pullUpToRefreshIMP.setShouldIntercept(false);
                }else if(responseData.getResultcode().equals("202")){
                    toast("没有更多内容");
                } else{
                    toast("网络连接错误");
                }
                //refreshCircle.setRefreshing(false);
                pullUpToRefreshIMP.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                toast("网络连接错误");
                //refreshCircle.setRefreshing(false);
                pullUpToRefreshIMP.setRefreshing(false);
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
