package com.example.poke;

public class UserHistory {
    int rate;
    String recipeTitle;
    int recipeID;
    String date;
    int recipeImage;

    public UserHistory(){}

    public UserHistory(String recipeTitle, int recipeID, String date) {
        this.recipeTitle = recipeTitle;
        this.recipeID = recipeID;
        this.date = date;
    }

    public void setRecipeImage(int recipeImage) {
        this.recipeImage = recipeImage;
    }

    public int getRecipeImage() {
        return recipeImage;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getDate() {
        return date;
    }

    public int getRecipeID() {
        return recipeID;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public int getRate() {
        return rate;
    }
}
