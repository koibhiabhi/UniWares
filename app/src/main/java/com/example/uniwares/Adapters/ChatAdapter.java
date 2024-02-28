package com.example.uniwares.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uniwares.Models.MessageModel;
import com.example.uniwares.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter {



    ArrayList<MessageModel> msgmodel;
    Context context;

    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    public ChatAdapter(ArrayList<MessageModel> msgmodel, Context context) {
        this.msgmodel = msgmodel;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SENDER_VIEW_TYPE)
        { View view = LayoutInflater.from(context).inflate(R.layout.senders_msg, parent, false );
            return new SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.reciever_msg, parent, false );
            return new RecieverViewHolder(view);


        }
    }


    @Override
    public int getItemViewType(int position) {

        if (msgmodel.get(position).getuID().equals(FirebaseAuth.getInstance().getUid()))
        {
            return SENDER_VIEW_TYPE;
        } else {
            return RECEIVER_VIEW_TYPE;
        }



    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessageModel messageModel = msgmodel.get(position);


        if (holder.getClass() == SenderViewHolder.class) {
            ((SenderViewHolder)holder).sendmsg.setText(messageModel.getMessage());
            ((SenderViewHolder)holder).sendtime.setText(formatTime(messageModel.getTimestamp()));

        } else {
            ((RecieverViewHolder)holder).recievemsg.setText(messageModel.getMessage());
            ((RecieverViewHolder)holder).recievetime.setText(formatTime(messageModel.getTimestamp()));
        }




    }

    private String formatTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    @Override
    public int getItemCount() {

        return msgmodel.size();
    }

    public  class RecieverViewHolder extends RecyclerView.ViewHolder {

        TextView recievemsg, recievetime;

        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);

            recievemsg = itemView.findViewById(R.id.recievermsg);
            recievetime = itemView.findViewById(R.id.rtime);


        }
    }


    public class SenderViewHolder extends  RecyclerView.ViewHolder {

        TextView sendmsg, sendtime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            sendmsg = itemView.findViewById(R.id.sendermsg);
            sendtime = itemView.findViewById(R.id.astime);
        }
    }




}
