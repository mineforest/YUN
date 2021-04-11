package com.example.tcnk;

import androidx.annotation.NonNull;

public class User {
    public String userName;
    public String email;

    public User(){

    }

    public User(String userName, String email){
        this.userName = userName;
        this.email = email;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' + ", email'" + email + '\'' + '}';
    }
}
