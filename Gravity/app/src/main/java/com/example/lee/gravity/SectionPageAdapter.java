package com.example.lee.gravity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

/**
 * Created by lee on 2015-12-24.
 */
public class SectionPageAdapter extends FragmentPagerAdapter {

    Context mContext;
    public SectionPageAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {

        if(position == 0)
        {
            return new Fragement1();
        }
        else
        {
            return new Fragement2();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
