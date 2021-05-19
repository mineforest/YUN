package com.example.poke;

import java.util.Date;

public class UserHistory {
    String recipeHistory;
    int stars;
    int recipeID;
    Date date;

    public UserHistory(String recipeHistory, int stars) {
        this.recipeHistory = recipeHistory;
        this.stars = stars;
    }

    public int getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public void setRecipeHistory(String recipeHistory) {
        this.recipeHistory = recipeHistory;
    }

    public String getRecipeHistory() {
        return recipeHistory;
    }
}
