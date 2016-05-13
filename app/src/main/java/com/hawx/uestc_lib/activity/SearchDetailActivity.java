package com.hawx.uestc_lib.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hawx.uestc_lib.R;
import com.hawx.uestc_lib.base.BaseActivity;
import com.hawx.uestc_lib.data.SearchDetailData;
import com.hawx.uestc_lib.widget.TableLayout;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/13.
 */
public class SearchDetailActivity extends BaseActivity {
    private final String RESULT_URL="RESULT_URL_KEY";
    private final String RESULT_TITLE="RESULT_TITLE_KEY";
    private final int START_FOR_RESULT_CODE=1;
    private Toolbar toolbar;
    private ArrayList<String> ItemsEntrys=new ArrayList<String>();
    private ArrayList<String> InfoEntrys=new ArrayList<String>();
    private JSONArray collection=new JSONArray();
    private boolean alreadyCollect=false;
    private boolean isDataChange=false;
    private SharedPreferences sharedPreferences;
    @BindView(R.id.activity_searchdetail_author)
    TextView author;
    @BindView(R.id.activity_searchdetail_bookname)
    TextView book_name;
    @BindView(R.id.activity_searchdetail_publisher)
    TextView publisher;
    @BindView(R.id.activity_searchdetail_carrier)
    TextView carrier;
    @BindView(R.id.activity_searchdetail_annotation)
    TextView annotation;
    @BindView(R.id.activity_searchdetail_booktheme)
    TextView book_theme;
    @BindView(R.id.activity_searchdetail_otherone)
    TextView otherone;
    @BindView(R.id.activity_searchdetail_extraname)
    TextView extra_name;
    @BindView(R.id.activity_searchdetail_standardnum)
    TextView standard_num;
    @BindView(R.id.activity_searchdetail_callnum)
    TextView call_num;
    @BindView(R.id.activity_searchdetail_tablecontainer)
    LinearLayout table_container;
    @BindView(R.id.activity_searchdetail_pretextview)
    TextView pre_text;
    @BindView(R.id.activity_searchdetail_scrollview)
    ScrollView scrollView;
    @BindView(R.id.activity_searchdetail_listtitletext)
    TextView listTitle;
    private String[] matcher=new String[10];
    private RequestQueue requestQueue;
    private int bookNum;
    private Toolbar.OnMenuItemClickListener menuItemClickListener;
    private SharedPreferences.Editor editor;
    private String url_detail;
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNeedLeftMenu(false);
        setContentView(R.layout.activity_searchdetail);
        ButterKnife.bind(this);
        try {
            checkCollection();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initToolBar();
        intiMatcher();
        getInformation();
    }

    private void checkCollection() throws JSONException {
        url_detail=getIntent().getExtras().getString(RESULT_URL);
        title=getIntent().getExtras().getString(RESULT_TITLE);
        sharedPreferences=getSharedPreferences("collection",0);
        editor=sharedPreferences.edit();
        String data=sharedPreferences.getString("data","nodata");
        if(data.equals("nodata")){
            editor.putString("data",collection.toString());
            editor.apply();
        }else {
            collection = new JSONArray(data);
            for(int i=0;i<collection.length();i++){
                JSONObject jsonObject= (JSONObject) collection.get(i);
                String title_contain=jsonObject.getString("title");
                if(title_contain.equals(title)){
                    alreadyCollect=true;
                    break;
                }
            }

        }
    }

    private void initToolBar() {
        toolbar= (Toolbar) findViewById(R.id.base_toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setSubtitle("书籍详情");
        toolbar.setSubtitleTextAppearance(this,R.style.SubTitleText);
        toolbar.setNavigationIcon(R.mipmap.icon_navigation_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        menuItemClickListener=new Toolbar.OnMenuItemClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_collection:{
                        isDataChange=true;
                        if(alreadyCollect){
                            for(int i=0;i<collection.length();i++){
                                JSONObject jsonObject= null;
                                try {
                                    jsonObject = (JSONObject) collection.get(i);
                                    String title_contain=jsonObject.getString("title");
                                    if(title_contain.equals(title)){
                                        collection.remove(i);
                                        alreadyCollect=false;
                                        break;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            item.setIcon(R.mipmap.icon_menu_collection);
                            editor.putString("data",collection.toString());
                            editor.apply();
                            toast("取消收藏");
                        }else{
                            SearchDetailData searchDetailData=new SearchDetailData(title,url_detail);
                            JSONObject jsonObject= null;
                            try {
                                jsonObject = searchDetailData.toJsonObject();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            collection.put(jsonObject);
                            editor.putString("data",collection.toString());
                            editor.apply();
                            alreadyCollect=true;
                            toast("收藏成功");
                            item.setIcon(R.mipmap.icon_menu_collection_alreadycollect);
                            }
                        break;
                    }
                    default:
                        break;
                }
                return true;
            }
        };
        toolbar.setOnMenuItemClickListener(menuItemClickListener);
    }
    private void intiMatcher() {
        matcher[0]="主要责任者";
        matcher[1]="题名";
        matcher[2]="出版者";
        matcher[3]="载体形态";
        matcher[4]="附注";
        matcher[5]="主题";
        matcher[6]="标准号";
        matcher[7]="索书号";
        matcher[8]="其他责任人";
        matcher[9]="附加题名";
    }
    private void getInformation() {
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.start();
        requestQueue.add(new StringRequest(Request.Method.GET, url_detail, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseData(response);
                pre_text.setVisibility(View.INVISIBLE);
                scrollView.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                toast("网络连接错误");
                pre_text.setText("网络连接错误");
            }
        }));
    }

    private void parseData(String response) {
        Document doc = Jsoup.parse(response);
        Elements itemsentrys = doc.getElementsByClass("bibItemsEntry");
        for (Element itemsentry : itemsentrys) {
            Elements tds = itemsentry.getElementsByTag("td");
            bookNum+=1;
            for (Element td : tds) {
                String td_string = td.text();
                ItemsEntrys.add(td_string);
            }
        }
        Elements infoentrys=doc.getElementsByClass("bibInfoEntry");
        for(Element infoentry:infoentrys){
            String infoentry_string=infoentry.text();
            InfoEntrys.add(infoentry_string);
        }
        setTable();
        matchingAndDivision(InfoEntrys);
    }
    private void setTable() {
        if(bookNum==0){
            listTitle.setText("暂无相关馆藏信息");
        }else {
            for (int i = 0; i < bookNum; i++) {
                TableLayout tl = generateTableView(i);
                table_container.addView(tl);
            }
        }

    }
    private void matchingAndDivision(ArrayList<String> arrayList) {
        String author_regular = "("+matcher[0]+")"+"(.*)"+"("+matcher[1]+")";
        Pattern author_pattern = Pattern.compile(author_regular);
        Matcher author_m = author_pattern.matcher(arrayList.get(0));
        if (author_m.find( )) {
            author.setText(author_m.group(2));
        } else {
            author.setText("暂无相关信息");
        }
        String bookname_regular = "("+matcher[1]+")"+"(.*)"+"("+matcher[2]+")";
        Pattern bookname_pattern = Pattern.compile(bookname_regular);
        Matcher bookname_m = bookname_pattern.matcher(arrayList.get(0));
        if (bookname_m.find( )) {
            book_name.setText(bookname_m.group(2));
        } else {
            book_name.setText("暂无相关信息");
        }
        String publisher_regular = "("+matcher[2]+")"+"(.*)";
        Pattern publisher_pattern = Pattern.compile(publisher_regular);
        Matcher publisher_m = publisher_pattern.matcher(arrayList.get(0));
        if (publisher_m.find( )) {
            publisher.setText(publisher_m.group(2));
        } else {
            publisher.setText("暂无相关信息");
        }
        String carrier_regular = "("+matcher[3]+")"+"(.*)"+"("+matcher[4]+")";
        Pattern carrier_pattern = Pattern.compile(carrier_regular);
        Matcher carrier_m = carrier_pattern.matcher(arrayList.get(2));
        if (carrier_m.find( )) {
            carrier.setText(carrier_m.group(2));
        } else {
            carrier.setText("暂无相关信息");
        }
        String annotation_regular = "("+matcher[4]+")"+"(.*)"+"("+matcher[5]+")";
        Pattern annotation_pattern = Pattern.compile(annotation_regular);
        Matcher annotation_m= annotation_pattern.matcher(arrayList.get(2));
        if (annotation_m.find( )) {
            annotation.setText(annotation_m.group(2));
        } else {
            annotation.setText("暂无相关信息");
        }
        //============================================================================================================
        //   主题---------其他责任人-----附加题名---------标准号-----------索书号
        //     5--------------8--------------9---------------6---------------7
        //book_theme------otherone------extra_name------standard_num------call_num
        //
        //
        //               |exist-8|                 |exist-9|                   |exist-6|                   |branch-1 end|
        //        |------|finded|-----|8-9|-------|finded|------|9-6|----------|finded|---------|6-7|---------|finded|
        //        |      |set-5|            |     |set-8|              |        |set-9|                        |set-6|
        //        |                         |                          |
        //|5-8|---|                         |                          |    |not exist-6|                  |branch-2 end|
        //        |                         |                          |-----|else|-----------|9-7|-----------|finded|
        //        |                         |                                                                 |set-9|
        //        |                         |
        //        |                         |    |not exist-9|                 |exist-6|                     |branch-3 end|
        //        |                         |------|else|--------|8-6|----------|finded|--------|6-7|----------|finded|
        //        |                                                     |        |set-8|                        |set-6|
        //        |                                                     |
        //        |                                                     |     |not exist-6|                  |branch-4 end|
        //        |                                                     |-------|else|-----------|8-7|-----------|finded|
        //        |                                                                                              |set-8|
        //        |
        //        |   |not exist-8|                |exist-9|                      |exist-6|                    |branch-5 end|
        //        |-----|else|---------|5-9|--------|finded|------|9-6|-----------|finded|---------|6-7|-----------|finded|
        //                                   |      |set-5|              |        |set-9|                          |set-6|
        //                                   |                           |
        //                                   |                           |      |not exist-6|                   |branch-6 end|
        //                                   |                           |--------|else|------------|9-7|----------|finded|
        //                                   |                                                                      |set-9|
        //                                   |
        //                                   |      |not exist-9|                    |exist-6|                   |branch-7 end|
        //                                   |------|else|----------|5-6|------------|finded|---------|6-7|---------|finded|
        //                                                                  |        |set-5|                        |set-6|
        //                                                                  |
        //                                                                  |         |not exist-6|              |branch-8 end|
        //                                                                  |---------|else|-----------|5-7|---------|finded|
        //                                                                                                          |set-5|
        //====================================================================================================================================
        String booktheme_regular = "("+matcher[5]+")"+"(.*)"+"("+matcher[8]+")";//find 5-8
        Pattern booktheme_pattern = Pattern.compile(booktheme_regular);
        Matcher booktheme_m= booktheme_pattern.matcher(arrayList.get(2));
        if (booktheme_m.find( )) {
            //exist-8
            book_theme.setText(booktheme_m.group(2));//set-5
            String e1_r = "("+matcher[8]+")"+"(.*)"+"("+matcher[9]+")";//find 8-9
            Pattern e1_p = Pattern.compile(e1_r);
            Matcher e1_m= e1_p.matcher(arrayList.get(2));
            if(e1_m.find()){
                //exist-9
                otherone.setText(e1_m.group(2));//set-8
                String e2_r = "("+matcher[9]+")"+"(.*)"+"("+matcher[6]+")";//find 9-6
                Pattern e2_p = Pattern.compile(e2_r);
                Matcher e2_m= e2_p.matcher(arrayList.get(2));
                if(e2_m.find()){
                    //exist-6
                    extra_name.setText(e2_m.group(2));//set-9
                    String e3_r = "("+matcher[6]+")"+"(.*)"+"("+matcher[7]+")";//find 6-7
                    Pattern e3_p = Pattern.compile(e3_r);
                    Matcher e3_m= e3_p.matcher(arrayList.get(2));
                    if(e3_m.find()){
                        standard_num.setText(e3_m.group(2));//set-6,branch-1 end
                    }
                }else{
                    // not exist-6
                    String e4_r = "("+matcher[9]+")"+"(.*)"+"("+matcher[7]+")";//find 9-7
                    Pattern e4_p = Pattern.compile(e4_r);
                    Matcher e4_m= e4_p.matcher(arrayList.get(2));
                    if(e4_m.find()){
                        extra_name.setText(e4_m.group(2));//set-9,branch-2 end
                    }
                }
            }else{
                //not exist-9
                String e5_r = "("+matcher[8]+")"+"(.*)"+"("+matcher[6]+")";//find 8-6
                Pattern e5_p = Pattern.compile(e5_r);
                Matcher e5_m= e5_p.matcher(arrayList.get(2));
                if(e5_m.find()){
                    //exist-6
                    otherone.setText(e5_m.group(2));//set-8
                    String e6_r = "("+matcher[6]+")"+"(.*)"+"("+matcher[7]+")";//find 6-7
                    Pattern e6_p = Pattern.compile(e6_r);
                    Matcher e6_m= e6_p.matcher(arrayList.get(2));
                    if(e6_m.find()){
                        standard_num.setText(e6_m.group(2));//set-6,branch-3 end
                    }else{

                    }

                }else{
                    //not exist-6
                    String e7_r = "("+matcher[8]+")"+"(.*)"+"("+matcher[7]+")";//find 8-7
                    Pattern e7_p = Pattern.compile(e7_r);
                    Matcher e7_m= e7_p.matcher(arrayList.get(2));
                    if(e7_m.find()){
                        otherone.setText(e7_m.group(2));//set-8,branch-4 end
                    }
                }
            }
        } else {
            //not exist-8
            String e9_r = "("+matcher[5]+")"+"(.*)"+"("+matcher[9]+")";//find 5-9
            Pattern e9_p = Pattern.compile(e9_r);
            Matcher e9_m= e9_p.matcher(arrayList.get(2));
            if (e9_m.find( )) {
                //exist-9
                book_theme.setText(e9_m.group(2));//set-5
                String e10_r = "("+matcher[9]+")"+"(.*)"+"("+matcher[6]+")";//find 9-6
                Pattern e10_p = Pattern.compile(e10_r);
                Matcher e10_m= e10_p.matcher(arrayList.get(2));
                if(e10_m.find()){
                    //exist-6
                    extra_name.setText(e10_m.group(2));//set-9
                    String e11_r = "("+matcher[6]+")"+"(.*)"+"("+matcher[7]+")";//find 6-7
                    Pattern e11_p = Pattern.compile(e11_r);
                    Matcher e11_m= e11_p.matcher(arrayList.get(2));
                    if(e11_m.find()){
                        standard_num.setText(e11_m.group(2));//set-6,branch-5,end
                    }
                }else{
                    //not exist-6
                    String e12_r = "("+matcher[9]+")"+"(.*)"+"("+matcher[7]+")";//find 9-7
                    Pattern e12_p = Pattern.compile(e12_r);
                    Matcher e12_m= e12_p.matcher(arrayList.get(2));
                    if(e12_m.find()){
                        extra_name.setText(e12_m.group(2));//set-9,branch-6 end
                    }
                }
            } else {
                //not exist-9
                String e13_r = "("+matcher[5]+")"+"(.*)"+"("+matcher[6]+")";//find 5-6
                Pattern e13_p = Pattern.compile(e13_r);
                Matcher e13_m= e13_p.matcher(arrayList.get(2));
                if(e13_m.find()){
                    //exist-6
                    book_theme.setText(e13_m.group(2));//set-5
                    String e14_r = "("+matcher[6]+")"+"(.*)"+"("+matcher[7]+")";//find 6-7
                    Pattern e14_p = Pattern.compile(e14_r);
                    Matcher e14_m= e14_p.matcher(arrayList.get(2));
                    if(e14_m.find()){
                        standard_num.setText(e14_m.group(2));//set-6,branch-7 end
                    }
                }else{
                    //not exist-6
                    String e15_r = "("+matcher[5]+")"+"(.*)"+"("+matcher[7]+")";//find 5-7
                    Pattern e15_p = Pattern.compile(e15_r);
                    Matcher e15_m= e15_p.matcher(arrayList.get(2));
                    if(e15_m.find()){
                        book_theme.setText(e15_m.group(2));//set-5,branch-8 end
                    }
                }
            }
        }
        checkIfEmpty();
        //=======================================================================================================================
        String callnum_regular = "("+matcher[7]+")"+"(.*)";
        Pattern callnum_pattern = Pattern.compile(callnum_regular);
        Matcher callnum_m= callnum_pattern.matcher(arrayList.get(2));
        if (callnum_m.find( )) {
            call_num.setText(callnum_m.group(2));
        } else {
            call_num.setText("暂无相关信息");
        }
    }

    private void checkIfEmpty() {
        if(book_theme.getText().toString().equals("")){
            book_theme.setText("暂无相关信息");
        }
        if(otherone.getText().toString().equals("")){
            otherone.setText("暂无相关信息");
        }
        if(extra_name.getText().toString().equals("")){
            extra_name.setText("暂无相关信息");
        }
        if(standard_num.getText().toString().equals("")){
            standard_num.setText("暂无相关信息");
        }
    }

    private TableLayout generateTableView(int i) {
        TableLayout tl_r=new TableLayout(this,ItemsEntrys.get(i*5),ItemsEntrys.get(i*5+1),ItemsEntrys.get(i*5+2),ItemsEntrys.get(i*5+3),ItemsEntrys.get(i*5+4),requestQueue);
        return tl_r;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_searchdetailactivity, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(alreadyCollect)
            menu.findItem(R.id.menu_collection).setIcon(R.mipmap.icon_menu_collection_alreadycollect);
        return true;

    }

    @Override
    public void finish() {
        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        bundle.putBoolean("isDataChanged",isDataChange);
        intent.putExtras(bundle);
        setResult(START_FOR_RESULT_CODE,intent);
        super.finish();
    }
}
