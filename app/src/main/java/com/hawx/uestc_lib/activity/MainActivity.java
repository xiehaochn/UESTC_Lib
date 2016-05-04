package com.hawx.uestc_lib.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.hawx.uestc_lib.R;
import com.hawx.uestc_lib.adapter.PictureSlideViewPagerAdapter;
import com.hawx.uestc_lib.base.BaseActivity;
import com.hawx.uestc_lib.widget.CustomViewPager;
import com.hawx.uestc_lib.widget.SlideFrameLayout;
import com.hawx.uestc_lib.widget.ViewPagerTabPoints;

/**
 * MainActivity
 * @author Hawx
 * @version 1.0
 */
public class MainActivity extends BaseActivity {
    private Toolbar toolbar;
    private Toolbar.OnMenuItemClickListener menuItemClickListener;
    private CustomViewPager viewPager;
    private ViewPagerTabPoints viewPagerTabPoints;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolBar();
        initViewPager();
    }

    private void initViewPager() {
        viewPager= (CustomViewPager) findViewById(R.id.activity_viewpager);
        viewPagerTabPoints= (ViewPagerTabPoints) findViewById(R.id.activity_viewpagertabpoints);
        viewPager.setListener(new CustomViewPager.onTouchEventDownListener() {
            @Override
            public void onTouchEventDown() {
                ViewGroup rootView= (ViewGroup) findViewById(android.R.id.content);
                SlideFrameLayout slideFrameView= (SlideFrameLayout) rootView.getChildAt(0);
                slideFrameView.setDoNotIntercept(true);
            }
        });
        viewPager.setListenerUp(new CustomViewPager.onTouchEventUpListener() {
            @Override
            public void onTouchEventUp() {
                ViewGroup rootView= (ViewGroup) findViewById(android.R.id.content);
                SlideFrameLayout slideFrameView= (SlideFrameLayout) rootView.getChildAt(0);
                slideFrameView.setDoNotIntercept(false);
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
                    case R.id.menu_setting:{
                        toast("Setting");
                        break;
                    }
                    case R.id.menu_share:{
                        toast("Share");
                        break;
                    }
                    case R.id.menu_exit:{
                        toast("Exit");
                        finishAll();
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
}
