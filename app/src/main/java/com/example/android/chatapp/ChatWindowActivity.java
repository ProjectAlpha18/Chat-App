package com.example.android.chatapp;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

public class ChatWindowActivity extends AppCompatActivity {

    TextView userNameTextView;
    ImageView displayPicImageView;
    Button sendButton;
    EditText messageEditText;
    DatabaseReference chatsRef;
    String userName,displayPicUrl,pushKey;
    FirebaseAuth mAuth;

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

        userNameTextView = findViewById(R.id.toolbarUsername);
        displayPicImageView = findViewById(R.id.toolbarIcon);

        userNameTextView=findViewById(R.id.toolbarUsername);
        displayPicImageView=findViewById(R.id.toolbarIcon);
        sendButton=findViewById(R.id.sendButton);
        messageEditText=findViewById(R.id.chatWindowMessage);

        chatsRef=FirebaseDatabase.getInstance().getReference().child("Chats");
        Intent intent = getIntent();
        userName=intent.getStringExtra("User Name");
        displayPicUrl=intent.getStringExtra("Image Url");
        pushKey=intent.getStringExtra("Chat Ref");

        userNameTextView.setText(userName);
        Glide.with(this).load(displayPicUrl).into(displayPicImageView);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=messageEditText.getText().toString().trim();
                if(!message.equals("")){
                    //Send Message
                    HashMap<String, Object> timeStampNow=new HashMap<>();
                    timeStampNow.put("timestamp",ServerValue.TIMESTAMP);
                    Message messageObj=new Message(userName,message,timeStampNow);
                    FirebaseDatabase.getInstance().getReference().child("Chats").child(pushKey).push().setValue(messageObj);
                    messageEditText.setText("");
                }
            }
        });
    }
}
