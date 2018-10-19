package com.example.l.netdisk;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class DetailVPAdapter extends PagerAdapter {
    List<View> views;
    String[] pageTitle = new String[]{"简介","CAST"};

    public DetailVPAdapter(List<View> views){
        this.views = views;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(views.get(position));
        return views.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(views.get(position));
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitle[position];
    }
}
