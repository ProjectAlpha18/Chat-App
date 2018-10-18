package com.example.android.chatapp;

public class ChatItem {
    private String imagePath;
    private String userName;
    private String userKey;
    private String chatRef;



    public ChatItem(String imagePath, String userName, String userKey, String chatRef) {
        this.imagePath = imagePath;
        this.userName = userName;
        this.userKey=userKey;
        this.chatRef=chatRef;
    }
    public String getChatRef() {
        return chatRef;
    }
    public String getImagePath() {
        return imagePath;
    }

    public String getUserName() {
        return userName;
    }
    public String getUserKey() {
        return userKey;
    }
}
