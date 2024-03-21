package com.example.uniwares.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.uniwares.Adapters.PopularAdapter;
import com.example.uniwares.R;
import com.example.uniwares.databinding.FragmentExploreFragBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class explore_frag extends Fragment {
    private FragmentExploreFragBinding binding;
    private DatabaseReference databaseReference;

    public explore_frag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentExploreFragBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        initPopular();
        return rootView;
    }

    private void initPopular() {
        DatabaseReference adsRef = databaseReference.child("Ads");
        binding.progressBarPopular.setVisibility(View.VISIBLE);
        ArrayList<HashMap<String, String>> adsList = new ArrayList<>();

        adsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot adSnapshot : userSnapshot.getChildren()) {
                            String title = adSnapshot.child("title").getValue(String.class);
                            String uid = adSnapshot.child("uid").getValue(String.class);
                            String price = adSnapshot.child("price").getValue(String.class);
                            // Retrieve other fields as needed

                            // Fetch user's name based on the UID
                            DatabaseReference userRef = databaseReference.child("Users").child(uid);
                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                    if (userSnapshot.exists()) {
                                        String username = userSnapshot.child("username").getValue(String.class);

                                        // Retrieve image URL
                                        String imageUrl = null;
                                        DataSnapshot imagesSnapshot = adSnapshot.child("Images");
                                        for (DataSnapshot imageSnapshot : imagesSnapshot.getChildren()) {
                                            imageUrl = imageSnapshot.child("image").getValue(String.class);
                                            break; // Break after retrieving the first image URL
                                        }

                                        // Add ad details to HashMap
                                        HashMap<String, String> ad = new HashMap<>();
                                        ad.put("title", title);
                                        ad.put("price", price);
                                        ad.put("username", username);
                                        ad.put("imageUrl", imageUrl); // Add image URL
                                        // Add other fields to the HashMap

                                        adsList.add(ad);

                                        // Notify RecyclerView adapter when data changes
                                        if (!adsList.isEmpty()) {
                                            binding.recyclerViewPopular.setLayoutManager(new GridLayoutManager(requireContext(), 2));
                                            binding.recyclerViewPopular.setAdapter(new PopularAdapter(adsList));
                                        }
                                        binding.progressBarPopular.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Handle cancelled event
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle cancelled event
            }
        });
    }
}