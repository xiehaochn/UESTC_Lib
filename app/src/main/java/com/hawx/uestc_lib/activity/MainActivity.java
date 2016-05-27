package com.hawx.uestc_lib.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hawx.uestc_lib.R;
import com.hawx.uestc_lib.adapter.PictureSlideViewPagerAdapter;
import com.hawx.uestc_lib.adapter.RecommendAdapter;
import com.hawx.uestc_lib.base.BaseActivity;
import com.hawx.uestc_lib.data.ResponseData;
import com.hawx.uestc_lib.utils.Utils;
import com.hawx.uestc_lib.widget.CustomViewPager;
import com.hawx.uestc_lib.widget.SelectDialog;
import com.hawx.uestc_lib.widget.SlideFrameLayout;
import com.hawx.uestc_lib.widget.ViewPagerTabPoints;
import com.hawx.uestc_lib.widget.WaitingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * MainActivity
 * @author Hawx
 * @version 1.0
 */
public class MainActivity extends BaseActivity implements View.OnClickListener{
    private Toolbar toolbar;
    private Toolbar.OnMenuItemClickListener menuItemClickListener;
    private CustomViewPager viewPager;
    private ViewPagerTabPoints viewPagerTabPoints;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private RequestQueue requestQueue;
    private boolean isLoading=false;
    private static final String URL ="http://apis.juhe.cn/goodbook/query";
    public static final String APPKEY ="2f0af81454a23554f12f800d259e5e60";
    private RecommendAdapter adapter;
    @BindView(R.id.activity_main_textbutton_more)
    TextView text_button_more;
    @BindView(R.id.activity_main_gridlayout)
    GridLayout gridLayout;
    @BindView(R.id.activity_main_button_242)
    Button button_242;
    @BindView(R.id.activity_main_button_243)
    Button button_243;
    @BindView(R.id.activity_main_button_244)
    Button button_244;
    @BindView(R.id.activity_main_button_248)
    Button button_248;
    @BindView(R.id.activity_main_button_252)
    Button button_252;
    @BindView(R.id.activity_main_button_257)
    Button button_257;
    @BindView(R.id.activity_main_waitingdialog)
    WaitingDialog waitingDialog;
    @BindView(R.id.activity_main_recyclerview)
    RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTag("主页");
        setContentView(R.layout.activity_main);
        setNeedSlideFinish(false);
        ButterKnife.bind(this);
        initToolBar();
        initViewPager();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }else{
            initData();
        }
        text_button_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SelectDialog selectDialog=new SelectDialog(MainActivity.this,R.style.SelectDialog);
                selectDialog.setCancelable(true);
                Window dialogWindow=selectDialog.getWindow();
                WindowManager.LayoutParams params=dialogWindow.getAttributes();
                dialogWindow.setGravity(Gravity.TOP);
                int[] location=new int[2];
                gridLayout.getLocationOnScreen(location);
                params.y=location[1]-Utils.getStatusBarHeight(MainActivity.this);
                dialogWindow.setAttributes(params);
                dialogWindow.setWindowAnimations(R.style.SelectDialogAnim);
                selectDialog.show();
                selectDialog.setListener(new SelectDialog.dialogSelectedListener() {
                    @Override
                    public void dialogSelected() {
                        if(!isLoading){
                            gridLayoutAnimation(selectDialog.getCatalog_ID(),selectDialog.getCatalog());
                        }
                    }
                });
            }
        });
        initRecyclerView();
    }

    private void initRecyclerView() {
        adapter=new RecommendAdapter(this);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void getPermission(final String permission) {
        int alreadyGetPermission=checkSelfPermission(permission);
        if(alreadyGetPermission!=PackageManager.PERMISSION_GRANTED){
            if(shouldShowRequestPermissionRationale(permission)) {
                requestPermissions(new String[]{permission}, REQUEST_CODE_ASK_PERMISSIONS);
            }else {
                AlertDialog.Builder builder=new AlertDialog.Builder(this,R.style.CustomDialog);
                builder.setMessage("需要以下权限：存储空间\n请手动开启！").setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        finishAll();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finishAll();
                    }
                }).create().show();
            }
        }else {
            initData();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE_ASK_PERMISSIONS:{
                    if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                        initData();
                    }else{
                        toast("PermissionDenied!");
                        finishAll();
                    }
                break;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    private void initData() {
        requestQueue=Volley.newRequestQueue(this);
        requestQueue.start();
    }

    private void initViewPager() {
        viewPager= (CustomViewPager) findViewById(R.id.activity_viewpager);
        viewPagerTabPoints= (ViewPagerTabPoints) findViewById(R.id.activity_viewpagertabpoints);
        viewPager.setListener(new CustomViewPager.onTouchEventDownListener() {
            @Override
            public void onTouchEventDown() {
                ViewGroup rootView= (ViewGroup) findViewById(android.R.id.content);
                SlideFrameLayout slideFrameView;
                if(Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP) {
                    slideFrameView = (SlideFrameLayout) rootView.getChildAt(0);
                    slideFrameView.setDoNotIntercept(true);
                }else{
                    DrawerLayout drawerLayout= (DrawerLayout) rootView.getChildAt(0);
                    FrameLayout drawerContentRoot = (FrameLayout) drawerLayout.getChildAt(0);
                    slideFrameView= (SlideFrameLayout) drawerContentRoot.getChildAt(0);
                    slideFrameView.setDoNotIntercept(true);
                }

            }
        });
        viewPager.setListenerUp(new CustomViewPager.onTouchEventUpListener() {
            @Override
            public void onTouchEventUp() {
                ViewGroup rootView= (ViewGroup) findViewById(android.R.id.content);
                SlideFrameLayout slideFrameView;
                if(Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP) {
                    slideFrameView = (SlideFrameLayout) rootView.getChildAt(0);
                    slideFrameView.setDoNotIntercept(false);
                }else{
                    DrawerLayout drawerLayout= (DrawerLayout) rootView.getChildAt(0);
                    FrameLayout drawerContentRoot = (FrameLayout) drawerLayout.getChildAt(0);
                    slideFrameView= (SlideFrameLayout) drawerContentRoot.getChildAt(0);
                    slideFrameView.setDoNotIntercept(false);
                }
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    viewPagerTabPoints.setCurrentPoint(2);
                }else if(position==4){
                    viewPagerTabPoints.setCurrentPoint(0);
                }else{
                    viewPagerTabPoints.setCurrentPoint(position-1);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state){
                    case ViewPager.SCROLL_STATE_IDLE:{
                        if(viewPager.getCurrentItem()==0){
                            viewPager.setCurrentItem(3,false);
                        }else if(viewPager.getCurrentItem()==4){
                            viewPager.setCurrentItem(1,false);
                        }
                        viewPager.setDragging(false);
                        break;
                    }
                    case ViewPager.SCROLL_STATE_DRAGGING:{
                    }
                    case ViewPager.SCROLL_STATE_SETTLING:{
                        viewPager.setDragging(true);
                        break;
                    }
                    default:
                        break;
                }
            }
        });
        FragmentManager manager=getSupportFragmentManager();
        PictureSlideViewPagerAdapter adapter=new PictureSlideViewPagerAdapter(manager);
        viewPagerTabPoints.setPointsNum(adapter.getCount()-2);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1,false);
    }

    private void initToolBar() {
        toolbar= (Toolbar) findViewById(R.id.base_toolbar);
        menuItemClickListener=new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_share:{
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "我在使用科大藏经阁进行馆藏查询，再也不怕找不到图书位置了~");
                        sendIntent.setType("text/plain");
                        startActivity(Intent.createChooser(sendIntent,"分享至："));
                        break;
                    }
                    case R.id.menu_exit:{
                        finish();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=new MenuInflater(this);
        menuInflater.inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @OnClick({R.id.activity_main_button_243,R.id.activity_main_button_257,R.id.activity_main_button_252,R.id.activity_main_button_248,
            R.id.activity_main_button_244,R.id.activity_main_button_242})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_main_button_242:{
                if(!isLoading){
                    gridLayoutAnimation(242,"中国文学");
                }
                break;
            }
            case R.id.activity_main_button_252:{
                if(!isLoading){
                    gridLayoutAnimation(252,"人物传记");
                }
                break;
            }
            case R.id.activity_main_button_244:{
                if(!isLoading){
                    gridLayoutAnimation(244,"儿童文学");
                }
                break;
            }
            case R.id.activity_main_button_248:{
                if(!isLoading){
                    gridLayoutAnimation(248,"历史");
                }
                break;
            }
            case R.id.activity_main_button_257:{
                if(!isLoading){
                    gridLayoutAnimation(257,"哲学");
                }
                break;
            }
            case R.id.activity_main_button_243:{
                if(!isLoading){
                    gridLayoutAnimation(243,"外国文学");
                }
                break;
            }
            default:
                break;
        }
    }
    private void sendRequest(final int catalog_ID, final String catalog){
        isLoading=true;
        waitingDialog.setVisibility(View.VISIBLE);
        final String requestBody="catalog_id="+catalog_ID+"&pn=&rn=15&dtype=&key="+APPKEY;
        String requestURL=URL+"?"+requestBody;
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, requestURL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                waitingDialog.setVisibility(View.INVISIBLE);
                isLoading = false;
                gridLayout.setAlpha(1f);
                ResponseData responseData=new ResponseData();
                try {
                    responseData=ResponseData.jsonToData(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(responseData.getResultcode().equals("200")) {
                    Intent intent = new Intent(MainActivity.this, BookListActivity.class);
                    intent.putExtra("catalog_id",catalog_ID);
                    intent.putExtra("catalog", catalog);
                    intent.putExtra("data", responseData.getResult().toString());
                    startActivity(intent);
                }else{
                    toast("网络连接错误");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                waitingDialog.setVisibility(View.INVISIBLE);
                isLoading=false;
                gridLayout.setAlpha(1f);
                toast("网络连接错误");
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void gridLayoutAnimation(final int catalog_ID, final String catalog) {
        ObjectAnimator gridLayoutAnimator=new ObjectAnimator().ofFloat(gridLayout,"alpha",1,0);
        gridLayoutAnimator.setInterpolator(new AccelerateInterpolator());
        gridLayoutAnimator.setDuration(500);
        gridLayoutAnimator.start();
        gridLayoutAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                sendRequest(catalog_ID,catalog);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
