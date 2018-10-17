package com.example.android.chatapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatViewHolder> {

    private Context mCtx;
    private List<ChatItem> chatsList;


    public ChatsAdapter(Context mCtx, List<ChatItem> chatsList) {
        this.mCtx = mCtx;
        this.chatsList = chatsList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.chat_list_item, null);

        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatViewHolder chatViewHolder, final int i) {
        final ChatItem chatItem = chatsList.get(i);
        chatViewHolder.userNameTextView.setText(chatItem.getUserName());
        Glide.with(mCtx).load(chatItem.getImagePath()).into(chatViewHolder.displayPictureImageView);

        chatViewHolder.chatItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, ChatWindowActivity.class);
                intent.putExtra("User Name", chatItem.getUserName());
                intent.putExtra("Image Url", chatItem.getImagePath());

                mCtx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatsList.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {

        ImageView displayPictureImageView;
        TextView userNameTextView;
        LinearLayout chatItemLayout;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            displayPictureImageView = itemView.findViewById(R.id.displayPictureImageView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            chatItemLayout = itemView.findViewById(R.id.chatItemLayout);
        }
    }
}
