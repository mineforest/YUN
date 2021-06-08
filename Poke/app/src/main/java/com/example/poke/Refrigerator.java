package com.example.poke;

public class Refrigerator {
    public static String ingredient;
    String users;
    String fridgeName;

    public void Refrigerator(String users, String ingredient, String fridgeName){
        this.users = users;
        this.ingredient = ingredient;
        this.fridgeName = fridgeName;
    }

    public void setFridgeName(String fridgeName) {
        this.fridgeName = fridgeName;
    }

    public String getFridgeName() {
        return fridgeName;
    }

    public String getUsers(){
        return users;
    }
    public void setUsers(String userId){
        this.users = users;
    }

    public String getIngredient(){
        return ingredient;
    }
    public void setIngredient(String name){
        this.ingredient = ingredient;
    }
}
