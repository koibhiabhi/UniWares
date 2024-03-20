package com.example.uniwares.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.uniwares.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class listing extends Fragment {


    public listing() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  =  inflater.inflate(R.layout.fragment_listing, container, false);




        return view;

    }


    public static void addToFavourite(Context context, String adId){


        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null){

            Toast.makeText(context.getApplicationContext(), "You are logged in!", Toast.LENGTH_SHORT).show();



        } else {


            long timestamp = System.currentTimeMillis();


            HashMap<String, Object> hashMap = new HashMap<>();

            hashMap.put("adID", adId);
            hashMap.put("timestamp", timestamp);

            DatabaseReference re = FirebaseDatabase.getInstance().getReference("Users");
            re.child(firebaseAuth.getUid()).child("Favourites").child(adId)
                    .setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(context.getApplicationContext(), "Added to favourites!", Toast.LENGTH_SHORT).show();


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(context.getApplicationContext(), "Failed to add favourites"+e.getMessage(), Toast.LENGTH_SHORT).show();


                        }
                    });

        }



    }




    private static void removeFromFavourites(Context context, String adId){


        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null){

            Toast.makeText(context.getApplicationContext(), "You are not logged in!", Toast.LENGTH_SHORT).show();



        } else {


            long timestamp = System.currentTimeMillis();


            HashMap<String, Object> hashMap = new HashMap<>();

            hashMap.put("adID", adId);
            hashMap.put("timestamp", timestamp);

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(firebaseAuth.getUid()).child("Favorites").child(adId)
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(context.getApplicationContext(), "Removed to favourites!", Toast.LENGTH_SHORT).show();


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(context.getApplicationContext(), "Failed to remove from favourites"+e.getMessage(), Toast.LENGTH_SHORT).show();


                        }
                    });

        }


    }






}