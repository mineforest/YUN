package com.example.poke;

public class Historydata {

    private int history_image;
    private String history_rec;
    private String history_date;

    public Historydata(int history_image, String history_rec, String history_date) {
        this.history_image = history_image;
        this.history_rec = history_rec;
        this.history_date = history_date;
    }

    public int getHistory_image() {
        return history_image;
    }

    public void setHistory_image(int history_image) {
        this.history_image = history_image;
    }

    public String getHistory_rec() {
        return history_rec;
    }

    public void setHistory_rec(String history_rec) {
        this.history_rec = history_rec;
    }

    public String getHistory_date() {
        return history_date;
    }

    public void setHistory_date(String history_date) {
        this.history_date = history_date;
    }
}
