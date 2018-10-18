package com.example.android.chatapp;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChatWindowActivity extends AppCompatActivity {

    TextView userNameTextView;
    ImageView displayPicImageView;
    ImageView sendButton;
    EditText messageEditText;
    DatabaseReference chatsRef;
    String userName, displayPicUrl, pushKey, currentChatRef;
    FirebaseAuth mAuth;
    ArrayList<Message> messageArrayList;
    RecyclerView messagesRecyclerView;
    MessageAdapter messageAdapter;
    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            Log.i("message", "onChildAdded: " + dataSnapshot.child("messageText").getValue(String.class));
            Message message = dataSnapshot.getValue(Message.class);
            Log.i("message OBJ", "onChildAdded: " + message.toString());
            messageArrayList.add(message);
            Log.i("bcd", "onChildAdded: " + messageArrayList);
            messageAdapter.notifyDataSetChanged();

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);


        Toolbar mToolbar = findViewById(R.id.userToolbar);

        AppCompatActivity activity = ChatWindowActivity.this;
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable leftArrow = ContextCompat.getDrawable(ChatWindowActivity.this, R.drawable.abc_ic_ab_back_material);
        leftArrow.setColorFilter(ContextCompat.getColor(ChatWindowActivity.this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        activity.getSupportActionBar().setHomeAsUpIndicator(leftArrow);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        userNameTextView = findViewById(R.id.toolbarUsername);
        displayPicImageView = findViewById(R.id.toolbarIcon);

        messageArrayList=new ArrayList<>();
        messagesRecyclerView = findViewById(R.id.chatWindowRecycler);
        messagesRecyclerView.setHasFixedSize(true);
        messageAdapter = new MessageAdapter(this, messageArrayList);
        messagesRecyclerView.setAdapter(messageAdapter);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(ChatWindowActivity.this));

        userNameTextView = findViewById(R.id.toolbarUsername);
        displayPicImageView = findViewById(R.id.toolbarIcon);
        sendButton = findViewById(R.id.sendButton);
        messageEditText = findViewById(R.id.chatWindowMessage);

        chatsRef = FirebaseDatabase.getInstance().getReference().child("Chats");
        Intent intent = getIntent();
        userName = intent.getStringExtra("User Name");
        displayPicUrl = intent.getStringExtra("Image Url");
        pushKey = intent.getStringExtra("Chat Ref");
        currentChatRef = intent.getStringExtra("Chat Ref");

        userNameTextView.setText(userName);
        Glide.with(this).load(displayPicUrl).into(displayPicImageView);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageEditText.getText().toString().trim();
                if (!message.equals("")) {
                    //Send Message
                    HashMap<String, Object> timeStampNow = new HashMap<>();
                    Object timestamp = ServerValue.TIMESTAMP;
                    timeStampNow.put("timestamp", timestamp);
                    Message messageObj = new Message(userName, message, timeStampNow);
                    FirebaseDatabase.getInstance().getReference().child("Chats").child(pushKey).push().setValue(messageObj);
                    messageEditText.setText("");
                }
            }
        });
        reference = FirebaseDatabase.getInstance().getReference().child("Chats").child(currentChatRef);
        reference.addChildEventListener(childEventListener);
    }


    @Override
    protected void onStop() {
        super.onStop();
        reference.removeEventListener(childEventListener);
        Log.i("abc", "onStop: stopped");
    }
}
