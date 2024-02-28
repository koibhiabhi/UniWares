package com.example.uniwares;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.uniwares.Adapters.ChatAdapter;
import com.example.uniwares.Models.MessageModel;
import com.example.uniwares.databinding.ActivityChatScreenBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class chat_screen extends AppCompatActivity {

    ActivityChatScreenBinding binding;

    FirebaseDatabase db;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        final String senderID = auth.getUid();

        String recieveID = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("username");
        String profilePic = getIntent().getStringExtra("profilePicUrl");

        binding.username.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.logouni).into(binding.profileImage);


        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chat_screen.this, home.class);
                startActivity(intent);
                finish();
            }
        });



        final ArrayList<MessageModel> messageModels = new ArrayList<>();

        final ChatAdapter chatAdapter = new ChatAdapter(messageModels, this);

        binding.chatsrnrecycler.setAdapter(chatAdapter);

        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        binding.chatsrnrecycler.setLayoutManager(layoutmanager);

        final String senderroom = "chats/" + senderID + "_" + recieveID;
        final String recieverroom = "chats/" + recieveID + "_" + senderID;




        db.getReference().child("chats")
                        .child(senderroom).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        messageModels.clear();
                        for (DataSnapshot snap1 : snapshot.getChildren()) {
                            MessageModel model = snap1.getValue(MessageModel.class);

                            messageModels.add(model);
                        }
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




        binding.sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = binding.typedmsg.getText().toString();
                final MessageModel model = new MessageModel(senderID, message);
                model.setTimestamp(new Date().getTime());
                binding.typedmsg.setText("");

                db.getReference().child("chats")
                        .child(senderroom)
                        .push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            db.getReference().child("chats")
                                    .child(recieverroom)
                                    .push()
                                    .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    });
                            }
                        });


            }
        });





    }
}