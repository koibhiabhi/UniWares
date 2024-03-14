package com.example.uniwares.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.uniwares.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class redeemcoins extends Fragment {

    private TextView coinsTextView;

    CardView c1;

    public redeemcoins() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_redeemcoins, container, false);

        coinsTextView = view.findViewById(R.id.coinsTextView);
        c1 = view.findViewById(R.id.touchCard);

        DatabaseReference coinsRef = FirebaseDatabase.getInstance().getReference("rewards").child(FirebaseAuth.getInstance().getUid()).child("totalCoins");
        coinsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    long coins = (long) dataSnapshot.getValue();
                    coinsTextView.setText("Coins: " + coins);

                    c1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (coins >= 250) {
                                Toast.makeText(getContext(), "Redirecting to redeem page", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Not enough coins", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });

        return view;
    }
}

