package com.example.poke;

public class UserIngredient {
    String ingredientTitle;
    String expirationDate;
    String category;

    public UserIngredient(String ingredientTitle, String expirationDate, String category) {
        this.ingredientTitle = ingredientTitle;
        this.expirationDate = expirationDate;
        this.category = category;
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
