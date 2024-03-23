package com.example.uniwares.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uniwares.Adapters.AdAdapterListing;
import com.example.uniwares.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class listing extends Fragment {

    public listing() {
        // Required empty public constructor
    }

    private RecyclerView recyclerViewListing;
    private ArrayList<HashMap<String, Object>> adsList;
    private AdAdapterListing adAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listing, container, false);

        recyclerViewListing = view.findViewById(R.id.recyclerViewListing);
        recyclerViewListing.setLayoutManager(new LinearLayoutManager(getContext()));
        adsList = new ArrayList<>();
        adAdapter = new AdAdapterListing(adsList);
        recyclerViewListing.setAdapter(adAdapter);

        loadUserAds();

        return view;
    }

    private void loadUserAds() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            String uid = firebaseAuth.getCurrentUser().getUid();
            DatabaseReference userAdsRef = FirebaseDatabase.getInstance().getReference("Ads").child(uid);
            userAdsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    adsList.clear();
                    for (DataSnapshot adSnapshot : snapshot.getChildren()) {
                        HashMap<String, Object> adMap = (HashMap<String, Object>) adSnapshot.getValue();
                        if (adMap != null) {
                            adMap.put("adId", adSnapshot.getKey()); // Add the adId to the HashMap
                            adsList.add(adMap);
                        }
                    }
                    adAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Failed to load ads: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}