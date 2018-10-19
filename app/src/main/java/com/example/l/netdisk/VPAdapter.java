package com.example.l.netdisk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class VPAdapter extends PagerAdapter {

    //private List<RecyclerView> recyclerViewList;

    /*public VPAdapter(List<RecyclerView> recyclerViewList){

        this.recyclerViewList = recyclerViewList;
    }*/
    private View currentView;

    @Override
    public int getCount() {
        return MainActivity.weekday.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.monrecyc,null,false);
        RecyclerView.LayoutManager  layoutManager = new LinearLayoutManager(container.getContext());
        RecyclerView recyclerView = (RecyclerView)view ;
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<Bangumi> bangumis = (ArrayList<Bangumi>) LitePal.where("week=?",MainActivity.weekday[position]).find(Bangumi.class);
        recyclerView.setAdapter(new homeAdapter(bangumis));
        //container.addView(recyclerViewList.get(position));
        container.addView(view);
        //return recyclerViewList.get(position);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //container.removeView(recyclerViewList.get(position));
        container.removeView((View)object);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return MainActivity.weekday[position];
    }


}
