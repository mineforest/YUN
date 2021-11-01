package com.example.poke;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class UserAllergy {
    String allergy;

    public String getAllergy_key() {
        return allergy_key;
    }

    public void setAllergy_key(String allergy_key) {
        this.allergy_key = allergy_key;
    }

    String allergy_key;

    public UserAllergy(){}
    public UserAllergy(String allergy){
        this.allergy = allergy;
    }


    public String getAllergy() {
        return allergy;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

    @Exclude
    public Map<String, Object> toMap() throws InterruptedException {
        HashMap<String, Object> result = new HashMap<>();
        result.put("allergy", allergy);
        return result;
    }
}
