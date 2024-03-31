package com.example.uniwares.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uniwares.Models.OrderModel;
import com.example.uniwares.R;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private ArrayList<OrderModel> orderList;

    public OrderAdapter(ArrayList<OrderModel> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card_layout, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderModel order = orderList.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView, priceTextView, sellerNameTextView;
        private ImageView productImageView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title);
            priceTextView = itemView.findViewById(R.id.price);
            sellerNameTextView = itemView.findViewById(R.id.sellerName);
            productImageView = itemView.findViewById(R.id.productImage);
        }

        public void bind(OrderModel order) {
            titleTextView.setText(order.getProductTitle());
            priceTextView.setText(itemView.getContext().getString(R.string.rs_price, order.getPrice()));
            sellerNameTextView.setText(order.getSellerName());
            Glide.with(itemView.getContext())
                    .load(order.getImageUrl())
                    .placeholder(R.drawable.logo)
                    .into(productImageView);
        }
    }
}