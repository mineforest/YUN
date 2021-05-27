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

public class Recipe_get {
    String rcp_id;
    String rcp_title;
    String thumbnail;
    String rcp_url;
    String ingre_id[];
    String cook_time;
    String ingre[];
    String sauce[];
    String rcp_method[];
    String rcp_method_img[];
    String tag[];
    String tag2;

    Recipe_get() {};
    Recipe_get(String rcp_title, String thumbnail, String cook_time, String tag) {
        this.rcp_title = rcp_title;
        this.thumbnail = thumbnail;
        this.cook_time = cook_time;
        this.tag2 = tag;
    }

    public String getTag2() {return tag2;}

    public void setTag2(String tag2) {this.tag2 = tag2;}

    public String getRcp_id() {
        return rcp_id;
    }

    public void setRcp_id(String rcp_id) {
        this.rcp_id = rcp_id;
    }

    public String getRcp_title() {
        return rcp_title;
    }

    public void setRcp_title(String rcp_title) {
        this.rcp_title = rcp_title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getRcp_url() {
        return rcp_url;
    }

    public void setRcp_url(String rcp_url) {
        this.rcp_url = rcp_url;
    }

    public String[] getIngre_id() {
        return ingre_id;
    }

    public void setIngre_id(String[] ingre_id) {
        this.ingre_id = ingre_id;
    }

    public String getCook_time() {
        return cook_time;
    }

    public void setCook_time(String cook_time) {
        this.cook_time = cook_time;
    }

    public String[] getIngre() {
        return ingre;
    }

    public void setIngre(String[] ingre) {
        this.ingre = ingre;
    }

    public String[] getSauce() {
        return sauce;
    }

    public void setSauce(String[] sauce) {
        this.sauce = sauce;
    }

    public String[] getRcp_method() {
        return rcp_method;
    }

    public void setRcp_method(String[] rcp_method) {
        this.rcp_method = rcp_method;
    }

    public String[] getRcp_method_img() {
        return rcp_method_img;
    }

    public void setRcp_method_img(String[] rcp_method_img) {
        this.rcp_method_img = rcp_method_img;
    }

    public String[] getTag() {
        return tag;
    }

    public void setTag(String[] tag) {
        this.tag = tag;
    }

}
