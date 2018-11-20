package com.example.l.netdisk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Filelist extends AsyncTask <String,ArrayList<FileMsg>,Void>{
    private boolean isFile = false;

    @Override
    protected Void doInBackground(String... strings) {
        Socket socket = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        ArrayList<FileMsg> fileMsgs = new ArrayList<>();
        ArrayList<String> results = new ArrayList<>();
        try{
            socket = new Socket(ServerFileListActivity.SERVER_IP,9805);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            if (strings.length==1){
                bufferedWriter.write(strings[0]);
                bufferedWriter.flush();
                socket.shutdownOutput();
            }else {
                bufferedWriter.write(strings[0]+"splittoken"+strings[1]);
                bufferedWriter.flush();
                socket.shutdownOutput();
            }

            String string;
            while ((string = bufferedReader.readLine())!=null){
                results.add(string);
            }

            if(results.get(results.size()-1).equals("isFile")){
                FileMsg msg = new FileMsg();
                msg.setFilename(results.get(0));
                fileMsgs.add(msg);
                isFile = true;

            }else {
                ServerFileListActivity.parentpath = results.get(0);
                for (int i = 1;i < results.size();i++){
                    FileMsg fileMsg = new FileMsg();
                    fileMsg.setParent(results.get(0));
                    fileMsg.setFilename(results.get(i));
                    fileMsgs.add(fileMsg);
                }
            }

            publishProgress(fileMsgs);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (bufferedWriter!=null){
                    bufferedWriter.close();
                }
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
    protected void onProgressUpdate(final ArrayList<FileMsg>... values) {
        if (isFile){
            AlertDialog.Builder builder = new AlertDialog
                    .Builder(ServerFileListActivity.views.get(0).getContext());
            builder.setTitle("确定下载到本地么?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.dlBinder.startDL(values[0].get(0).getFilename());
                            return;
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
            builder.show();

        }else {
            RecyclerView recyclerView = (RecyclerView) ServerFileListActivity.views.get(0).findViewById(R.id.filelist_recyc);
            LinearLayoutManager manager = new LinearLayoutManager(recyclerView.getContext());
            FileMsgAdpater adpater = new FileMsgAdpater(values[0]);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adpater);
            return;
        }


    }
}
