package com.example.l.netdisk;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout layout ;
    //static RecyclerView recyclerView;
    //static homeAdapter adapter;
    //static String WEEK = "Mon";
    static final String[] weekday = new String[]{"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
    static ViewPager viewPager;
    //static VPAdapter vpAdapter;
    static int position;
    static protected SwipeRefreshLayout refreshLayout;
    static DLService.DLBinder dlBinder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            dlBinder = (DLService.DLBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    /*private Fresh fresh = new Fresh() {
        @Override
        public void fresh(String week) {
            /*RecyclerView.LayoutManager  layoutManager = new LinearLayoutManager(MainActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            ArrayList<Bangumi> bangumis = (ArrayList<Bangumi>) LitePal.where("week=?",week).find(Bangumi.class);
            adapter = new homeAdapter(bangumis);
            recyclerView.setAdapter(adapter);

            RecyclerView.LayoutManager  layoutManager = new LinearLayoutManager(MainActivity.this);
            getDayView(week).setLayoutManager(layoutManager);
            ArrayList<Bangumi> bangumis = (ArrayList<Bangumi>) LitePal.where("week=?",week).find(Bangumi.class);
            adapter = new homeAdapter(bangumis);
            getDayView(week).setAdapter(adapter);

        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //recyclerView = getRecyclerview(WEEK,getView(WEEK));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        layout = findViewById(R.id.drawerlayout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        refreshLayout = findViewById(R.id.home_refresh);
        Intent intent = new Intent(MainActivity.this,DLService.class);
        startService(intent);
        bindService(intent,connection,BIND_AUTO_CREATE);


        viewPager = findViewById(R.id.dayList);
        viewPager.setAdapter(new VPAdapter());
        TabLayout tabLayout = findViewById(R.id.home_tablayout);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                MainActivity.position = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        refreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FreshBangumiList().execute();
            }
        });

        ActionBar bar = getSupportActionBar();
        if (bar!=null){
            bar.setDisplayHomeAsUpEnabled(true);
            //bar.setHomeAsUpIndicator(R.mipmap.timg);
        }

        //fresh.fresh(WEEK);



        navigationView.setCheckedItem(R.id.search_activity);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //WEEK = item.getTitle().toString();
                //fresh.fresh(WEEK);
                switch (item.getItemId()){
                    case R.id.search_activity:
                        Intent intent = new Intent(MainActivity.this,VedioDLUrlActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.filelist_activity:
                        Intent intent1 = new Intent(MainActivity.this,ServerFileListActivity.class);
                        startActivity(intent1);
                        break;
                    default:
                }
                layout.closeDrawers();
                return  true;
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"popipa",Snackbar.LENGTH_SHORT)
                        .setAction("liveGO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this,"holloworld",Toast.LENGTH_SHORT).show();
                            }
                        }).show();
                Intent intent = new Intent(MainActivity.this,BangumiDetail.class);
                startActivity(intent);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            layout.openDrawer(GravityCompat.START);
        }
        return true;
    }





}
