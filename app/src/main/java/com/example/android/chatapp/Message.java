package com.example.android.chatapp;

import java.util.HashMap;

public class Message {
    String senderName;
    String messageText;
    HashMap<String, Object> timestampCreated;

    public Message(String senderName, String messageText, HashMap<String, Object> timestampCreated) {
        this.senderName = senderName;
        this.messageText = messageText;
        this.timestampCreated = timestampCreated;
    }


}
