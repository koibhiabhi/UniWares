package com.example.uniwares.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
    public void onBindViewHolder(@NonNull HolderImagesPicked holder, int position) {

        ModelImagePicked model = imagePickedArrayList.get(position);

        String imageUri = model.getImageUrl();

        Log.e(TAG, "onBindViewHolder: imageUri"+ imageUri);

        try {
            Glide.with(context)
                    .load(imageUri)
                    .placeholder(R.drawable.image)
                    .into(holder.imageIv);
        }
        catch (Exception e ) {
            Log.e(TAG, "onBindViewHolder", e);
        }



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
