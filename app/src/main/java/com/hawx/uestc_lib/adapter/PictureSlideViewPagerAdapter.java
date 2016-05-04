package com.hawx.uestc_lib.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hawx.uestc_lib.fragment.PictureSlideFragment;

/**
 * Created by Administrator on 2016/5/3.
 */
public class PictureSlideViewPagerAdapter extends FragmentStatePagerAdapter {
    public PictureSlideViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return PictureSlideFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return 5;
    }
}
