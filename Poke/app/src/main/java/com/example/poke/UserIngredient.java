package com.example.poke;

public class UserIngredient {
    String ingredientTitle;
    String regist_date;
    String expirationDate;
    String category;
    String ingredientKey;

    public UserIngredient(){

    }

    public UserIngredient(String ingredientTitle, String expirationDate, String category) {
        this.ingredientTitle = ingredientTitle;
        this.expirationDate = expirationDate;
        this.category = category;

    }

    public UserIngredient(String ingredientTitle, String expirationDate, String category, String ingredientKey) {
        this.ingredientTitle = ingredientTitle;
        this.expirationDate = expirationDate;
        this.category = category;
        this.ingredientKey = ingredientKey;
    }

    public void setIngredientKey(String ingredientKey) {
        this.ingredientKey = ingredientKey;
    }

    public String getIngredientKey() {
        return ingredientKey;
    }

    public void setIngredientTitle(String ingredientTitle) {
        this.ingredientTitle = ingredientTitle;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIngredientTitle() {
        return ingredientTitle;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getCategory() {
        return category;
    }
}
