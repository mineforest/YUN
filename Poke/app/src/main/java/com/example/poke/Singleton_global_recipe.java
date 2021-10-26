package com.example.poke;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Singleton_global_recipe {
    private static Singleton_global_recipe instance;
    private ArrayList<Recipe_get> rcps;

    private Singleton_global_recipe() {}

    public void setData(ArrayList<Recipe_get> rcps) {
        this.rcps = rcps;
    }

    public ArrayList<Recipe_get> getData() {
        return this.rcps;
    }

    public static synchronized Singleton_global_recipe getInstance() {
        if(instance == null){
            instance = new Singleton_global_recipe();
        }
        return instance;
    }
}
