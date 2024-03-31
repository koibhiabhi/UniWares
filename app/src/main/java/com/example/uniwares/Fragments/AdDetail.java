package com.example.uniwares.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.uniwares.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class AdDetail extends Fragment {

    private static final String TAG = "MAIN";
    private String title;
    private String price;
    private String username;
    private ArrayList<String> imageUrls; // Updated to ArrayList

    private String description;
    private String category;
    private String condition;
    private String brand;
    private String status;
    private long timestamp; // Add timestamp field
    private String uid;

    String adId;




    public AdDetail() {
        // Required empty public constructor
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            adId = getArguments().getString("adId");
            title = getArguments().getString("title");
            price = getArguments().getString("price");
            uid = getArguments().getString("uid");
            username = getArguments().getString("username");
            String imageUrlString = getArguments().getString("imageUrl");
            if (imageUrlString != null) {
                imageUrls = new ArrayList<>(Arrays.asList(imageUrlString.split(",")));
            } // Updated to use member variable
            description = getArguments().getString("description");
            category = getArguments().getString("category");
            condition = getArguments().getString("condition");
            brand = getArguments().getString("brand");
            status = getArguments().getString("status");
            timestamp = getArguments().getLong("timestamp"); // Retrieve timestamp

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_ad_detail, container, false);

        ImageView closeButton = view.findViewById(R.id.closeBtn);

        // Set click listener for the close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the previous fragment
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });


        ImageView cartImageView = view.findViewById(R.id.cart);

        // Set click listener for the cart ImageView
        cartImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the cart fragment
                cart_frag cartFragment = new cart_frag();
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, cartFragment); // Replace fragment_container with your fragment container id
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        Button addToCartButton = view.findViewById(R.id.addtocart);
        addToCartButton.setOnClickListener(v -> {
            HashMap<String, String> cartItem = new HashMap<>();
            Log.d(TAG,"On AdIdSuccessful: "+adId );
            cartItem.put("adId",  adId);
            Log.d(TAG,"On AdIdSuccessful: "+adId  );
            cartItem.put("title", title);
            cartItem.put("price", price);
            cartItem.put("imageUrl", imageUrls.get(0)); // Assuming you want to use the first image URL
            cartItem.put("description", description);
            cartItem.put("category", category);
            cartItem.put("condition", condition);
            cartItem.put("brand", brand);
            cartItem.put("status", status);


            // Get an instance of the cart_frag and call the addItemToCart method
            cart_frag cartFragment = new cart_frag();
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, cartFragment, "cart_fragment"); // Replace fragment_container with your fragment container id
            transaction.addToBackStack(null);
            transaction.commit();

            // Wait for the transaction to be completed before adding the item to the cart
            requireActivity().getSupportFragmentManager().executePendingTransactions();

            cartFragment.addItemToCart(cartItem);


        });







        TextView titleTextView = view.findViewById(R.id.text_ad_title);
        TextView priceTextView = view.findViewById(R.id.text_ad_price);
        TextView usernameTextView = view.findViewById(R.id.textAdSellerName);
        TextView descriptionTextView = view.findViewById(R.id.text_ad_description);
        TextView categoryTextView = view.findViewById(R.id.text_ad_category);
        TextView conditionTextView = view.findViewById(R.id.text_ad_condition);
        TextView brandTextView = view.findViewById(R.id.text_ad_brand);
        TextView statusTextView = view.findViewById(R.id.text_ad_status);

        TextView productnamehead = view.findViewById(R.id.productnamehead);
        productnamehead.setText(title);

        TextView timestampTextView = view.findViewById(R.id.textAdTimestamp);
        timestampTextView.setText(formatTimestamp(timestamp));

        titleTextView.setText(title);
        priceTextView.setText("Rs. "+price);
        usernameTextView.setText("Published by "+username);
        descriptionTextView.setText(description);
        categoryTextView.setText(category);
        conditionTextView.setText(condition);
        brandTextView.setText(brand);
        statusTextView.setText(status);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop());

        ImageSlider imageSlider = view.findViewById(R.id.sliderView);
        ArrayList<SlideModel> imageList = new ArrayList<>();
        if (imageUrls != null) {
            for (String imageUrl : imageUrls) {
                imageList.add(new SlideModel(imageUrl, ScaleTypes.CENTER_CROP));
            }
        }
        imageSlider.setImageList(imageList);

        cart_frag cartFragment = new cart_frag();

        // Get the count of items in the cart
        int cartItemsCount = cartFragment.getCartItemsCount();

        // Update the countcart TextView
        TextView countcartTextView = view.findViewById(R.id.countcart);
        countcartTextView.setText(String.valueOf(cartItemsCount));



        Button buyNowButton = view.findViewById(R.id.buynow);
        buyNowButton.setOnClickListener(new View.OnClickListener() {
            private static final String TAG = "Buying Add";

            @Override
            public void onClick(View v) {
                // Create a new instance of the buynow fragment
                buynow buyNowFragment = new buynow();

                // Pass data to the buynow fragment using arguments
                Bundle args = new Bundle();
                args.putBoolean("isBuyNowClicked", true); // Pass a flag indicating the buy now button is clicked
                args.putString("adId", adId);
                args.putString("uid", uid);
                Log.d(TAG, "On AdIdSuccessful: " + adId);
                args.putString("title", title);
                args.putString("price", price);
                args.putString("username", username);
                args.putString("imageUrl", imageUrls.get(0));
                args.putString("category", category);
                args.putString("brand", brand);
                args.putString("status", status);
                args.putLong("timestamp", timestamp);
                //args.putString("adId", adId); // Assuming adId is the variable holding your ad's ID

                buyNowFragment.setArguments(args);

                // Replace the current fragment with the buynow fragment
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, buyNowFragment); // Replace fragment_container with your fragment container id
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });







        return view;
    }

    private String formatTimestamp(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault());
        return "Posted " + sdf.format(new Date(timestamp));
    }





}