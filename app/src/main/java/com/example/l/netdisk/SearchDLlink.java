package com.example.l.netdisk;

import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Adapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class SearchDLlink extends AsyncTask<String,List<SearchResult>,Void> {
    private ArrayList<SearchResult> results = new ArrayList<SearchResult>();

    @Override
    protected Void doInBackground(String... strings) {
        String str = strings[1];

        //Log.d("jfjfjjjjjj",str);
        String Url;
        if (str.equals(VedioDLUrlActivity.SEARCH) ){
            Url = "https://share.dmhy.org/topics/list?keyword="+strings[0];
        }else if (str.equals(VedioDLUrlActivity.NOSEARCH)){
            Url = strings[0];
        }else {
            SearchResult result = new SearchResult();
            result.setTitle("NO Information");
            results.add(result);
            publishProgress(results);
            return null;
        }

        //Log.d("jfjfjjjjjj",Url);
        try {
            Document searchHtml = Jsoup.connect(Url).get();
            Elements tbody = searchHtml.select("tbody").select("tr");

            if (tbody.size()==0){
                SearchResult result = new SearchResult();
                result.setTitle("没有可显示资源");
                results.add(result);
                publishProgress(results);
                return null;
            }

            for (Element element:tbody){
                SearchResult result = new SearchResult();
                result.setTitle(element.select("td.title").text());
                result.setLink(element.select("a.download-arrow").attr("href"));
                results.add(result);
            }

            publishProgress(results);

        }catch (Exception e){
            e.printStackTrace();
            Log.d("jfjffffjjj","exception");
        }
        return null;

    }

    @Override
    protected void onProgressUpdate(List<SearchResult>... values) {
        ArrayList<SearchResult> searchResults = (ArrayList<SearchResult>) values[0];
        SearchResultAdapter adapter = new SearchResultAdapter(searchResults);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(VedioDLUrlActivity.recyclerView.getContext());
        VedioDLUrlActivity.recyclerView.setLayoutManager(layoutManager);
        VedioDLUrlActivity.recyclerView.setAdapter(adapter);

    }
}
