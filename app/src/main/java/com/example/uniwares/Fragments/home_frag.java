package com.example.uniwares.Fragments;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uniwares.Adapters.CategoryAdapter;
import com.example.uniwares.Domain.CategoryDomain;
import com.example.uniwares.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class home_frag extends Fragment {

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewCategoryList;




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



        propic = view.findViewById(R.id.profileimg);


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

            adapter = new CategoryAdapter(categoryList);
            recyclerViewCategoryList.setAdapter(adapter);
        }
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