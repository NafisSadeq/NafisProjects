package com.firebasedemo.nanochat;

public class UserProfile {
    private String userName;
    private String password;
    private String name;

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public UserProfile(String userName, String password, String name) {

        this.userName = userName;
        this.password = password;
        this.name = name;
    }



    public UserProfile() {
        // necessary for Firebase's deserializer
    }

}