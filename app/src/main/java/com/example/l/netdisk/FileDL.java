package com.example.l.netdisk;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.net.Socket;

public class FileDL extends AsyncTask<String, Long,Integer> {
    public static final int DL_SUCCESS = 0;
    public static final int DL_FAILED = 1;
    public static final int DL_CANCLED = 2;
    public static final int DL_PAUSED = 3;

    private boolean isPaused = false;
    private boolean isCancled = false;

    private DLListener listener;

    public FileDL(DLListener listener){
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        BufferedWriter writer = null;
        InputStream is = null;
        RandomAccessFile dlFile = null;
        Socket socket = null;
        try{
            socket = new Socket(ServerFileListActivity.SERVER_IP,9804);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String filename = strings[0].substring(strings[0].lastIndexOf("\\")+1);
            String filePath = Environment.
                    getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()
                    +"/"+filename;
            File file = new File(filePath);
            dlFile = new RandomAccessFile(file,"rw");
            if (file.exists()){
                writer.write(strings[0]+"splittoken"+file.length());
                dlFile.seek(file.length());
            }else {
                writer.write(strings[0]+"splittoken"+0);
            }
            writer.flush();
            socket.shutdownOutput();

            is = socket.getInputStream();
            byte[] b = new byte[1024];
            int len;
            Timer oneSec = null;

            while ((len = is.read(b))!=-1){
                if (isCancled){
                    return DL_CANCLED;
                }else if (isPaused){
                    return DL_PAUSED;
                }else {
                    dlFile.write(b,0,len);

                    if (oneSec!=null){
                        if (oneSec.isOneSec()){
                            publishProgress(dlFile.length());
                            oneSec = new Timer();
                            oneSec.start();
                        }
                    }else {
                        oneSec = new Timer();
                        oneSec.start();
                    }

                }



            }

            return DL_SUCCESS;

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (dlFile != null){
                    dlFile.close();
                }
                if (writer != null){
                    writer.close();
                }
                if (is != null){
                    is.close();
                }
                if (socket != null){
                    socket.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return DL_FAILED;
    }

    @Override
    protected void onProgressUpdate(Long... values) {
        listener.onProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Integer integer) {
        switch (integer){
            case DL_SUCCESS:
                listener.onSuccess();
                break;
            case DL_FAILED:
                listener.onFailed();
                break;
            case DL_PAUSED:
                listener.onPause();
                break;
            case DL_CANCLED:
                listener.onCancle();
                break;
                default:
                    break;
        }
    }

    public void setCancled() {
        isCancled = true;
    }

    public void setPaused() {
        isPaused = true;
    }
}
