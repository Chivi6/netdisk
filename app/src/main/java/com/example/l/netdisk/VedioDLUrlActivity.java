package com.example.l.netdisk;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class VedioDLUrlActivity extends AppCompatActivity {
    public static final String SEARCH = "search";
    public static final String NOSEARCH = "nosearch";
    static RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedio_dlurl);
        recyclerView = findViewById(R.id.DLurl_recyc);
        Toolbar toolbar = findViewById(R.id.DLtoolbar);
        Button search = findViewById(R.id.search_bangumi);
        final EditText editText = findViewById(R.id.search_text);
        Intent intent = getIntent();

        setSupportActionBar(toolbar);
        toolbar.setTitle("搜索结果");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchContent = editText.getText().toString();
                Log.d("jfjfjf",searchContent);
                new SearchDLlink().execute(new String[]{searchContent,SEARCH});
            }
        });

        editText.setText(intent.getStringExtra("NS"));

        new SearchDLlink().execute(new String[]{"https://share.dmhy.org/topics/list?keyword="+intent.getStringExtra("NS"),NOSEARCH});
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }
}
