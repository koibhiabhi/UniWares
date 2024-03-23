package com.example.uniwares.Fragments;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
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

public class home_frag extends Fragment implements CategoryAdapter.OnCategoryClickListener {

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewCategoryList, recyclerViewRecentAds;
    private EditText search;
    private String searchText = "";

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

        ArrayList<CategoryDomain> categoryList = new ArrayList<>();

        adapter = new CategoryAdapter(categoryList);
        recyclerViewCategoryList.setAdapter(adapter);

        recyclerViewCategoryList(view.getContext());

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

        propic = view.findViewById(R.id.profileimg);

        Button splash = view.findViewById(R.id.dukaan);
        splash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                // Inflate the custom layout for the dialog content
                LayoutInflater inflater = requireActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.welcomeuniware, null);
                builder.setView(dialogView);

                // Set the background of the dialog's window to be transparent
                AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // Set other properties of the dialog if needed
                builder.setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                // Show the AlertDialog
                dialog.show();
            }
        });

        db.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        //taking image in the setting, isse setting mein bhi dikhegi image
                        String profilePicUrl = snapshot.child("profilepic").getValue(String.class);

                        Picasso.get()
                                .load(profilePicUrl)
                                .placeholder(R.drawable.avatar)
                                .into(propic);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        TextView welcomename = view.findViewById(R.id.welcomename);

        db.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Get user information
                        String name = snapshot.child("username").getValue(String.class);
                        welcomename.setText(name != null ? name : "Null!");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                        Log.e(TAG, "Failed to fetch user information: " + error.getMessage());
                        Toast.makeText(getContext(), "Failed to fetch user information", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        recyclerViewRecentAds = view.findViewById(R.id.recylerViewrecentlyadd);

        TextView t1 = view.findViewById(R.id.textView17);
        CardView cv = view.findViewById(R.id.mainLayout);
        ConstraintLayout constraintLayout = view.findViewById(R.id.constraintLayout);
        TextView t  = view.findViewById(R.id.textView13);
        Button b = view.findViewById(R.id.dukaan);
        CardView c = view.findViewById(R.id.card);
        LottieAnimationView l = view.findViewById(R.id.anibag);

        int originalTopMargin;
        int originalTopToTop;
        int originalBottomToBottom;

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) recyclerViewRecentAds.getLayoutParams();
        originalTopMargin = params.topMargin;
        originalTopToTop = params.topToTop;
        originalBottomToBottom = params.bottomToBottom;

        search = view.findViewById(R.id.editTextText);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchText = s.toString();
                fetchRecentAds(searchText);

                if (!searchText.isEmpty()) {
                    // Hide other views
                    recyclerViewCategoryList.setVisibility(View.GONE);
                    b.setVisibility(View.GONE);
                    constraintLayout.setVisibility(View.GONE);
                    t.setVisibility(View.GONE);
                    c.setVisibility(View.GONE);
                    l.setVisibility(View.GONE);
                    t1.setVisibility(View.GONE);
                    cv.setVisibility(View.GONE);

                    // Set top margin of recyclerViewRecentAds
                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) recyclerViewRecentAds.getLayoutParams();
                    params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;  // Set top constraint to parent's top
                    params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;  // Set bottom constraint to parent's bottom
                    params.topMargin = 380;  // Set top margin to 380dp
                    recyclerViewRecentAds.setLayoutParams(params);

                } else {
                    // Show other views
                    recyclerViewCategoryList.setVisibility(View.VISIBLE);
                    b.setVisibility(View.VISIBLE);
                    constraintLayout.setVisibility(View.VISIBLE);
                    t.setVisibility(View.VISIBLE);
                    c.setVisibility(View.VISIBLE);
                    l.setVisibility(View.VISIBLE);
                    t1.setVisibility(View.VISIBLE);
                    cv.setVisibility(View.VISIBLE);

                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) recyclerViewRecentAds.getLayoutParams();
                    params.topToTop = originalTopToTop;
                    params.bottomToBottom = originalBottomToBottom;
                    params.topMargin = originalTopMargin;
                    recyclerViewRecentAds.setLayoutParams(params);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        fetchRecentAds(searchText);

        return view;
    }

    public void recyclerViewCategoryList(Context context) {
        if (recyclerViewCategoryList != null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            recyclerViewCategoryList.setLayoutManager(linearLayoutManager);

            ArrayList<CategoryDomain> categoryList = new ArrayList<>();
            for (String categoryName : categories) {
                categoryList.add(new CategoryDomain(categoryName, "book_1"));
            }

            adapter = new CategoryAdapter(categoryList, this); // 'this' refers to the OnCategoryClickListener implemented in home_frag
            recyclerViewCategoryList.setAdapter(adapter);
        }
    }

    //working search bar

    private void fetchRecentAds(String searchText) {
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

                                            // Filter ads based on search query
                                            if (title.toLowerCase().contains(searchText.toLowerCase())) {
                                                HashMap<String, String> adDetails = new HashMap<>();
                                                adDetails.put("postId", postId);
                                                adDetails.put("title", title);
                                                adDetails.put("price", price);
                                                adDetails.put("addedBy", username);
                                                adDetails.put("timestamp", timestamp.toString());
                                                adDetails.put("username", username);
                                                adDetails.put("imageUrl", imageUrl);

                                                allAds.add(adDetails);
                                            }

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

    @Override
    public void onCategoryClick(String category) {
        // Load explore_frag with the selected category
        loadFragment(new explore_frag(category));
    }

    private void loadFragment(Fragment fragment) {
        // Replace the current fragment with the new fragment
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }
}