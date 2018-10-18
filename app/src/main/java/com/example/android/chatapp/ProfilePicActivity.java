package com.example.android.chatapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
public class ProfilePicActivity extends AppCompatActivity{

    private static final int CHOOSE_IMAGE = 101;

    TextView textView;
    ImageView imageView;
    EditText editText;
    String username;
    Uri downloadUri;
    String displayPicUrl;

    Uri uriProfileImage;
    ProgressBar progressBar;

    String profileImageUrl;

    FirebaseAuth mAuth;
    FirebaseStorage storage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.change_profile_pic);
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, SignInActivity.class));
        }

        editText = findViewById(R.id.user_name);
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressbar);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    String uid = user.getUid();
                    Log.v("uid",uid);
                    username = dataSnapshot.child(uid).child("username").getValue(String.class);
                    Log.v("username",username);
                    editText.setText(username);
                    profileImageUrl = dataSnapshot.child(uid).child("displayPictureUrl").getValue(String.class);
                    Log.v("pic url",profileImageUrl);
                    Glide.with(ProfilePicActivity.this)
                        .load(profileImageUrl)
                         .into(imageView);
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("error","The read failed: " + databaseError.getCode());
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
            }
        });

        findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });
    }

    private void saveUserInformation() {
        final String displayName = editText.getText().toString();

        if (displayName.isEmpty()) {
            editText.setError("Name required");
            editText.requestFocus();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseUser user = mAuth.getCurrentUser();
                String uid = user.getUid();
                dataSnapshot.child(uid).child("username").getRef().setValue(displayName);
                editText.setText(username);
                Log.v("picUrlBeforeSettingOnDb",displayPicUrl);
                dataSnapshot.child(uid).child("displayPictureUrl").getRef().setValue(displayPicUrl);
                Glide.with(ProfilePicActivity.this)
                        .load(displayPicUrl)
                        .into(imageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("error","The read failed: " + databaseError.getCode());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                imageView.setImageBitmap(bitmap);
                storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference().child("ProfilePhotos");
                StorageReference photoRef = storageRef.child(uriProfileImage.getLastPathSegment());
                photoRef.putFile(uriProfileImage).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful());
                        downloadUri = urlTask.getResult();
                        displayPicUrl = String.valueOf(downloadUri);
                        Log.v("storedInStorage",displayPicUrl);
                        Toast.makeText(ProfilePicActivity.this, "Upload successful.", Toast.LENGTH_SHORT).show();

                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
