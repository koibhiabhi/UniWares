package com.example.uniwares;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.uniwares.Adapters.ChatAdapter;
import com.example.uniwares.Models.MessageModel;
import com.example.uniwares.databinding.ActivityChatScreenBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class chat_screen extends AppCompatActivity {

    private static final String TAG = "MAIN_TAG";
    ActivityChatScreenBinding binding;

    FirebaseDatabase db;

    FirebaseAuth auth;

    public static final String FCM_SERVER_KEY = "AAAArMhDUE4:APA91bHZEqYMSQN6k9xg6PqsZ7MTUrcPgr-uMeQvAbicW9HBXm1CBStmSbn_bhziui9F2etR3cCcK3Lv7h570NGVU8kKLaM7ZQpjnhZXZt9p8GqvrgFMjHI1i4f-8oG5YAd-sjOCbevs";


    public  static final String NOTIFICTION_TYPE_NEW_MESSAGE = "NEW MESSAGE";

    private String myUid;

    private String myName;

    private String recieptFCMToken = "";

    private String recieptUid = "";

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
        String profilePic = getIntent().getStringExtra("profilepic");



        binding.username.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.avatar).into(binding.profileImage);


        loadMyInfo();
        loadReciptDetails(recieveID);


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
                        layoutmanager.scrollToPosition(messageModels.size() - 1);


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
                                            layoutmanager.scrollToPosition(messageModels.size() - 1);
                                            prepareNotification(message); // Call prepareNotification here
                                        }
                                    });
                            }
                        });
            }
        });

    }

    private void loadReciptDetails(String recieptUid) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(recieptUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("username").getValue(String.class);
                    String profilepic = snapshot.child("profilepic").getValue(String.class);
                    recieptFCMToken = snapshot.child("fcmToken").getValue(String.class);

                    Log.d(TAG, "onDataChange: name:" + name);
                    Log.d(TAG, "onDataChange: profilepic:" + profilepic);
                    Log.d(TAG, "onDataChange: recieptFCMToken:" + recieptFCMToken);

                    // Update your UI or do other operations with the fetched data
                } else {
                    Log.d(TAG, "onDataChange: User with UID " + recieptUid + " does not exist.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: " + error.getMessage());
            }
        });
    }



    private void loadMyInfo(){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        ref.child(""+auth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        myName = ""+ snapshot.child("username").getValue();
                        Log.d(TAG, "OnDataChange: myName: "+myName);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



    }

    private void prepareNotification( String message){

        Log.d(TAG,"prepareNotification: ");

        JSONObject notificationJo = new JSONObject();
        JSONObject notificationDataJo = new JSONObject();
        JSONObject notificationNotificationJo = new JSONObject();

        try {

            notificationDataJo.put("notificationType: ", " "+NOTIFICTION_TYPE_NEW_MESSAGE);
            notificationDataJo.put("senderUid: ", " "+auth.getUid());

            notificationNotificationJo.put("title",""+ myName);
            notificationNotificationJo.put("body",""+ message);
            notificationNotificationJo.put("sound",""+ "default");

            notificationJo.put("to",""+ recieptFCMToken);
            notificationJo.put("notification", notificationNotificationJo);
            notificationJo.put("data", notificationDataJo);


        } catch (Exception e){
            Log.e(TAG, "prepareNotification: ",e);

        }

        sendFcmNotification(notificationJo);

    }

    private void sendFcmNotification(JSONObject notificationJo) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(

                Request.Method.POST,
                "https://fcm.googleapis.com/fcm/send",
                notificationJo,
                response -> {

                    Log.d(TAG, "sendFcmNotification: "+response.toString());

                },
                error -> {

                    Log.d(TAG, "sendFcmNotification: "+error);


                }


        ){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> headers = new HashMap<>();
                headers.put("Contet-Type", "application/json");
                headers.put("Contet-Type", "application/json");
                headers.put("Authorization", "key="+FCM_SERVER_KEY);


                return headers;
            }
        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }


}