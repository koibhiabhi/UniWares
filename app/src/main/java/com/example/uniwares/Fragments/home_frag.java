package com.example.uniwares.Fragments;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uniwares.Adapters.CategoryAdapter;
import com.example.uniwares.Adapters.RecentlyAddedAdsAdapter;
import com.example.uniwares.Domain.CategoryDomain;
import com.example.uniwares.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;


public class home_frag extends Fragment {

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewCategoryList, recyclerViewRecentAds;

    public home_frag() {
        // Required empty public constructor
    }

    FirebaseDatabase db = FirebaseDatabase.getInstance();

    ImageView propic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_frag, container, false);

        recyclerViewCategoryList = view.findViewById(R.id.recyclerView);

        recyclerViewCategoryList(view.getContext());

        propic = view.findViewById(R.id.profileimg);


        CardView cardView = view.findViewById(R.id.mainLayout);

        // Set an OnClickListener for the CardView
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new instance of transact_frag
                transact_frag transactFragment = new transact_frag();

                // Replace the current fragment with transact_frag
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new explore_frag())
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Load user profile picture
        db.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String profilePicUrl = snapshot.child("profilepic").getValue(String.class);
                        Picasso.get()
                                .load(profilePicUrl)
                                .placeholder(R.drawable.avatar)
                                .into(propic);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "Failed to fetch user information: " + error.getMessage());
                        Toast.makeText(getContext(), "Failed to fetch user information", Toast.LENGTH_SHORT).show();
                    }
                });

        TextView welcomename = view.findViewById(R.id.welcomename);

        // Display user's name
        db.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = snapshot.child("username").getValue(String.class);
                        welcomename.setText(name != null ? name : "Null!");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "Failed to fetch user information: " + error.getMessage());
                        Toast.makeText(getContext(), "Failed to fetch user information", Toast.LENGTH_SHORT).show();
                    }
                });

        // Assuming recyclerViewRecentAds is your RecyclerView
        recyclerViewRecentAds = view.findViewById(R.id.recylerViewrecentlyadd); // Make sure to find the correct view by its ID

        fetchRecentAds();

        return view;
    }

    public void recyclerViewCategoryList(Context context) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategoryList.setLayoutManager(linearLayoutManager);

        ArrayList<CategoryDomain> categoryList = new ArrayList<>();
        for (String categoryName : categories) {
            categoryList.add(new CategoryDomain(categoryName, "book_1"));
        }

        adapter = new CategoryAdapter(categoryList);
        recyclerViewCategoryList.setAdapter(adapter);
    }










//    private void fetchRecentAds() {
//        db.getReference().child("Ads").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    ArrayList<HashMap<String, String>> allAds = new ArrayList<>();
//                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
//                        for (DataSnapshot adSnapshot : userSnapshot.getChildren()) {
//                            String postId = adSnapshot.getKey();
//                            String title = adSnapshot.child("title").getValue(String.class);
//                            String price = adSnapshot.child("price").getValue(String.class);
//                            String addedBy = adSnapshot.child("uid").getValue(String.class);
//                            Long timestamp = adSnapshot.child("timestamp").getValue(Long.class);
//
//                            if (title != null && price != null && addedBy != null && timestamp != null) {
//                                DatabaseReference userRef = db.getReference().child("Users").child(addedBy);
//                                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot userSnapshot) {
//                                        if (userSnapshot.exists()) {
//                                            String username = userSnapshot.child("username").getValue(String.class);
//                                            HashMap<String, String> adDetails = new HashMap<>();
//                                            adDetails.put("postId", postId);
//                                            adDetails.put("title", title);
//                                            adDetails.put("price", price);
//                                            adDetails.put("addedBy", username);
//                                            adDetails.put("timestamp", timestamp.toString());
//                                            adDetails.put("username", username);
//
//                                            allAds.add(adDetails);
//
//                                            // Sort the ads based on timestamp
//                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                                allAds.sort(new Comparator<HashMap<String, String>>() {
//                                                    @Override
//                                                    public int compare(HashMap<String, String> adMap1, HashMap<String, String> adMap2) {
//                                                        return Long.compare(Long.parseLong(adMap2.get("timestamp")), Long.parseLong(adMap1.get("timestamp")));
//                                                    }
//                                                });
//                                            }
//
//                                            // Get the last 7 ads from the sorted list
//                                            ArrayList<HashMap<String, String>> recentlyAddedAdsList = new ArrayList<>(allAds.subList(0, Math.min(allAds.size(), 7)));
//
//                                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
//                                            recyclerViewRecentAds.setLayoutManager(layoutManager);
//                                            RecentlyAddedAdsAdapter adapter = new RecentlyAddedAdsAdapter(getContext(), recentlyAddedAdsList);
//                                            recyclerViewRecentAds.setAdapter(adapter);
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//                                        // Handle cancelled event
//                                    }
//                                });
//                            } else {
//                                Log.w(TAG, "Null value encountered for ad with postId: " + postId);
//                            }
//                        }
//                    }
//                } else {
//                    Log.d(TAG, "No ads found");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle cancelled event
//            }
//        });
//    }



















    private void fetchRecentAds() {
        db.getReference().child("Ads").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<HashMap<String, String>> allAds = new ArrayList<>();
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot adSnapshot : userSnapshot.getChildren()) {
                            String postId = adSnapshot.getKey();
                            String title = adSnapshot.child("title").getValue(String.class);
                            String price = adSnapshot.child("price").getValue(String.class);
                            String addedBy = adSnapshot.child("uid").getValue(String.class);
                            Long timestamp = adSnapshot.child("timestamp").getValue(Long.class);

                            if (title != null && price != null && addedBy != null && timestamp != null) {
                                DatabaseReference userRef = db.getReference().child("Users").child(addedBy);
                                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                        if (userSnapshot.exists()) {
                                            String username = userSnapshot.child("username").getValue(String.class);

                                            // Fetch image URL
                                            String imageUrl = null;
                                            DataSnapshot imagesSnapshot = adSnapshot.child("Images");
                                            for (DataSnapshot imageSnapshot : imagesSnapshot.getChildren()) {
                                                imageUrl = imageSnapshot.child("image").getValue(String.class);
                                                break; // Break after retrieving the first image URL
                                            }

                                            HashMap<String, String> adDetails = new HashMap<>();
                                            adDetails.put("postId", postId);
                                            adDetails.put("title", title);
                                            adDetails.put("price", price);
                                            adDetails.put("addedBy", username);
                                            adDetails.put("timestamp", timestamp.toString());
                                            adDetails.put("username", username);
                                            adDetails.put("imageUrl", imageUrl);

                                            allAds.add(adDetails);

                                            // Sort the ads based on timestamp
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                allAds.sort(new Comparator<HashMap<String, String>>() {
                                                    @Override
                                                    public int compare(HashMap<String, String> adMap1, HashMap<String, String> adMap2) {
                                                        return Long.compare(Long.parseLong(adMap2.get("timestamp")), Long.parseLong(adMap1.get("timestamp")));
                                                    }
                                                });
                                            }

                                            // Get the last 7 ads from the sorted list
                                            ArrayList<HashMap<String, String>> recentlyAddedAdsList = new ArrayList<>(allAds.subList(0, Math.min(allAds.size(), 7)));

                                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                                            recyclerViewRecentAds.setLayoutManager(layoutManager);
                                            RecentlyAddedAdsAdapter adapter = new RecentlyAddedAdsAdapter(getContext(), recentlyAddedAdsList);
                                            recyclerViewRecentAds.setAdapter(adapter);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Handle cancelled event
                                    }
                                });
                            } else {
                                Log.w(TAG, "Null value encountered for ad with postId: " + postId);
                            }
                        }
                    }
                } else {
                    Log.d(TAG, "No ads found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle cancelled event
            }
        });
    }
































    public static final String[] categories = {
            "Books",
            "Mobiles",
            "Clothes",
            "Sports",
            "Electronics & Appliances",
            "Computer/Laptop",
            "Others"
    };
}