package com.example.uniwares.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.uniwares.Models.ModelImagePicked;
import com.example.uniwares.R;
import com.example.uniwares.databinding.RawImagesPickedBinding;

import java.util.ArrayList;

public class AdapterImagesPicked extends RecyclerView.Adapter<AdapterImagesPicked.HolderImagesPicked>{
    private static final String TAG = "IMAGES_TAG";
    private RawImagesPickedBinding binding;
    private Context context;
    private ArrayList<ModelImagePicked> imagePickedArrayList;

    public AdapterImagesPicked(Context context, ArrayList<ModelImagePicked> imagePickedArrayList) {
        this.context = context;
        this.imagePickedArrayList = imagePickedArrayList;
    }

    @NonNull
    @Override
    public HolderImagesPicked onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RawImagesPickedBinding.inflate(LayoutInflater.from(context), parent, false );
        return new HolderImagesPicked(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderImagesPicked holder, @SuppressLint("RecyclerView") int position) {
        ModelImagePicked model = imagePickedArrayList.get(position);
        Uri imageUri = model.getImageUri();
        Log.d(TAG, "onBindViewHolder: Image URL: " + imageUri); // Log the image URL

        // Load the image using Glide
        Glide.with(context)
                .load(imageUri)
                .placeholder(R.drawable.image) // Placeholder image while loading
                .error(R.drawable.image)       // Error image if loading fails
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e(TAG, "onLoadFailed: Error loading image", e); // Log any errors
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.imageIv);

        holder.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePickedArrayList.remove(model);
                notifyItemRemoved(position);
            }
        });
    }
    @Override
    public int getItemCount() {
        return imagePickedArrayList.size();
    }
    class HolderImagesPicked extends RecyclerView.ViewHolder {
        ImageView imageIv;
        ImageButton closeBtn;
        public HolderImagesPicked(@NonNull View itemView) {
            super(itemView);
            imageIv = binding.imageIv;
            closeBtn = binding.closeBtn;
        }
    }
}
