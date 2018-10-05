package com.example.android.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the activity to use the settings_activity.xml layout file
        setContentView(R.layout.activity_settings);

        TextView profilepic = findViewById(R.id.profilePicture);

        //Set a click listener on that view
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, ProfilePicActivity.class);
                startActivity(intent);
            }
        });

        TextView logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SettingsActivity.this, SignInActivity.class));
                Toast.makeText(SettingsActivity.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
