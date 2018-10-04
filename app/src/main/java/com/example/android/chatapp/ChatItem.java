package com.example.android.chatapp;

public class ChatItem {
    private String imagePath;
    private String userName;

    public ChatItem(String imagePath, String userName) {
        this.imagePath = imagePath;
        this.userName = userName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getUserName() {
        return userName;
    }
}
