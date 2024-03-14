package com.example.uniwares.Adapters;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.uniwares.R;

public class InfinitePagerAdapter extends RecyclerView.Adapter<InfinitePagerAdapter.ViewHolder> {

    private Context context;
    private int[] images = {R.drawable.charity, R.drawable.charity2, R.drawable.charity3, R.drawable.charity4, R.drawable.charity5};
    private ViewPager2 viewPager;
    private int currentPosition = 0;

    public InfinitePagerAdapter(Context context, ViewPager2 viewPager) {
        this.context = context;
        this.viewPager = viewPager;
        startAutoScroll();
    }

    private void startAutoScroll() {
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            public void run() {
                if (currentPosition == Integer.MAX_VALUE) {
                    currentPosition = 0;
                }
                viewPager.setCurrentItem(currentPosition++, true);
                handler.postDelayed(this, 2000); // Auto-scroll interval in milliseconds (3 seconds here)
            }
        };
        handler.postDelayed(update, 2000); // Initial delay before the auto-scroll starts
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(images[position % images.length]);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
