package com.example.poke;

public class Refrigerator {
    String users;
    String ingredient;

    public void Refrigerator(String users, String ingredient){
        this.users = users;
        this.ingredient = ingredient;
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
