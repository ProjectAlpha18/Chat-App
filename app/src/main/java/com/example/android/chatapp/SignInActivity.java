package com.example.android.chatapp;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class SignInActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private Button signUpButton, logInButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editTextEmail = findViewById(R.id.etEmail);
        editTextPassword = findViewById(R.id.etPassword);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        signUpButton = findViewById(R.id.bSignUp);
        logInButton = findViewById(R.id.bLogin);
        mAuth = FirebaseAuth.getInstance();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Clicked","SignUp");
                registerUser();
            }
        });
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logInUser();
            }
        });
    }


private void logInUser() {
    final String email = editTextEmail.getText().toString().trim();
    String password = editTextPassword.getText().toString().trim();

    if (email.isEmpty()) {
        editTextEmail.setError(getString(R.string.input_error_email));
        editTextEmail.requestFocus();
        progressBar.setVisibility(View.GONE);
        return;
    }

    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        editTextEmail.setError(getString(R.string.input_error_email_invalid));
        editTextEmail.requestFocus();
        progressBar.setVisibility(View.GONE);
        return;
    }

    if (password.isEmpty()) {
        editTextPassword.setError(getString(R.string.input_error_password));
        editTextPassword.requestFocus();
        progressBar.setVisibility(View.GONE);
        return;
    }

    if (password.length() < 6) {
        editTextPassword.setError(getString(R.string.input_error_password_length));
        editTextPassword.requestFocus();
        progressBar.setVisibility(View.GONE);
        return;
    }

    progressBar.setVisibility(View.VISIBLE);
    mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignInActivity.this, getString(R.string.logIn_success), Toast.LENGTH_LONG).show();
                    } else {
                        //error message
                    }
                }
            });
}
    private void registerUser() {

        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError(getString(R.string.input_error_email));
            editTextEmail.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError(getString(R.string.input_error_email_invalid));
            editTextEmail.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError(getString(R.string.input_error_password));
            editTextPassword.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError(getString(R.string.input_error_password_length));
            editTextPassword.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }


        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            User user = new User(email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignInActivity.this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                                    } else {
                                        //display a failure message
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}
