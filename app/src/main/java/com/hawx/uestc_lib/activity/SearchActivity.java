package com.hawx.uestc_lib.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hawx.uestc_lib.R;
import com.hawx.uestc_lib.base.BaseActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/12.
 */
public class SearchActivity extends BaseActivity {
    private final String url="http://webpac.uestc.edu.cn/search~S1*chx/?searchtype=X&searcharg=";
    private final String url_detail="http://webpac.uestc.edu.cn";
    private final String RESULT_URL="RESULT_URL_KEY";
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> result_string=new ArrayList<String>();
    private ArrayList<String> result_link=new ArrayList<String>();
    private RequestQueue requestQueue;
    private String searchContent;
    private String urlFinal;
    private boolean searchFlag=false;
    @BindView(R.id.activity_search_edittext)
    EditText editText;
    @BindView(R.id.activity_search_button)
    ImageButton imageButton;
    @BindView(R.id.activity_search_textview)
    TextView textView;
    @BindView(R.id.activity_search_listview)
    ListView listView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTag("馆藏查询");
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.start();
        arrayAdapter=new ArrayAdapter<String>(this,R.layout.listview_item_searchresult,result_string);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url_detail_final=url_detail+result_link.get(position);
                startResultActivity(SearchActivity.this,url_detail_final);
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchFlag){
                    return;
                }
                searchContent=editText.getText().toString();
                if(searchContent.equals("")){
                    imageButton.startAnimation(AnimationUtils.loadAnimation(SearchActivity.this,R.anim.anim_searchbutton_error));
                    toast("请输入搜索内容");
                }else {
                    searchFlag=true;
                    textView.setText("搜索中...");
                    String searchContent_UTF8 = null;
                    try {
                        searchContent_UTF8 = URLEncoder.encode(searchContent, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    urlFinal = url + searchContent_UTF8;
                    requestQueue.add(new StringRequest(Request.Method.GET, urlFinal, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            parseResult(response);
                            textView.setVisibility(View.INVISIBLE);
                            listView.setVisibility(View.VISIBLE);
                            searchFlag=false;
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            textView.setText("网络连接错误");
                            toast("网络连接错误");
                        }
                    }));
                }
            }
        });
    }

    private void parseResult(String response) {
        result_string.clear();
        result_link.clear();
        arrayAdapter.clear();
        Document doc = Jsoup.parse(response);
        Elements titles =doc.getElementsByClass("briefcitTitle");
        for (Element title : titles) {
            Elements links = title.getElementsByTag("a");
            for (Element link : links) {
                String linkHref = link.attr("href");
                result_link.add(linkHref);
            }
            String mtext = title.text();
            result_string.add(mtext);
        }
        arrayAdapter.notifyDataSetChanged();
    }
    private void startResultActivity(Context context, String url){
        Intent intent=new Intent(context,SearchDetailActivity.class);
        intent.putExtra(RESULT_URL,url);
        startActivity(intent);
    }
}
