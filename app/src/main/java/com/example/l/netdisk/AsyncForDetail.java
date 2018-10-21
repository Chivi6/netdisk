package com.example.l.netdisk;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AsyncForDetail extends AsyncTask<String,Integer,Integer> {
    final int detailOK = 1;
    final int castOK = 2;
    final Staff bgmDetail = new Staff();
    ArrayList<Cast> casts = new ArrayList<Cast>();
    @Override
    protected Integer doInBackground(String... url) {



        try {
            Log.d("jfjf",url[0]);
            Document bangumiDetail = Jsoup.connect("http://bangumi.tv" + url[0]).get();
            Elements jianJieE = bangumiDetail.select("div.subject_summary");
            if (jianJieE.size()>0)
                bgmDetail.setJianjie(jianJieE.get(0).text());
            else
                bgmDetail.setJianjie("No Information");
            //Log.d("jfjfjfjfjf",bgmDetail.getJianjie());

            File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath()+"/Detail");
            DeleteDir(f);

            //Log.d("jfjf",""+f.list().length);


            if (bangumiDetail.select("ul.browserMedium").select("li").size()>0){

            for (Element element:bangumiDetail.select("ul.browserMedium").select("li")){
                String castName = element.select("span.tip_j").text();
                String picUrlTemp = element.select("span.avatarNeue").attr("style");
                String picUrl ;
                if (picUrlTemp.indexOf("lain")!=-1){
                    picUrl = picUrlTemp.substring(picUrlTemp.indexOf("lain"),picUrlTemp.lastIndexOf("\'"));
                }else {
                    picUrl = "";
                }
                String castPicUrl;
                if (picUrl.equals("")){
                    castPicUrl = "NoPic";
                }else {
                    castPicUrl = "http://"+picUrl;
                }
                Cast cast = new Cast();
                cast.setCastName(castName);
                cast.setCastPicUrl(castPicUrl);
                casts.add(cast);

            }
            }else {
                Cast cast = new Cast();
                cast.setCastName("NO Information");
                cast.setCastPicUrl("NoPic");
                casts.add(cast);
            }



            //封面下载
            String coverUrl = "http:"+bangumiDetail.select("a.thickbox").get(0).attr("href");
            final String imgName = coverUrl.substring(coverUrl.lastIndexOf("/")+1);
            PicDL(coverUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath()+"/Detail";
                    File file = new File(filePath);
                    file.mkdirs();
                    FileOutputStream fileOutputStream = new FileOutputStream(filePath+"/"+imgName);
                    InputStream is = response.body().byteStream();
                    int len;
                    byte[] b = new byte[1024];
                    while ((len = is.read(b))!=-1){
                        fileOutputStream.write(b,0,len);
                    }
                    fileOutputStream.close();
                    response.close();
                    is.close();
                    bgmDetail.setCoverPicPath(filePath+"/"+imgName);
                    publishProgress(detailOK);
                }
            });


            //cast图片下载，图片本地路径配置
            for (Cast cast:casts){
                String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath()+"/Detail";
                File file = new File(filePath);
                file.mkdirs();
                try {
                    final String picPath = filePath + "/" + cast.getCastName() + ".jpg";
                    if (cast.getCastPicUrl().equals("NoPic")){
                        cast.setCastPicPath(picPath);
                        publishProgress(castOK);
                    }else {
                        //Log.d("jfjf",cast.getCastPicUrl());
                        PicDL(cast.getCastPicUrl(), new Callback() {
                            FileOutputStream fileOutputStream = new FileOutputStream(picPath);
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                InputStream is = response.body().byteStream();
                                int len;
                                byte[] b = new byte[1024];
                                while ((len = is.read(b))!=-1){
                                    fileOutputStream.write(b,0,len);
                                }
                                fileOutputStream.close();
                                response.close();
                                is.close();
                                publishProgress(castOK);
                            }
                        });

                        cast.setCastPicPath(picPath);

                    }
                }catch (Exception fnf){
                    fnf.printStackTrace();
                }
            }
            bgmDetail.setCasts(casts);
        }catch (Exception e){
            e.printStackTrace();Log.d("jfjf","exception");
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        switch (values[0]){
            case detailOK:
                Glide.with(BangumiDetail.cover.getContext()).load(bgmDetail.getCoverPicPath()).into(BangumiDetail.cover);
                TextView textView = BangumiDetail.views.get(0).findViewById(R.id.detail);
                textView.setText(bgmDetail.getJianjie());
                break;
            case castOK:
                //Log.d("jfjfjfjfffff",casts.get(0).getCastName());
                RecyclerView recyclerView = (RecyclerView) BangumiDetail.views.get(1).findViewById(R.id.cast_recyc);
                RecyclerView.LayoutManager manager = new GridLayoutManager(recyclerView.getContext(),2);
                StaffRecycAdapter adapter = new StaffRecycAdapter(casts);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);
                break;
                default:
        }
    }

    private void PicDL(String url, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }

    private void DeleteDir(File dir){
        if (!dir.exists())
            return;

        if (dir.isDirectory()){
            String[] filenames = dir.list();
            for (String filename:filenames){
                File file = new File(dir.getPath(),filename);
                if (file.isDirectory()){
                    DeleteDir(file);
                    file.delete();
                }else {
                    file.delete();
                }
            }
        }
    }
}
