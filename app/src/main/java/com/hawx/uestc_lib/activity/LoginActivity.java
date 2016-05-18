package com.hawx.uestc_lib.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.hawx.uestc_lib.R;
import com.hawx.uestc_lib.base.BaseActivity;
import com.hawx.uestc_lib.widget.WaitingDialog;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/12.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private final String USER_DATA="USER_DATA";
    private final String FROM_ACTIVITY="FROM_ACTIVITY";
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences1;
    private SharedPreferences.Editor editor1;
    private boolean loading=false;
    @BindView(R.id.activity_login_uername)
    EditText userName;
    @BindView(R.id.activity_login_password)
    EditText passWord;
    @BindView(R.id.activity_login_checkbox1)
    CheckBox remember;
    @BindView(R.id.activity_login_checkbox2)
    CheckBox autoLogin;
    @BindView(R.id.activity_login_button_log)
    Button login;
    @BindView(R.id.activity_login_button_back)
    Button back;
    @BindView(R.id.activity_login_waitingdialog)
    WaitingDialog waitingDialog;
    @BindView(R.id.activity_login_gridlayout)
    GridLayout gridLayout;
    private Handler mhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:{
                    String user_data=msg.getData().getString("user_data");
                    startUserCenterActivity(LoginActivity.this,user_data);
                    finish();
                    loading=false;
                    break;

                }
                case 2:{
                    toast("连接超时，请检查网络设置");
                    loading=false;
                    gridLayout.setVisibility(View.VISIBLE);
                    waitingDialog.setVisibility(View.INVISIBLE);
                    break;
                }
                case 3:{
                    toast("帐号信息有误");
                    loading=false;
                    gridLayout.setVisibility(View.VISIBLE);
                    waitingDialog.setVisibility(View.INVISIBLE);
                    break;
                }
                default:
                    break;
            }
        }
    };

    private void startUserCenterActivity(Context context, String user_data) {
        Bundle bundle=new Bundle();
        bundle.putString(USER_DATA,user_data);
        bundle.putString(FROM_ACTIVITY,"LoginActivity");
        Intent intent=new Intent(context,UserCenterActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNeedLeftMenu(false);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        sharedPreferences=getSharedPreferences("music",0);
        editor=sharedPreferences.edit();
        sharedPreferences1=getSharedPreferences("book",0);
        editor1=sharedPreferences1.edit();
        initToolBar();
        final String bookName=sharedPreferences1.getString("bookName","nodata");
        final String bookNum=sharedPreferences1.getString("bookNum","nodata");
        if(bookName.equals("nodata")) {
            initView();
        }else{
            loading=true;
            gridLayout.setVisibility(View.INVISIBLE);
            waitingDialog.setVisibility(View.VISIBLE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpsURLConnection con2 = null;
                    try {
                        URL url = new URL(bookName);
                        con2 = (HttpsURLConnection) url.openConnection();
                        con2.setRequestMethod("GET");
                        con2.setRequestProperty("Cookie", bookNum);
                        con2.setConnectTimeout(8000);
                        con2.setReadTimeout(8000);
                        InputStream in = con2.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder builder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                        String response = builder.toString();
                        Message msg = new Message();
                        msg.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("user_data", response);
                        msg.setData(bundle);
                        mhandler.sendMessage(msg);
                    } catch (SocketTimeoutException e) {
                        mhandler.sendEmptyMessage(2);
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private void initView() {
        String musicName=sharedPreferences.getString("musicName","nodata");
        String musicNum=sharedPreferences.getString("musicNum","nodata");
        if(!musicName.equals("nodata")&&!musicNum.equals("nodata")){
            userName.setText(musicName);
            passWord.setText(musicNum);
            remember.setChecked(true);
        }
        back.setOnClickListener(this);
        login.setOnClickListener(this);
        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked) {
                    userName.setText("");
                    passWord.setText("");
                    editor.clear();
                    editor.commit();
                }
            }
        });
        autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    editor1.clear();
                    editor1.commit();
                }
            }
        });
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.base_toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setSubtitle("登录");
        toolbar.setSubtitleTextAppearance(this,R.style.SubTitleText);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_login_button_back:{
                finish();
                break;
            }
            case R.id.activity_login_button_log:{
                String user_id=userName.getText().toString();
                String user_pw=passWord.getText().toString();
                if(user_id.equals("")|user_pw.equals("")){
                    toast("请输入帐号信息");
                }else {
                    if(loading){
                        return;
                    }
                    gridLayout.setVisibility(View.INVISIBLE);
                    waitingDialog.setVisibility(View.VISIBLE);
                    loading=true;
                    if(remember.isChecked()){
                        editor.clear();
                        editor.putString("musicName",user_id);
                        editor.putString("musicNum",user_pw);
                        editor.commit();
                    }
                    final String url = "https://webpac.uestc.edu.cn/patroninfo*chx?extpatid=" + user_id + "&extpatpw=" + user_pw + "&submit=submit";
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HttpsURLConnection con1 = null;
                            HttpsURLConnection con = null;
                            try {
                                URL url_URL = new URL(url);
                                con1 = (HttpsURLConnection) url_URL.openConnection();
                                con1.setInstanceFollowRedirects(false);
                                con1.setRequestMethod("GET");
                                con1.setConnectTimeout(8000);
                                con1.setReadTimeout(8000);
                                String COOKIES_HEADER = "Set-Cookie";
                                CookieManager msCookieManager = new CookieManager();
                                Map<String, List<String>> headerFields = con1.getHeaderFields();
                                List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);
                                if (cookiesHeader != null) {
                                    for (String cookie : cookiesHeader) {
                                        log(cookie);
                                        msCookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                                    }
                                }
                                String header_location = con1.getHeaderField("Location");
                                if (header_location != null) {
                                    String url_detail = "https://webpac.uestc.edu.cn" + header_location;
                                    //===================================================================================
                                    URL url_URL_detail = new URL(url_detail);
                                    con = (HttpsURLConnection) url_URL_detail.openConnection();
                                    con.setRequestMethod("GET");
                                    if (msCookieManager.getCookieStore().getCookies().size() > 0) {
                                        con.setRequestProperty("Cookie", TextUtils.join(";", msCookieManager.getCookieStore().getCookies()));
                                    }
                                    if(autoLogin.isChecked()){
                                        editor1.clear();
                                        editor1.putString("bookName",url_detail);
                                        editor1.putString("bookNum",TextUtils.join(";", msCookieManager.getCookieStore().getCookies()));
                                        editor1.commit();
                                    }
                                    con.setConnectTimeout(8000);
                                    con.setReadTimeout(8000);
                                    InputStream in = con.getInputStream();
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                                    StringBuilder builder = new StringBuilder();
                                    String line;
                                    while ((line = reader.readLine()) != null) {
                                        builder.append(line);
                                    }
                                    String response = builder.toString();
                                    Message msg = new Message();
                                    msg.what = 1;
                                    Bundle bundle = new Bundle();
                                    bundle.putString("user_data", response);
                                    msg.setData(bundle);
                                    mhandler.sendMessage(msg);
                                } else {
                                    mhandler.sendEmptyMessage(3);
                                }

                            } catch (SocketTimeoutException e) {
                                mhandler.sendEmptyMessage(2);
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                break;
            }
            default:
                break;
        }
    }


}
