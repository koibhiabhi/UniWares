package com.example.uniwares.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uniwares.Adapters.CartAdapter;
import com.example.uniwares.R;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class cart_frag extends Fragment implements CartAdapter.OnItemRemovedListener {

    private RecyclerView cartRecyclerView;
    private TextView totalSumTextView;
    private TextView emptyTextView;
    private List<HashMap<String, String>> cartItems;
    private CartAdapter cartAdapter;

    private static final String CART_ITEMS_KEY = "cart_items";

    public cart_frag() {
        cartItems = new ArrayList<>(); // Initialize cartItems
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart_frag, container, false);

        ImageView closeButton = view.findViewById(R.id.closecartBtn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Load cart items from SharedPreferences
        loadCartItems();

        cartRecyclerView = view.findViewById(R.id.cartView);
        totalSumTextView = view.findViewById(R.id.totalsum);
        emptyTextView = view.findViewById(R.id.emptyText);

        cartAdapter = new CartAdapter(cartItems, this);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartRecyclerView.setAdapter(cartAdapter);

        updateTotalSum();
        updateCartVisibility();

        return view;
    }


    private void updateTotalSum() {
        if (cartItems != null) {
            int totalSum = 0;
            for (HashMap<String, String> cartItem : cartItems) {
                totalSum += Integer.parseInt(cartItem.get("price"));
            }
            totalSumTextView.setText("Rs. " + totalSum);
        }
    }

    private void updateCartVisibility() {
        if (cartItems != null) {
            if (cartItems.isEmpty()) {
                emptyTextView.setVisibility(View.VISIBLE);
                cartRecyclerView.setVisibility(View.GONE);
                totalSumTextView.setVisibility(View.GONE);
            } else {
                emptyTextView.setVisibility(View.GONE);
                cartRecyclerView.setVisibility(View.VISIBLE);
                totalSumTextView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void addItemToCart(HashMap<String, String> cartItem) {
        cartItems.add(cartItem);
        cartAdapter.notifyItemInserted(cartItems.size() - 1);
        updateTotalSum();
        updateCartVisibility();
        saveCartItems();
    }

    @Override
    public void onItemRemoved(int position) {
        cartItems.remove(position);
        cartAdapter.notifyItemRemoved(position);
        updateTotalSum();
        updateCartVisibility();
        saveCartItems();
    }

    private void saveCartItems() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("cart_items", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(cartItems);
        editor.putString("cart_items", json);
        editor.apply();
    }

    private void loadCartItems() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("cart_items", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("cart_items", "");
        Type type = new TypeToken<List<HashMap<String, String>>>() {}.getType();
        cartItems = new Gson().fromJson(json, type);

        if (cartItems == null) {
            cartItems = new ArrayList<>(); // Initialize cartItems if null
        }
    }

    public int getCartItemsCount() {
        return cartItems.size();

    }
}
