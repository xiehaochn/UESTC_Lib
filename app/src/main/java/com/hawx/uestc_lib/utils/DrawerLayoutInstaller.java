package com.hawx.uestc_lib.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * 抽屉导航注入辅助类
 * @author frogermcs
 * @version 1.0
 */
public class DrawerLayoutInstaller {
    private static final int DEFAULT_LEFT_DRAWER_WIDTH_DP = 240;
    public static DrawerBuilder from(Activity activity){
        return new DrawerBuilder(activity);
    }
    public static class DrawerBuilder {
        private Activity activity;
        private int drawerRootResId;
        private Toolbar toolbar;
        private View drawerLeftView;
        private int drawerLeftWidth;
        public DrawerBuilder(Activity activity) {
            this.activity = activity;
        }
        public DrawerBuilder drawerRoot(int drawerRootResId) {
            this.drawerRootResId = drawerRootResId;
            return this;
        }
        public DrawerBuilder withNavigationIconToggler(Toolbar toolbar) {
            this.toolbar = toolbar;
            return this;
        }

        public DrawerBuilder drawerLeftView(View drawerLeftView) {
            this.drawerLeftView = drawerLeftView;
            return this;
        }
        public DrawerBuilder drawerLeftWidth(int width) {
            this.drawerLeftWidth = width;
            return this;
        }
        public DrawerLayout build(int version) {
            DrawerLayout drawerLayout = createDrawerLayout();
            addDrawerToActivity(drawerLayout,version);
            setupToggler(drawerLayout);
            setupDrawerLeftView(drawerLayout);
            return drawerLayout;
        }
        private DrawerLayout createDrawerLayout() {
            if (drawerRootResId != 0) {
                return (DrawerLayout) LayoutInflater.from(activity).inflate(drawerRootResId, null);
            } else {
                DrawerLayout drawerLayout = new DrawerLayout(activity);

                FrameLayout contentView = new FrameLayout(activity);
                drawerLayout.addView(contentView, new DrawerLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                ));

                FrameLayout leftDrawer = new FrameLayout(activity);

                int drawerWidth = drawerLeftWidth != 0 ? drawerLeftWidth : DEFAULT_LEFT_DRAWER_WIDTH_DP;

                final ViewGroup.LayoutParams leftDrawerParams = new DrawerLayout.LayoutParams(
                        (int) (drawerWidth * Resources.getSystem().getDisplayMetrics().density),
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        Gravity.START
                );
                drawerLayout.addView(leftDrawer, leftDrawerParams);
                return drawerLayout;
            }
        }
        private void addDrawerToActivity(DrawerLayout drawerLayout,int version) {
            if(version>= Build.VERSION_CODES.LOLLIPOP) {
                ViewGroup rootView = (ViewGroup) activity.findViewById(android.R.id.content);
                ViewGroup drawerContentRoot = (ViewGroup) drawerLayout.getChildAt(0);
                View contentView = rootView.getChildAt(0);

                rootView.removeView(contentView);

                drawerContentRoot.addView(contentView, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                ));

                rootView.addView(drawerLayout, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                ));
            }else{
                ViewGroup rootView = (ViewGroup) activity.findViewById(android.R.id.content);
                ViewGroup slideFrameView= (ViewGroup) rootView.getChildAt(0);
                ViewGroup linearLayoutView= (ViewGroup) slideFrameView.getChildAt(0);
                ViewGroup drawerContentRoot = (ViewGroup) drawerLayout.getChildAt(0);
                View contentView =linearLayoutView.getChildAt(1);
                linearLayoutView.removeView(contentView);
                drawerContentRoot.addView(contentView, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                ));

                linearLayoutView.addView(drawerLayout, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                ));
            }
        }
        private void setupToggler(final DrawerLayout drawerLayout) {
            if (toolbar != null) {
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                            drawerLayout.closeDrawer(GravityCompat.START);
                        } else {
                            drawerLayout.openDrawer(GravityCompat.START);
                        }
                    }
                });
            }
        }

        private void setupDrawerLeftView(DrawerLayout drawerLayout) {
            if (drawerLeftView != null) {
                DrawerLayout.LayoutParams layoutParams=new DrawerLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
                ((ViewGroup) drawerLayout.getChildAt(1))
                        .addView(drawerLeftView,layoutParams);
            }
        }
    }
}
