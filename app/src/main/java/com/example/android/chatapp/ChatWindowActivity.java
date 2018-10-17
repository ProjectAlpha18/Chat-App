package com.example.android.chatapp;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

        Toolbar mToolbar = findViewById(R.id.userToolbar);

        AppCompatActivity activity = ChatWindowActivity.this;
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable leftArrow =  ContextCompat.getDrawable(ChatWindowActivity.this, R.drawable.abc_ic_ab_back_material);
        leftArrow.setColorFilter(ContextCompat.getColor(ChatWindowActivity.this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        activity.getSupportActionBar().setHomeAsUpIndicator(leftArrow);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        userNameTextView=findViewById(R.id.toolbarUsername);
        displayPicImageView=findViewById(R.id.toolbarIcon);

        Intent intent = getIntent();
        String userName = intent.getStringExtra("User Name");
        String displayPicUrl = intent.getStringExtra("Image Url");
        userNameTextView.setText(userName);
        Glide.with(this).load(displayPicUrl).into(displayPicImageView);

    }
}
