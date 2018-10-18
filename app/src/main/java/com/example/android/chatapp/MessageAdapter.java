package com.example.android.chatapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private Context mCtx;
    private List<Message> messageList;

    public MessageAdapter(Context mCtx,List<Message> messageList){
        this.mCtx=mCtx;
        this.messageList=messageList;
    }
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.message, null);

        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, int i) {
        Message message=messageList.get(i);
        if (message.senderName.equals(OverviewActivity.myUserName)){
//            RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams) messageViewHolder.messageLayout.getLayoutParams();
//            lp.addRule(RelativeLayout.ALIGN_PARENT_END);
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
//            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)messageViewHolder.messageLayout.getLayoutParams();
//            if(params==null){
//                Log.i(TAG, "onBindViewHolder: null param");
//            }
//
//            params.gravity = Gravity.END;
//            layoutParams.setLayoutDirection(ViewCompat.LAYOUT_DIRECTION_RTL);
//            layoutParams.gravity=Gravity.RIGHT;
//            messageViewHolder.messageLayout.setLayoutParams(layoutParams);
            messageViewHolder.messageText.setBackgroundResource(R.drawable.my_chat_bubble);
            messageViewHolder.messageLayout.setGravity(Gravity.END);
        }else{
            messageViewHolder.messageText.setBackgroundResource(R.drawable.chat_bubble);
        }
        Log.i(TAG, "onBindViewHolder: message object"+messageViewHolder.messageText);
        messageViewHolder.messageText.setText(message.messageText);
        Log.i(TAG, "onBindViewHolder: "+message.messageText);
        messageViewHolder.timeText.setText(message.timestampCreated.get("timestamp").toString());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
    class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView messageText,timeText;
        LinearLayout messageLayout;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText=itemView.findViewById(R.id.messageTextView);
            timeText=itemView.findViewById(R.id.timeTextView);
            messageLayout=itemView.findViewById(R.id.messageLayout);
        }
    }
}
