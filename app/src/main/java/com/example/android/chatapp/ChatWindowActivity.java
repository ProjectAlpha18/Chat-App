package com.example.android.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ChatWindowActivity extends AppCompatActivity {

    TextView userNameTextView;
    ImageView displayPicImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        userNameTextView=findViewById(R.id.chatWindowUserName);
        displayPicImageView=findViewById(R.id.chatWindowDisplayPic);

        Intent intent=getIntent();
        String userName=intent.getStringExtra("User Name");
        String displayPicUrl=intent.getStringExtra("Image Url");
        userNameTextView.setText(userName);
        Glide.with(this).load(displayPicUrl).into(displayPicImageView);

    }
}
