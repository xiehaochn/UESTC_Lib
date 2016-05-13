package com.hawx.uestc_lib.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hawx.uestc_lib.R;
import com.hawx.uestc_lib.base.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/12.
 */
public class CollectionActivity extends BaseActivity {
    private final String RESULT_URL="RESULT_URL_KEY";
    private final String RESULT_TITLE="RESULT_TITLE_KEY";
    private final int START_FOR_RESULT_CODE=1;
    private ArrayList<String> collection_title =new ArrayList<String>();
    private ArrayList<String> collection_url =new ArrayList<String>();
    private SharedPreferences sharedPreferences;
    private JSONArray collection=new JSONArray();
    private ArrayAdapter<String> arrayAdapter;

    @BindView(R.id.activity_collection_linearlayout)
    LinearLayout linearLayout;
    @BindView(R.id.activity_collection_pretextview)
    TextView textView;
    @BindView(R.id.activity_collection_listview)
    ListView listView;
    @BindView(R.id.base_toolbar)
    Toolbar toolbar;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTag("我的收藏");
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);
        try {
            initData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        init();
    }

    private void init() {
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_delete:{
                        AlertDialog.Builder builder=new AlertDialog.Builder(CollectionActivity.this);
                        builder.setTitle("清空收藏列表")
                               .setMessage("是否确认清空收藏列表，清空后无法恢复！")
                               .setCancelable(false)
                               .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                       editor.clear();
                                       editor.apply();
                                       collection_title.clear();
                                       collection_url.clear();
                                       arrayAdapter.notifyDataSetChanged();
                                       linearLayout.setVisibility(View.INVISIBLE);
                                       textView.setVisibility(View.VISIBLE);
                                   }
                               })
                               .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                   }
                               });
                        builder.create().show();
                        break;
                    }
                    default:
                        break;
                }
                return true;
            }
        });
        arrayAdapter=new ArrayAdapter<String>(this,R.layout.listview_item_searchresult,collection_title);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startResultActivity(CollectionActivity.this,collection_url.get(position),collection_title.get(position));
            }
        });
    }

    private void initData() throws JSONException {
        sharedPreferences=getSharedPreferences("collection",0);
        String data=sharedPreferences.getString("data","nodata");
        editor=sharedPreferences.edit();
        if(data.equals("nodata")){
            editor.putString("data",collection.toString());
            editor.apply();
            linearLayout.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);
        }else {
            collection = new JSONArray(data);
            if(collection.length()==0){
                linearLayout.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.VISIBLE);
            }else {
                for (int i = 0; i < collection.length(); i++) {
                    JSONObject jsonObject = (JSONObject) collection.get(i);
                    String title_contain = jsonObject.getString("title");
                    String url_contain = jsonObject.getString("url");
                    collection_title.add(title_contain);
                    collection_url.add(url_contain);
                }
            }
        }
    }
    private void startResultActivity(Context context, String url, String title){
        Intent intent=new Intent(context,SearchDetailActivity.class);
        intent.putExtra(RESULT_URL,url);
        intent.putExtra(RESULT_TITLE,title);
        startActivityForResult(intent,START_FOR_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            Bundle bundle=data.getExtras();
            boolean changed=bundle.getBoolean("isDataChanged");
            if(changed){
                collection_title.clear();
                collection_url.clear();
                try {
                    initData();
                    arrayAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_collectionactivity,menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
