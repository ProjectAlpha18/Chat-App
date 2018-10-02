package com.example.android.chatapp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
//        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user == null) {
//                    //user is signed out
//                    Intent intent = new Intent(MainActivity.this, SignInActivity.class);
//                    startActivity(intent);
//                }else{
//                    Intent intent=new Intent(MainActivity.this,OverviewActivity.class);
//                    startActivity(intent);
//                }
//            }
//        };
        Intent intent=new Intent(MainActivity.this,OverviewActivity.class);
        startActivity(intent);
        finish();
    }
}
