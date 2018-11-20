package com.example.l.netdisk;

public class Timer extends Thread {

    private boolean isOneSec = false;

    public boolean isOneSec() {
        return isOneSec;
    }

    @Override
    public void run() {
        super.run();
        try {
            sleep(1000);
            isOneSec = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return;
    }
}
