package com.example.l.netdisk;

import java.util.ArrayList;

public class Staff {
    private ArrayList<Cast> casts;
    private String    jianjie;
    private String    coverPicPath;

    public void setJianjie(String jianjie) {
        this.jianjie = jianjie;
    }

    public void setCoverPicPath(String coverPicPath) {
        this.coverPicPath = coverPicPath;
    }

    public String getJianjie() {
        return jianjie;
    }

    public String getCoverPicPath() {
        return coverPicPath;
    }

    public void setCasts(ArrayList<Cast> casts) {
        this.casts = casts;
    }

    public ArrayList<Cast> getCasts() {
        return casts;
    }
}
