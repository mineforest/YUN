package com.example.poke;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class UserAllergy {
    String allergy;

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
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("allergy", allergy);

        return result;
    }
}
