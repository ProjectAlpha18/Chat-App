package com.example.android.chatapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NewChatActivity extends AppCompatActivity {
    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
    List<ChatItem> allUsersList;
    ChatsAdapter usersAdapter;
    RecyclerView usersRecyclerView;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);
        progressBar = findViewById(R.id.usersProgressBar);
        progressBar.setVisibility(View.VISIBLE);
        allUsersList = new ArrayList<>();
        usersRecyclerView = findViewById(R.id.usersRecyclerView);
        usersRecyclerView.setHasFixedSize(true);

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    String userName = d.child("Name").getValue(String.class);
                    Log.i("username", userName);
                    String displayPicUrl = d.child("displayPictureLink").getValue(String.class);
                    Log.i("url", displayPicUrl);
                    allUsersList.add(new ChatItem(displayPicUrl, userName));
                }
                usersAdapter = new ChatsAdapter(NewChatActivity.this, allUsersList);
                usersRecyclerView.setAdapter(usersAdapter);
                usersRecyclerView.setLayoutManager(new LinearLayoutManager(NewChatActivity.this));
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
