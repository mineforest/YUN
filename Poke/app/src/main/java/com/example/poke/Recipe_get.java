package com.example.poke;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firestore.v1.WriteResult;

import java.util.ArrayList;
import java.util.List;
import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Recipe_get {
    String id;
    String name;
    String thumbnail;
    String url;
    String[] ingredient_ids;
    String time;
    String[] ingre_list;
    String[] sauce_list;
    String[] recipe;
    String[] recipe_img;
    String[] tag;

    public Recipe_get() {};
    public Recipe_get(String rcp_title, String thumbnail, String cook_time) {
        this.name = rcp_title;
        this.thumbnail = thumbnail;
        this.time = cook_time;
    }

    public Recipe_get(String id, String name, String thumbnail, String url, String[] ingredient_ids, String time, String[] ingre_list, String[] sauce_list, String[] recipe, String[] recipe_img, String[] tag) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.url = url;
        this.ingredient_ids = ingredient_ids;
        this.time = time;
        this.ingre_list = ingre_list;
        this.sauce_list = sauce_list;
        this.recipe = recipe;
        this.recipe_img = recipe_img;
        this.tag = tag;
    }

    public String[] getSauce_list() {
        return sauce_list;
    }

    public void setSauce_list(String[] sauce_list) {
        this.sauce_list = sauce_list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String[] getIngredient_ids() {
        return ingredient_ids;
    }

    public void setIngredient_ids(String[] ingredient_ids) {
        this.ingredient_ids = ingredient_ids;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String[] getIngre_list() {
        return ingre_list;
    }

    public void setIngre_list(String[] ingre_list) {
        this.ingre_list = ingre_list;
    }

    public String[] getRecipe() {
        return recipe;
    }

    public void setRecipe(String[] recipe) {
        this.recipe = recipe;
    }

    public String[] getRecipe_img() {
        return recipe_img;
    }

    public void setRecipe_img(String[] recipe_img) {
        this.recipe_img = recipe_img;
    }

    public String[] getTag() {
        return tag;
    }

    public void setTag(String[] tag) {
        this.tag = tag;
    }
}
