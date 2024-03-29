package com.example.uniwares.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.uniwares.Adapters.PopularAdapter;
import com.example.uniwares.databinding.FragmentExploreFragBinding;
import com.google.firebase.auth.FirebaseAuth;
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


    //
    private String selectedCategory;

    private ArrayList<HashMap<String, String>> adsList = new ArrayList<>(); // Define adsList at the class level


    public explore_frag(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }



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

        binding.textView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });

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
                            String category = adSnapshot.child("category").getValue(String.class);
                            if (selectedCategory == null || selectedCategory.isEmpty() || (category != null && category.equals(selectedCategory))) {
                                String title = adSnapshot.child("title").getValue(String.class);
                                String uid = adSnapshot.child("uid").getValue(String.class);
                                String price = adSnapshot.child("price").getValue(String.class);
                                String description = adSnapshot.child("description").getValue(String.class); // Add description
                                String condition = adSnapshot.child("condition").getValue(String.class); // Add condition
                                String brand = adSnapshot.child("brand").getValue(String.class); // Add brand
                                String status = adSnapshot.child("status").getValue(String.class); // Add status
                                long timestamp = adSnapshot.child("timestamp").getValue(Long.class); // Retrieve timestamp


                                // Check if the ad is posted by the current user
                                if (!uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
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
                                                ad.put("description", description); // Add description
                                                ad.put("category", category); // Add category
                                                ad.put("condition", condition); // Add condition
                                                ad.put("brand", brand); // Add brand
                                                ad.put("status", status); // Add status
                                                ad.put("timestamp", String.valueOf(timestamp)); // Add timestamp to HashMap
                                                // Add other fields to the HashMap

                                                adsList.add(ad);
                                            }

                                            // Notify RecyclerView adapter when data changes
                                            if (!adsList.isEmpty()) {
                                                binding.recyclerViewPopular.setLayoutManager(new GridLayoutManager(requireContext(), 2));
                                                binding.recyclerViewPopular.setAdapter(new PopularAdapter(adsList));
                                            }
                                            binding.progressBarPopular.setVisibility(View.GONE);
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
                }

                // Notify RecyclerView adapter when data changes
                if (!adsList.isEmpty()) {
                    binding.recyclerViewPopular.setLayoutManager(new GridLayoutManager(requireContext(), 2));
                    binding.recyclerViewPopular.setAdapter(new PopularAdapter(adsList));
                }
                binding.progressBarPopular.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle cancelled event
            }
        });

        // Set up search functionality
        binding.editTextText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchText = s.toString().toLowerCase().trim();

                if (!searchText.isEmpty()) {
                    ArrayList<HashMap<String, String>> filteredAdsList = new ArrayList<>();

                    for (HashMap<String, String> ad : adsList) {
                        if (ad.get("title").toLowerCase().contains(searchText)) {
                            filteredAdsList.add(ad);
                        }
                    }

                    binding.recyclerViewPopular.setLayoutManager(new GridLayoutManager(requireContext(), 2));
                    binding.recyclerViewPopular.setAdapter(new PopularAdapter(filteredAdsList));
                } else {
                    // If search text is empty, show all ads
                    binding.recyclerViewPopular.setLayoutManager(new GridLayoutManager(requireContext(), 2));
                    binding.recyclerViewPopular.setAdapter(new PopularAdapter(adsList));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void showFilterDialog() {
        // Create and show the dialog with sort options/filters
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setItems(new String[]{"Recently Added", "Price Low to High", "Price High to Low"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // Sort by Recently Added
                        Toast.makeText(getContext(), "Recently Added", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        // Sort by Price Low to High
                        Toast.makeText(getContext(), "Low to high", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        // Sort by Price High to Low
                        Toast.makeText(getContext(), "High to Low", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        builder.show();
    }




}