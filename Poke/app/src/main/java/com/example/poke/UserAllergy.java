package com.example.poke;

public class UserAllergy {
    String allergy;
    String preference;

    public UserAllergy(){}
    public UserAllergy(String preference){
        this.preference = preference;
    }

    public UserAllergy(String preference, String allergy) {
        this.preference = preference;
        this.allergy = allergy;
    }

    public String getAllergy() {
        return allergy;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }
}
