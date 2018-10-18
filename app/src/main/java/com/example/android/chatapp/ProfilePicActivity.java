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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class ProfilePicActivity extends AppCompatActivity{

    private static final int CHOOSE_IMAGE = 101;

    TextView textView;
    ImageView imageView;
    EditText editText;
    String username;
    //String photoURL;
    //String uid;

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

        editText = (EditText) findViewById(R.id.user_name);
        imageView = (ImageView) findViewById(R.id.imageView);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
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
        String displayName = editText.getText().toString();

        if (displayName.isEmpty()) {
            editText.setError("Name required");
            editText.requestFocus();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null && profileImageUrl != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();

            user.updateProfile(profile)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task){
                    if (task.isSuccessful()) {
                        //Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(this,"profile Udpated", Toast.LENGTH_SHORT)
                    }
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                imageView.setImageBitmap(bitmap);
                storage = FirebaseStorage.getInstance("/");
                StorageReference storageRef = storage.getReference();
                // TODO: upload Image To Firebase Storage;

                Bitmap bitm = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] d = baos.toByteArray();

                UploadTask uploadTask = storageRef.putBytes(d);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(ProfilePicActivity.this, "Upload Unsuccessful.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(ProfilePicActivity.this, "Upload successful.", Toast.LENGTH_SHORT).show();
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
