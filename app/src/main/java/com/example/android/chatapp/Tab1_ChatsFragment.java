package com.example.android.chatapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class Tab1_ChatsFragment extends Fragment {

    View rootView;
    RecyclerView recyclerView;
    ChatsAdapter chatsAdapter;
    List<ChatItem> chatItemList;
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
        chatItemList = new ArrayList<>();
        recyclerView = rootView.findViewById(R.id.chatsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //TODO
        //add items to list
        chatItemList.add(new ChatItem("https://image.flaticon.com/icons/png/512/33/33785.png", "User 1"));
        chatItemList.add(new ChatItem("https://image.flaticon.com/icons/png/512/33/33785.png", "User 2"));
        chatsAdapter = new ChatsAdapter(getActivity(), chatItemList);
        recyclerView.setAdapter(chatsAdapter);
    }
}
