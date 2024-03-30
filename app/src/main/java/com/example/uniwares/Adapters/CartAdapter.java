package com.example.uniwares.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uniwares.R;

import java.util.HashMap;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<HashMap<String, String>> cartItems;
    private OnItemRemovedListener listener;

    Context context;

    public CartAdapter(List<HashMap<String, String>> cartItems, OnItemRemovedListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        HashMap<String, String> cartItem = cartItems.get(position);
        holder.bind(cartItem);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public interface OnItemRemovedListener {
        void onItemRemoved(int position);
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImageView;
        TextView itemTitleTextView;
        TextView itemPriceTextView;
        ImageView removeButton;

        CartViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImageView = itemView.findViewById(R.id.postadImage);
            itemTitleTextView = itemView.findViewById(R.id.title);
            itemPriceTextView = itemView.findViewById(R.id.price);
            removeButton = itemView.findViewById(R.id.removefromcart);
        }

        void bind(HashMap<String, String> cartItem) {
            // Bind the data from the HashMap to the views
            itemTitleTextView.setText(cartItem.get("title"));
            itemPriceTextView.setText(cartItem.get("price"));
            // Load the image using a library like Glide or Picasso

            Glide.with(itemView.getContext())
                    .load(cartItem.get("imageUrl"))
                    .into(itemImageView);

//            String imageUrl = cartItem.get("imageUrl");
//            if (imageUrl != null) {
//                Glide.with(itemView.getContext())
//                        .load(imageUrl)
//                        .centerCrop()
//                        .into(itemImageView);
//            }


            removeButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemRemoved(position);
                }
            });
        }
    }
}
