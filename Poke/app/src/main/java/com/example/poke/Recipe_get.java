package com.example.poke;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import java.util.Comparator;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Recipe_get implements Parcelable {
    String id;
    String name;
    String thumbnail;
    String url;
    List<Long> ingredient_ids;
    String time;
    List<Map<String, String>> ingre_list;
    List<Map<String, String>> sauce_list;
    List<String> recipe;
    List<String> recipe_img;
    List<String> tag;
    Long rate;

    public Recipe_get() {};

    public Recipe_get(String rcp_id,String thumbnail, String name, List<String> tag) {
        this.id = rcp_id;
        this.thumbnail = thumbnail;
        this.name = name;
        this.tag = tag;
    }


    public Recipe_get(String id, String name, String thumbnail, String url, List<Long> ingredient_ids, String time, List<Map<String, String>> ingre_list, List<Map<String, String>> sauce_list, List<String> recipe, List<String> recipe_img, List<String> tag) {
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

    public Recipe_get(String rcp_id, String title, String thumbnail, String cook_time, List<Map<String, String>> ingre_list, List<String> tag) {
        this.id = rcp_id;
        this.name = title;
        this.thumbnail = thumbnail;
        this.time = cook_time;
        this.ingre_list = ingre_list;
        this.tag = tag;
    }

    protected Recipe_get(Parcel in) {
        id = in.readString();
        name = in.readString();
        thumbnail = in.readString();
        url = in.readString();
        time = in.readString();
        recipe = in.createStringArrayList();
        recipe_img = in.createStringArrayList();
        tag = in.createStringArrayList();
        if (in.readByte() == 0) {
            rate = null;
        } else {
            rate = in.readLong();
        }
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(thumbnail);
        dest.writeString(url);
        dest.writeString(time);
        dest.writeStringList(recipe);
        dest.writeStringList(recipe_img);
        dest.writeStringList(tag);
        if (rate == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(rate);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Recipe_get> CREATOR = new Creator<Recipe_get>() {
        @Override
        public Recipe_get createFromParcel(Parcel in) {
            return new Recipe_get(in);
        }

        @Override
        public Recipe_get[] newArray(int size) {
            return new Recipe_get[size];
        }
    };

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

    public List<Long> getIngredient_ids() {
        return ingredient_ids;
    }

    public void setIngredient_ids(List<Long> ingredient_ids) {
        this.ingredient_ids = ingredient_ids;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Map<String, String>> getIngre_list() {
        return ingre_list;
    }

    public void setIngre_list(List<Map<String, String>> ingre_list) {
        this.ingre_list = ingre_list;
    }

    public List<Map<String, String>> getSauce_list() {
        return sauce_list;
    }

    public void setSauce_list(List<Map<String, String>> sauce_list) {
        this.sauce_list = sauce_list;
    }

    public List<String> getRecipe() {
        return recipe;
    }

    public void setRecipe(List<String> recipe) {
        this.recipe = recipe;
    }

    public List<String> getRecipe_img() {
        return recipe_img;
    }

    public void setRecipe_img(List<String> recipe_img) {
        this.recipe_img = recipe_img;
    }

    public List<String> getTag() {
        return tag;
    }

    public void setTag(List<String> tag) {
        this.tag = tag;
    }
    
    public void setRate(ArrayList<String> myIngreList) {
        int cnt = 0;
        for (String myIngre : myIngreList) {
            for (Map<String, String> tmpIngre : this.getIngre_list()) {
                if (tmpIngre.containsValue(myIngre)) {
                    cnt++;
                }
            }
        }
        this.rate = Math.round((double) cnt / (double) this.getIngre_list().size() * 100.0);
    }

    public Long getRate() {
        return this.rate;
    }
}