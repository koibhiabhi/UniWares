package com.example.uniwares.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uniwares.Adapters.OrderAdapter;
import com.example.uniwares.Models.OrderModel;
import com.example.uniwares.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class order extends Fragment {


    public order() {
        // Required empty public constructor
    }
    private RecyclerView ordersRecyclerView;
    private ArrayList<OrderModel> orderList = new ArrayList<>();
    private OrderAdapter orderAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        ordersRecyclerView = view.findViewById(R.id.ordersRecyclerView);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderAdapter = new OrderAdapter(orderList);
        ordersRecyclerView.setAdapter(orderAdapter);

        fetchOrdersFromFirebase();

        return view;
    }

    private void fetchOrdersFromFirebase() {
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("Orders")
                .orderByChild("userUid") // Order the orders by the userUid field
                .equalTo(currentUserUid) // Filter to show orders with the current user's UID
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        orderList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            OrderModel order = snapshot.getValue(OrderModel.class);
                            orderList.add(order);
                        }
                        orderAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle error
                    }
                });
    }
}