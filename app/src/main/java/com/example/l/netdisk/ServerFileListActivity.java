package com.example.l.netdisk;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toolbar;

import java.util.ArrayList;

public class ServerFileListActivity extends AppCompatActivity {
    static ArrayList<View> views = new ArrayList<>();
    static String parentpath;
    static String SERVER_IP = "172.16.12.200";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_file_list);
        TabLayout tabLayout = findViewById(R.id.filelist_tab);
        ViewPager viewPager = findViewById(R.id.server_viewpager);
        FloatingActionButton fab = findViewById(R.id.filelist_fab);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.filelist_toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        View view = View.inflate(ServerFileListActivity.this,R.layout.filelist_recyc,null);
        View view1 = View.inflate(ServerFileListActivity.this,R.layout.filelist_recyc,null);
        View view2 = View.inflate(ServerFileListActivity.this,R.layout.filelist_recyc,null);
        views.add(view);
        views.add(view1);
        views.add(view2);
        ServerFileListVPAdapter vpAdapter = new ServerFileListVPAdapter(views);
        viewPager.setAdapter(vpAdapter);
        tabLayout.setupWithViewPager(viewPager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(ServerFileListActivity.this);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ServerFileListActivity.this);
                dialogBuilder.setTitle("输入下载连接")
                .setView(editText)
                .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new AddUri().execute(editText.getText().toString());
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialogBuilder.show();

            }
        });

        new Filelist().execute("homelist");
        new DLMsg().execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        views.removeAll(views);
        parentpath = null;
    }

    @Override
    public void onBackPressed() {
        String requst = parentpath+"splittoken"+"returnto";
        new Filelist().execute(requst);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }
}
