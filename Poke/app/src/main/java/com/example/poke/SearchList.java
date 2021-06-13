package com.example.poke;

public class SearchList {
    String searchTitle;
    String searchImage;

    public SearchList() { }

    public SearchList(String searchImage, String searchTitle) {
        this.searchImage = searchImage;
        this.searchTitle = searchTitle;
    }



    public String getSearchTitle() {
        return searchTitle;
    }

    public void setSearchTitle(String searchTitle) {
        this.searchTitle = searchTitle;
    }

    public String getSearchImage() {
        return searchImage;
    }

    public void setSearchImage(String searchImage) {
        this.searchImage = searchImage;
    }
}
