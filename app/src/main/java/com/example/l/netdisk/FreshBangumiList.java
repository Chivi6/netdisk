package com.example.l.netdisk;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.litepal.LitePal;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FreshBangumiList extends AsyncTask<Void,Void,Integer> {
    final static int LOADING = 1;
    final static int FIALED = 2;
    final static int SUCCESSED = 3;


    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Integer doInBackground(Void... voids) {
        File img;

        try{
            Document bangumiHtml = Jsoup.connect("http://bangumi.tv/calendar").get();
            //String[] weekday = new String[]{"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
            String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
            int week = 0;

            //加载一周的每一天
            for (int i = 0; i<7;i++){
                LitePal.deleteAll(Bangumi.class,"week=?",MainActivity.weekday[i]);
                Elements dayList = bangumiHtml.select("li.week").select("dd."+MainActivity.weekday[week]);
                //Log.d("dbisexi","size"+i+"lll"+dayList.size());
                    Elements elements = dayList.select("li");
                //Log.d("dbisexi111",elements.text());

                //加载一天的每一个
                    for(Element element : elements){

                        //Log.d("dbisexi1112222222",element.text());
                    String picUrlTemp = element.attr("style");
                    String picUrl = picUrlTemp.substring(picUrlTemp.indexOf("lain"),picUrlTemp.lastIndexOf("\'"));
                    final String dir = filePath+"/"+MainActivity.weekday[week];
                    final String fileName = picUrlTemp.substring(picUrlTemp.lastIndexOf("/"),picUrlTemp.lastIndexOf("\'"));
                    final String bangumiName = element.text();
                    final String weekd = MainActivity.weekday[week];
                    final String detailUrl = element.select("a").get(0).attr("href");
                    img  = new File(dir);

                    Log.d("dbisexi",dir+fileName);
                    img.mkdirs();
                    
                    picDL(picUrl, new Callback() {
                        String path = dir+fileName;
                        String day = weekd;
                        String bname = bangumiName;
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            InputStream iss = response.body().byteStream();
                            FileOutputStream fos;
                            if (path.substring(path.length()-4).equals(".jpg")){
                                fos = new FileOutputStream(path);
                            }else {
                                path = path+"/l.jpg";
                                fos = new FileOutputStream(path);
                            }
                            int len;
                            byte[] b = new byte[1024];
                            while ((len=iss.read(b))!=-1){
                                fos.write(b,0,len);
                            }
                            response.close();
                            fos.close();
                            iss.close();
                            Bangumi bangumi = new Bangumi();
                            bangumi.setWeek(day);
                            bangumi.setName(bname);
                            bangumi.setPicPath(path);
                            bangumi.setDetailUrl(detailUrl);
                            bangumi.save();
                            publishProgress();
                            //Log.d("numbersss",""+path);
                        }
                    });
                    /*OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url("http://"+picUrl).build();
                        Response response = client.newCall(request).execute();
                        if (response!=null){
                            is = response.body().byteStream();
                            byte[] b = new byte[1024];
                            int len = 0;
                            while ((len=is.read(b))!=-1){
                                fos.write(b,0,len);
                            }
                        }
                        response.body().close();*/

                        Log.d("dbisexi","success");


                    }
                    week++;
            }
        }catch (Exception e){
            e.printStackTrace();Log.d("dbisexieeeeeeee","exception");
        }finally {
            Log.d("dbisexieeeeeeee","exceptionfffffffffffffff");
        }
        publishProgress();
        return SUCCESSED;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        //fresh.fresh(MainActivity.WEEK);
        MainActivity.viewPager.setAdapter(new VPAdapter());
        MainActivity.refreshLayout.setRefreshing(false);
        MainActivity.viewPager.setCurrentItem(MainActivity.position);

    }


    private void picDL( String picUrl, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://"+picUrl).build();
        client.newCall(request).enqueue(callback);
    }
}
