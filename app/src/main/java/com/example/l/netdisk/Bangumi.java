package com.example.l.netdisk;

import org.litepal.crud.LitePalSupport;

public class Bangumi extends LitePalSupport {

    private String name;

    private String picPath;

    private String week;


    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }
    public String getName() {
        return name;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
}
