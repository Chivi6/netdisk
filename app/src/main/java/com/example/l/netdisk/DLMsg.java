package com.example.l.netdisk;

import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class DLMsg extends AsyncTask <Void,FileMsg,Void>{

    private boolean isCompleted = false;
    private boolean isFailed = false;

    @Override
    protected Void doInBackground(Void... voids) {
        Socket socket = null;
        BufferedReader bufferedReader = null;
        try{
            Thread.sleep(1000);
            socket = new Socket(ServerFileListActivity.SERVER_IP,9810);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String result = bufferedReader.readLine();
            if(result.equals("getfailed")){
                isFailed = true;
                return null;
            }

            String[] status = result.split("/");
            FileMsg fileMsg = new FileMsg();
            fileMsg.setFilename(status[0]);
            fileMsg.setDLspeed(status[3]);
            if (Long.parseLong(status[1])!=0 && (Long.parseLong(status[1]) == Long.parseLong(status[2]))){
                isCompleted = true;
            }else {
                fileMsg.setFilestatus("下载中");
            }
            publishProgress(fileMsg);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (bufferedReader!=null){
                    bufferedReader.close();
                }
                if (socket!=null){
                    socket.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(FileMsg... values) {
        ArrayList<FileMsg> fileMsgs = new ArrayList<>();
        FileMsg fileMsg = values[0];
        float speed = Float.parseFloat(values[0].getDLspeed());
        speed = speed/1024;
        DecimalFormat decimalFormat = new DecimalFormat(".0");
        if (speed<1024){
            fileMsg.setDLspeed(decimalFormat.format(speed)+"KB/s");
        }else if (speed>1024){
            speed = speed/1024;
            fileMsg.setDLspeed(decimalFormat.format(speed)+"MB/s");
        }
        fileMsgs.add(fileMsg);
        RecyclerView recyclerView = ServerFileListActivity.views.get(1).findViewById(R.id.filelist_recyc);
        LinearLayoutManager manager = new LinearLayoutManager(recyclerView.getContext());
        FileMsgAdpater adpater = new FileMsgAdpater(fileMsgs);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adpater);

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (!isFailed)
        if (!isCompleted){
            new DLMsg().execute();
        }
    }
}
