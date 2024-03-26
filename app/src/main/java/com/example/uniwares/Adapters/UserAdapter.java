package com.example.uniwares.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uniwares.R;
import com.example.uniwares.chat_screen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private final ArrayList<Map<String, String>> userList;
    private final Context context;

    FirebaseDatabase db = FirebaseDatabase.getInstance();


    public UserAdapter(ArrayList<Map<String, String>> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, String> userData = userList.get(position);
        String username = userData.get("username");
        String profilePicUrl = userData.get("profilepic");
        String userID = userData.get("userId");


        holder.userName.setText(username);

        // Load profile picture using Picasso library
        Picasso.get().load(profilePicUrl).placeholder(R.drawable.avatar).into(holder.profileImage);



        FirebaseDatabase.getInstance().getReference().child("chats").child(FirebaseAuth.getInstance().getUid() + userData.get("userId"))
                        .orderByChild("timestamp").limitToLast(1)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.hasChildren()){
                                    for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                        holder.lastMessage.setText(snapshot1.child("message").getValue().toString());
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(context, chat_screen.class);
                intent.putExtra("username", username);
                intent.putExtra("userId", userID); //Pass the receiver's ID here
                intent.putExtra("profilepic", profilePicUrl);

                // Start the activity
                context.startActivity(intent);
            }
        });

    }

    public void filterList(ArrayList<Map<String, String>> filteredList) {
        userList.clear();
        userList.addAll(filteredList);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView userName, lastMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            userName = itemView.findViewById(R.id.userName);
            lastMessage = itemView.findViewById(R.id.lastMessage);
        }
    }
}
