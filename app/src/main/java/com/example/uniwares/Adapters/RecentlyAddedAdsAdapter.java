package com.example.uniwares.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uniwares.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class RecentlyAddedAdsAdapter extends RecyclerView.Adapter<RecentlyAddedAdsAdapter.ViewHolder> {

    private Context context;
    private List<HashMap<String, String>> adList;

    public RecentlyAddedAdsAdapter(Context context, List<HashMap<String, String>> adList) {
        this.context = context;
        this.adList = adList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_recentlyadded, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String, String> ad = adList.get(position);

        // Bind data to views
        holder.titleTextView.setText(ad.get("title"));
        holder.priceTextView.setText(ad.get("price"));
        holder.addedByTextView.setText(ad.get("addedBy"));

        // Load image using Picasso or any other image loading library
        Picasso.get()
                .load(ad.get("imageUrl"))
                .placeholder(R.drawable.bookscat)
                .error(R.drawable.logo) // Optional: Set error placeholder image
                .into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        // Image loaded successfully
                    }

                    @Override
                    public void onError(Exception e) {
                        // Error occurred while loading image
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return adList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView priceTextView;
        TextView addedByTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.postadImage);
            titleTextView = itemView.findViewById(R.id.title);
            priceTextView = itemView.findViewById(R.id.price);
            addedByTextView = itemView.findViewById(R.id.addedby);
        }
    }
}
