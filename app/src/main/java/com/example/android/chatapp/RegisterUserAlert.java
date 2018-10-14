package com.example.android.chatapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
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

public class RegisterUserAlert extends AppCompatDialogFragment {

    private FirebaseAuth mAuth;
    private EditText editTextEmail,editTextPassword,editTextUsername;
    private Button signUpButton;
    private ProgressBar progressBar;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View mView = inflater.inflate(R.layout.activity_register_user_alert, null);
        mBuilder.setView(mView);

        editTextUsername = mView.findViewById(R.id.etUsername);
        editTextEmail = mView.findViewById(R.id.etRegisterEmail);
        editTextPassword = mView.findViewById(R.id.etRegisterPassword);
        signUpButton = mView.findViewById(R.id.bSignUp);
        mAuth = FirebaseAuth.getInstance();

        progressBar = mView.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Clicked", "SignUp");
                registerUser();
            }
        });

        return mBuilder.create();
    }

    private void registerUser() {
        final String username = editTextUsername.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (username.isEmpty()) {
            editTextUsername.setError(getString(R.string.input_error_username));
            editTextUsername.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        /*if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            editTextUsername.setError(getString(R.string.input_error_username_invalid));
            editTextUsername.requestFocus();
            //progressBar.setVisibility(View.GONE);
            return;
        }*/


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

                            User user = new User(email,username);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getActivity(), OverviewActivity.class);
                                        progressBar.setVisibility(View.GONE);
                                        startActivity(intent);
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}

