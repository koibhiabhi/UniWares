package com.example.uniwares.Adapters;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uniwares.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class AdAdapterListing extends RecyclerView.Adapter<AdAdapterListing.AdViewHolder> {

    private ArrayList<HashMap<String, Object>> adsList;

    public AdAdapterListing(ArrayList<HashMap<String, Object>> adsList) {
        this.adsList = adsList;
    }

    @NonNull
    @Override
    public AdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myads_card, parent, false);
        return new AdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdViewHolder holder, int position) {
        HashMap<String, Object> adMap = adsList.get(position);
        holder.bind(adMap);
    }

    @Override
    public int getItemCount() {
        return adsList.size();
    }

    class AdViewHolder extends RecyclerView.ViewHolder {
        private ImageView postadImage, deletead;
        private TextView title, price, dateadded;

        public AdViewHolder(@NonNull View itemView) {
            super(itemView);
            postadImage = itemView.findViewById(R.id.postadImage);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            dateadded = itemView.findViewById(R.id.dateadded);
            deletead = itemView.findViewById(R.id.deletead);
        }

        public void bind(HashMap<String, Object> adMap) {
            title.setText((String) adMap.get("title"));
            price.setText((String) adMap.get("price"));
            long timestamp = (long) adMap.get("timestamp");
            String formattedDate = convertTimestampToDate(timestamp);
            dateadded.setText(formattedDate);

            // Fetch image URLs from Firebase
            String adId = (String) adMap.get("adId");
            fetchImageUrls(adId, new OnImageUrlsLoadedListener() {
                @Override
                public void onImageUrlsLoaded(ArrayList<String> imageUrls) {
                    if (!imageUrls.isEmpty()) {
                        Glide.with(itemView.getContext())
                                .load(imageUrls.get(0))
                                .into(postadImage);
                    }
                }

                @Override
                public void onImageUrlsLoadFailed(String errorMessage) {
                    Toast.makeText(itemView.getContext(), "Failed to load image: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });

            // Delete ad on click
            deletead.setOnClickListener(v -> {
                deleteAd(adId);
            });
        }

        private String convertTimestampToDate(long timestamp) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
            return dateFormat.format(new Date(timestamp));
        }

        private void fetchImageUrls(String adId, OnImageUrlsLoadedListener listener) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            if (firebaseAuth.getCurrentUser() != null) {
                String uid = firebaseAuth.getCurrentUser().getUid();
                DatabaseReference imagesRef = FirebaseDatabase.getInstance().getReference("Ads").child(uid).child(adId).child("Images");
                imagesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<String> imageUrls = new ArrayList<>();
                        for (DataSnapshot imageSnapshot : snapshot.getChildren()) {
                            String imageUrl = imageSnapshot.child("image").getValue(String.class);
                            if (imageUrl != null) {
                                imageUrls.add(imageUrl);
                            }
                        }
                        listener.onImageUrlsLoaded(imageUrls);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onImageUrlsLoadFailed(error.getMessage());
                    }
                });
            }
        }

        private void deleteAd(String adId) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            if (firebaseAuth.getCurrentUser() != null) {
                String uid = firebaseAuth.getCurrentUser().getUid();
                int position = getAdapterPosition();
                Log.d("AdAdapter", "Deleting ad at position: " + position + ", list size: " + adsList.size());
                DatabaseReference adRef = FirebaseDatabase.getInstance().getReference("Ads").child(uid).child(adId);
                adRef.removeValue()
                        .addOnSuccessListener(aVoid -> {
                            new Handler().postDelayed(() -> {
                                if (position != RecyclerView.NO_POSITION && position < adsList.size()) {
                                    adsList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, adsList.size());
                                    Toast.makeText(itemView.getContext(), "Ad deleted successfully", Toast.LENGTH_SHORT).show();
                                }
                            }, 100); // Delay in milliseconds
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(itemView.getContext(), "Failed to delete ad: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        }
    }
    public interface OnImageUrlsLoadedListener {
        void onImageUrlsLoaded(ArrayList<String> imageUrls);
        void onImageUrlsLoadFailed(String errorMessage);
    }
}
