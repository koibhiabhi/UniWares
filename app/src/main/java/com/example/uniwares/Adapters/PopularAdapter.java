package com.example.uniwares.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.uniwares.Fragments.AdDetail;
import com.example.uniwares.R;
import com.example.uniwares.databinding.ViewholderPopListBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.Viewholder> {
    ArrayList<HashMap<String, String>> items;
    Context context;

    public PopularAdapter(ArrayList<HashMap<String, String>> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public PopularAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderPopListBinding binding = ViewholderPopListBinding.inflate(LayoutInflater.from(context), parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularAdapter.Viewholder holder, int position) {
        HashMap<String, String> item = items.get(position);

        holder.binding.title.setText(item.get("title"));
        holder.binding.sellername.setText(item.get("username"));
        holder.binding.priceTxt.setText(item.get("price"));





        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop());

        // Set image (if you have image URLs in your HashMap)
         Glide.with(context)
                 .load(item.get("imageUrl"))
                 .apply(requestOptions)
                 .into(holder.binding.imageView3);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle item click
                Fragment adDetailFragment = new AdDetail();
                Bundle bundle = new Bundle();
                bundle.putString("title", item.get("title"));
                bundle.putString("username", item.get("username"));
                bundle.putString("price", item.get("price"));
                bundle.putString("imageUrl", item.get("imageUrl"));
                String imageUrlString = item.get("imageUrls");
                if (imageUrlString != null) {
                    ArrayList<String> imageUrls = new ArrayList<>(Arrays.asList(imageUrlString.split(",")));
                    bundle.putStringArrayList("imageUrls", imageUrls);
                }
                bundle.putString("description", item.get("description")); // Add description
                bundle.putString("category", item.get("category")); // Add category
                bundle.putString("condition", item.get("condition")); // Add condition
                bundle.putString("brand", item.get("brand")); // Add brand
                bundle.putString("status", item.get("status")); // Add status
                adDetailFragment.setArguments(bundle);
                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, adDetailFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ViewholderPopListBinding binding;

        public Viewholder(ViewholderPopListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
