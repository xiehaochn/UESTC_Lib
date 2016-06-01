package com.hawx.uestc_lib.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.hawx.uestc_lib.R;
import com.hawx.uestc_lib.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/6/1.
 */

public class WebViewActivity extends BaseActivity {
    @BindView(R.id.activity_webview_wb)
    WebView webView;
    @BindView(R.id.base_toolbar)
    Toolbar toolbar;
    @BindView(R.id.activity_webview_progressbar)
    ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNeedLeftMenu(false);
        setContentView(R.layout.activity_webview);
        setNeedSlideFinish(false);
        ButterKnife.bind(this);
        initToolBar();
        initWebView();
    }

    private void initWebView() {
        final String URL=getIntent().getStringExtra("URL");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress==100)
                    progressBar.setVisibility(View.INVISIBLE);

            }
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                String title_r=title;
                title_r=getIntent().getStringExtra("TITLE");
                toolbar.setTitle(title_r);
                toolbar.setTitleTextAppearance(WebViewActivity.this,R.style.SmallTitleText);
            }
        });
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(URL);
    }

    private void initToolBar() {
        toolbar= (Toolbar) findViewById(R.id.base_toolbar);
        toolbar.setTitle("加载中");
        toolbar.setNavigationIcon(R.mipmap.icon_navigation_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
