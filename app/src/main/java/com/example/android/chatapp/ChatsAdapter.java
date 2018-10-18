package com.example.android.chatapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatViewHolder> {

    private Context mCtx;
    private List<ChatItem> chatsList;
    String chatRef = null;
    int flag = -1;
    String myDisplayUrl;

    public ChatsAdapter(Context mCtx, List<ChatItem> chatsList) {
        this.mCtx = mCtx;
        Log.i(TAG, "ChatsAdapter: " + mCtx);
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
//        Log.i(TAG, "onBindViewHolder: "+chatItem.getUserName() + i);
//        Log.i(TAG, "onBindViewHolder: "+chatItem.getImagePath() + i);
//        Log.i(TAG, "onBindViewHolder: "+chatItem.getUserKey() + i);
        chatViewHolder.userNameTextView.setText(chatItem.getUserName());
        Glide.with(mCtx).load(chatItem.getImagePath()).into(chatViewHolder.displayPictureImageView);

        chatViewHolder.chatItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (OverviewActivity.adapterFlag == 1) {
                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chats").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                if (d.child("userName").getValue(String.class).equals(chatItem.getUserName())) {
                                    chatRef = d.getKey();
                                    flag = 1;
                                }
                            }
                            if (flag == -1) {
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chats");
                                chatRef = ref.push().getKey();
                                ref.child(chatRef).setValue(new ChatItem(chatItem.getImagePath(), chatItem.getUserName(), chatItem.getUserKey(),chatRef));
                                FirebaseDatabase.getInstance().getReference().child("Users").child(chatItem.getUserKey()).child("Chats").child(chatRef).setValue(new ChatItem(chatItem.getImagePath(), OverviewActivity.myUserName, FirebaseAuth.getInstance().getCurrentUser().getUid(),chatRef));

                            }
                            Intent intent = new Intent(mCtx, ChatWindowActivity.class);
                            intent.putExtra("Chat Ref", chatRef);
                            intent.putExtra("User Key", chatItem.getUserKey());
                            intent.putExtra("User Name", chatItem.getUserName());
                            intent.putExtra("Image Url", chatItem.getImagePath());

                            mCtx.startActivity(intent);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else{
                    chatRef=chatItem.getChatRef();
                    Intent intent = new Intent(mCtx, ChatWindowActivity.class);
                    intent.putExtra("Chat Ref", chatRef);
                    intent.putExtra("User Key", chatItem.getUserKey());
                    intent.putExtra("User Name", chatItem.getUserName());
                    intent.putExtra("Image Url", chatItem.getImagePath());
                    mCtx.startActivity(intent);
                }
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
