package com.example.android.chatapp;

public class User {
    public String email, username, displayPictureUrl;


    public User(){

    }

    public User(String email,String username)
    {
        this.email = email;
        this.username = username;
        this.displayPictureUrl = "https://firebasestorage.googleapis.com/v0/b/chatapp-5d2d8.appspot.com/o/if_1041_boy_c_2400506.png?alt=media&token=cd095194-5b57-42ec-9a55-59657d336c85";
    }
}