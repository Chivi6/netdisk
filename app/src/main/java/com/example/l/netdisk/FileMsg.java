package com.example.l.netdisk;

public class FileMsg {
    private String filename;
    private String parent = "NO";
    private String filestatus = "";
    private String DLspeed    = "";

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getParent() {
        return parent;
    }

    public String getFilename() {
        return filename;
    }

    public String getFilestatus() {
        return filestatus;
    }

    public String getDLspeed() {
        return DLspeed;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setFilestatus(String filestatus) {
        this.filestatus = filestatus;
    }

    public void setDLspeed(String DLspeed) {
        this.DLspeed = DLspeed;
    }
}
