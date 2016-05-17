package com.hawx.uestc_lib.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.hawx.uestc_lib.R;
import com.hawx.uestc_lib.base.BaseActivity;

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
    private Handler mhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:{
                    String user_data=msg.getData().getString("user_data");
                    startUserCenterActivity(LoginActivity.this,user_data);
                    break;
                }
                case 2:{
                    toast("连接超时，请检查网络设置");
                    break;
                }
                case 3:{
                    toast("帐号信息有误");
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
        initToolBar();
        initView();
    }

    private void initView() {
        back.setOnClickListener(this);
        login.setOnClickListener(this);

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
                    saveUserData();
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
                                        con.setRequestProperty("Cookie",
                                                TextUtils.join(";", msCookieManager.getCookieStore().getCookies()));
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
                                // TODO Auto-generated catch block
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

    private void saveUserData() {
        if(remember.isChecked()){
            //to be continued...
        }
    }
}
