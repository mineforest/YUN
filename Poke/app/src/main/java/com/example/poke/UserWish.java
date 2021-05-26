package com.example.poke;

public class UserWish {
    String wish_image;
    String wish_title;

    public UserWish() {

    }
    public UserWish(String wish_image, String wish_title) {
        this.wish_image = wish_image;
        this.wish_title = wish_title;
    }
    public String getWish_image() {
        return wish_image;
    }

    public void setWish_image(String wish_image) {
        this.wish_image = wish_image;
    }

    public String getWish_title() {
        return wish_title;
    }

    public void setWish_title(String wish_title) {
        this.wish_title = wish_title;
    }
}
