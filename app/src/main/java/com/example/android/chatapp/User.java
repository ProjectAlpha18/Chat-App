package com.example.android.chatapp;

public class User {
    public String email, username, displayPictureUrl;


    public User(){

    }

    public User(String email,String username)
    {
        this.email = email;
        this.username = username;
        this.displayPictureUrl = "https://firebasestorage.googleapis.com/v0/b/chatapp-5d2d8.appspot.com/o/deadmau5_facebook_dp_by_earcl01-d4lnn7f.jpg?alt=media&token=7e61a2d1-22fa-42dc-801a-438785eb3468";
    }
}