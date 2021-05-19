package com.example.poke;

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
}
