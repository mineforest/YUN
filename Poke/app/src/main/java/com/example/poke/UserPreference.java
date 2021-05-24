package com.example.poke;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class UserPreference {
    String preference;

    public UserPreference(){}

    public UserPreference(String preference) {
        this.preference = preference;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("preference", preference);

        return result;
    }
}
