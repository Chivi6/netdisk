package com.example.l.netdisk;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.File;

public class DLService extends Service {
    private FileDL dlTask;
    private String dlFilename;
    private DLListener listener = new DLListener() {
        @Override
        public void onPause() {
            dlTask = null;
            getNotificationManager().notify(1,getNotification("已暂停",-1));
        }

        @Override
        public void onSuccess() {
            dlTask = null;
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("已完成",-1));
            Toast.makeText(DLService.this,"下载完成",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancle() {
            dlTask = null;
            stopForeground(true);
        }

        @Override
        public void onFailed() {
            dlTask = null;
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("下载失败",-1));
        }

        @Override
        public void onProgress(long progress) {
            getNotificationManager().notify(1,getNotification("下载中...",progress));

        }
    };

    private DLBinder binder = new DLBinder();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder;
    }

    class DLBinder extends Binder{
        public void startDL(String filename){
            if (dlTask==null){
                dlFilename = filename;
                dlTask = new FileDL(listener);
                dlTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,filename);
                startForeground(1,getNotification("下载中",0));
                //Toast.makeText(DLService.this,"开始下载",Toast.LENGTH_SHORT).show();
            }
        }

        public void cancleDL(){
            if (dlTask != null){
                dlTask.setCancled();
                String filename = dlFilename.substring(dlFilename.lastIndexOf("\\")+1);
                String filePath = Environment.
                        getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()
                        +"/"+filename;
                File file = new File(filePath);
                if (file.exists()){
                    file.delete();
                }
                getNotificationManager().cancel(1);
                stopForeground(true);
            }
        }

        public void pauseDL(){
            if (dlTask!=null){
                dlTask.setPaused();
            }
        }
    }

    private NotificationManager getNotificationManager(){
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private Notification getNotification(String title,long progress){
        Intent intent = new Intent(this,ServerFileListActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,0,intent,0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,title);
        builder.setContentIntent(pi)
        .setContentTitle(title)
        .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
        .setSmallIcon(R.mipmap.ic_launcher);

        if (progress > 0){
        progress = progress/1024;
        if (progress <= 1024){
            builder.setContentText(progress+"KB");
        }else if (progress >1024){
            progress = progress/1024;
            builder.setContentText(progress+"MB");
        }

        }
        return builder.build();

    }
}
