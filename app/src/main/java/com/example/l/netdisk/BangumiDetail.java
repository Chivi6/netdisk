package com.example.l.netdisk;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toolbar;

import java.util.ArrayList;

public class BangumiDetail extends AppCompatActivity {

    protected static CollapsingToolbarLayout collapsingToolbarLayout;
    protected static ImageView cover;
    protected static ViewPager viewPager;
    protected static ArrayList<View> views = new ArrayList<View>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bangumi_detail);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.bgm_title);
        TabLayout tabLayout = findViewById(R.id.detail_tab);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        viewPager = findViewById(R.id.detail_vp);
        cover = findViewById(R.id.bgm_cover);
        FloatingActionButton fab = findViewById(R.id.freshFAB);
        Intent intent = getIntent();
        final String title = intent.getStringExtra("Title");
        collapsingToolbarLayout.setTitle(title);
        final String detailUrl = intent.getStringExtra("Url");

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        tabLayout.setupWithViewPager(viewPager);
        View view2 = View.inflate(BangumiDetail.this,R.layout.staff,null);
        View view1 = View.inflate(BangumiDetail.this,R.layout.detail,null);

        Button button = view1.findViewById(R.id.DLUrl);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BangumiDetail.this,VedioDLUrlActivity.class);
                intent.putExtra("NS",title.substring(0,title.indexOf(" ")));
                startActivity(intent);
            }
        });
        views.add(view1);
        views.add(view2);
        viewPager.setAdapter(new DetailVPAdapter(views));

        new AsyncForDetail().execute(detailUrl);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncForDetail().execute(detailUrl);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home);
        finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        views.removeAll(views);
    }
}
