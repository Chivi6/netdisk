package com.example.l.netdisk;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class AddUri extends AsyncTask <String,Void,Void>{
    private boolean isAddSuccessed = false;
    @Override
    protected Void doInBackground(String... strings) {
        String uri = strings[0];
        Socket socket = null;
        BufferedWriter bufferedWriter = null;
        try {
            socket = new Socket(ServerFileListActivity.SERVER_IP,9803);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write(uri);
            bufferedWriter.flush();
            socket.shutdownOutput();
            isAddSuccessed = true;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (bufferedWriter!=null){
                    bufferedWriter.close();
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
    protected void onPostExecute(Void aVoid) {
        if (isAddSuccessed)
        new DLMsg().execute();
    }
}
