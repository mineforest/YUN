package com.example.poke;

public class UserHistory {
    String recipeHistory;
    int stars;

    public UserHistory(String recipeHistory, int stars) {
        this.recipeHistory = recipeHistory;
        this.stars = stars;
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
