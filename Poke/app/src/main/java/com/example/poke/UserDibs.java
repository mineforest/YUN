package com.example.poke;

public class UserDibs {
    String dipsTitle;
    String dipsImage;

    public UserDibs(String dipsImage, String dipsTitle) {
        this.dipsImage = dipsImage;
        this.dipsTitle = dipsTitle;
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

