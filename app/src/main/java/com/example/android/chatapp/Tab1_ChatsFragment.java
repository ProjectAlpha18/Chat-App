package com.example.android.chatapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class Tab1_ChatsFragment extends Fragment {

    View rootView;
    RecyclerView recyclerView;
    ChatsAdapter chatsAdapter;
    List<ChatItem> chatItemList;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_tab1_chats, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
        chatItemList = new ArrayList<>();
        OverviewActivity.adapterFlag=-1;
        recyclerView = rootView.findViewById(R.id.chatsRecyclerView);
        recyclerView.setHasFixedSize(true);
        progressBar = rootView.findViewById(R.id.chatsProgressBar);
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chats").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    String userName=d.child("userName").getValue(String.class);
                    Log.i(TAG, "onDataChange: "+userName);
                    String displayPicUrl=d.child("imagePath").getValue(String.class);
                    String userKey=d.child("userKey").getValue(String.class);
                    String chatRef=d.getKey();
                    chatItemList.add(new ChatItem(displayPicUrl,userName,userKey,chatRef));
                }
                chatsAdapter = new ChatsAdapter(getActivity(), chatItemList);
                recyclerView.setAdapter(chatsAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
