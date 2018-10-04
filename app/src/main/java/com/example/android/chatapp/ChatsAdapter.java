package com.example.android.chatapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    public void onBindViewHolder(@NonNull ChatViewHolder chatViewHolder, int i) {
        ChatItem chatItem = chatsList.get(i);
        chatViewHolder.userNameTextView.setText(chatItem.getUserName());
//        try {
//            URL url=new URL(chatItem.getImagePath());
//            Bitmap bitmap= BitmapFactory.decodeStream(url.openConnection().getInputStream());
//            chatViewHolder.displayPictureImageView.setImageBitmap(bitmap);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        Glide.with(mCtx).load(chatItem.getImagePath()).into(chatViewHolder.displayPictureImageView);
    }

    @Override
    public int getItemCount() {
        return chatsList.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {

        ImageView displayPictureImageView;
        TextView userNameTextView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            displayPictureImageView = itemView.findViewById(R.id.displayPictureImageView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
        }
    }
}
