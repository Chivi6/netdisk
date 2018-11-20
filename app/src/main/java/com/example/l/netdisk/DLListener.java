package com.example.l.netdisk;

public interface DLListener {
    void onPause();
    void onSuccess();
    void onCancle();
    void onFailed();
    void onProgress(long progress);
}
