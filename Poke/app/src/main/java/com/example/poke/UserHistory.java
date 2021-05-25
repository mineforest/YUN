package com.example.poke;

public class UserHistory {
    Long rate;
    String recipeTitle;
    String recipeID;
    String date;
    String recipeImage;

    public UserHistory() {}
    public UserHistory(String recipeTitle, String recipeImage, String date, Long rate){
        this.recipeTitle = recipeTitle;
        this.recipeImage = recipeImage;
        this.date = date;
        this.rate = rate;
    }

    public UserHistory(String recipeTitle, String recipeImage, String date, Long rate, String recipeID){
        this.recipeTitle = recipeTitle;
        this.recipeImage = recipeImage;
        this.date = date;
        this.rate = rate;
        this.recipeID = recipeID;
    }

    public UserHistory(String recipeTitle, String recipeID, String date) {
        this.recipeTitle = recipeTitle;
        this.recipeID = recipeID;
        this.date = date;
    }

    public void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRecipeID(String recipeID) {
        this.recipeID = recipeID;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public void setRate(Long rate) {
        this.rate = rate;
    }

    public String getDate() {
        return date;
    }

    public String getRecipeID() {
        return recipeID;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public Long getRate() {
        return rate;
    }
}
