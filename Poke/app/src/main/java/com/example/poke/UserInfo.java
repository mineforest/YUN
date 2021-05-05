package com.example.poke;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class UserInfo {
    String birthDay;
    String name;
    String age;
    String gender;
    String preference;
    String allergy;
    String recipeHistory;

    public UserInfo() {
    }

    public UserInfo(String preference, String allergy) {
        this.preference = preference;
        this.allergy = allergy;
    }

    public UserInfo(String recipeHistory) {
        this.recipeHistory = recipeHistory;
    }

    public UserInfo(String name, String age, String birthDay, String gender){
        this.name = name;
        this.age = age;
        this.birthDay = birthDay;
        this.gender = gender;
    }

    public String getBirthDay(){
        return birthDay;
    }
    public void setBirthDay(String userId){
        this.birthDay = birthDay;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getAge(){
        return age;
    }
    public void setAge(String age){
        this.age = age;
    }

    public String getGender(){
        return gender;
    }
    public void setGender(String gender){
        this.gender = gender;
    }

    public String getPreference() {
        return preference;
    }

    public String getAllergy() {
        return allergy;
    }

    public String getRecipeHistory() {
        return recipeHistory;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

    public void setRecipeHistory(String recipeHistory) {
        this.recipeHistory = recipeHistory;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("age", age);
        result.put("birthDay", birthDay);
        result.put("gender", gender);
        return result;
    }

}
