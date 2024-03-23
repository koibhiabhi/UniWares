package com.example.uniwares.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uniwares.Domain.CategoryDomain;
import com.example.uniwares.Fragments.explore_frag;
import com.example.uniwares.R;

import java.util.ArrayList;





public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    ArrayList<CategoryDomain>categoryDomains;


    //
    private Context context;


    public CategoryAdapter(ArrayList<CategoryDomain> category) {
        this.categoryDomains = category;

        //
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category,parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.categoryName.setText(categoryDomains.get(position).getTitle());
        String picUrl = "";
        switch(position){
            case 0:{
                picUrl = "bookscat";
                holder.mainLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.category_background));
                break;
            }
            case 1:{
                picUrl = "mobilecat";
                holder.mainLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.category_background));
                break;
            }
            case 2:{
                picUrl = "clothescat";
                holder.mainLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.category_background));
                break;
            }
            case 3:{
                picUrl = "sportscat";
                holder.mainLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.category_background));
                break;
            }
            case 4:{
                picUrl = "electcat";
                holder.mainLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.category_background));
                break;
            }
            case 5:{
                picUrl = "laptopcat";
                holder.mainLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.category_background));
                break;
            }
            case 6:{
                picUrl = "othercat";
                holder.mainLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.category_background));

                break;
            }
        }
        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(picUrl, "drawable",holder.itemView.getContext().getPackageName());
        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .into(holder.categoryPic);



        //
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the selected category to explore_frag
                String selectedCategory = categoryDomains.get(position).getTitle();
                listener.onCategoryClick(selectedCategory);
            }
        });

    }


    @Override
    public int getItemCount() {
        return categoryDomains.size();
    }

    public void setCategoryDomains(ArrayList<CategoryDomain> categories) {
        this.categoryDomains = categories;
        notifyDataSetChanged();
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(String category);
    }
    private OnCategoryClickListener listener;

    public CategoryAdapter(ArrayList<CategoryDomain> category, OnCategoryClickListener listener) {
        this.categoryDomains = category;
        this.listener = listener;
    }





    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView categoryName;
        ImageView categoryPic;
        ConstraintLayout mainLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            categoryPic = itemView.findViewById(R.id.categoryPic);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
