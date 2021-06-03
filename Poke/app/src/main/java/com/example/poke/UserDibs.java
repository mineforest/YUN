package com.example.poke;

public class UserDibs {
    String dipsTitle;
    String dipsImage;
    String rcp_id;

    public UserDibs() {}
    public UserDibs(String dipsImage, String dipsTitle) {
        this.dipsImage = dipsImage;
        this.dipsTitle = dipsTitle;
    }
    public UserDibs(String rcp_id, String dipsImage, String dipsTitle){
        this.rcp_id = rcp_id;
        this.dipsImage = dipsImage;
        this.dipsTitle = dipsTitle;
    }

    public String getRcp_id() {
        return rcp_id;
    }

    public void setRcp_id(String rcp_id) {
        this.rcp_id = rcp_id;
    }

    public String getDipsTitle() {
        return dipsTitle;
    }

    public void setDipsTitle(String dipsTitle) {
        this.dipsTitle = dipsTitle;
    }

    public String getDipsImage() {
        return dipsImage;
    }

    public void setDipsImage(String dipsImage) {
        this.dipsImage = dipsImage;
    }
}

