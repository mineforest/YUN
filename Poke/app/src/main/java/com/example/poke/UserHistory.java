package com.example.poke;

public class UserHistory {
    String rcp_id;
    Long rate;
    String recipeTitle;
    String date;
    String recipeImage;

    public UserHistory() {}

    public UserHistory(String rcp_id, String recipeTitle, String recipeImage, String date, Long rate){
        this.rcp_id = rcp_id;
        this.recipeTitle = recipeTitle;
        this.recipeImage = recipeImage;
        this.date = date;
        this.rate = rate;
    }

    public UserHistory(String rcp_id, String recipeTitle, String recipeImage){
        this.rcp_id = rcp_id;
        this.recipeTitle = recipeTitle;
        this.recipeImage = recipeImage;
    }



    public String getRcp_id() {
        return rcp_id;
    }

    public void setRcp_id(String rcp_id) {
        this.rcp_id = rcp_id;
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

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public void setRate(Long rate) {
        this.rate = rate;
    }

    public String getDate() {
        return date;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public Long getRate() {
        return rate;
    }
}
