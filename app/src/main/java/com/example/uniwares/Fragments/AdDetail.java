package com.example.uniwares.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.uniwares.R;

import java.util.ArrayList;
import java.util.Arrays;


public class AdDetail extends Fragment {

    private String title;
    private String price;
    private String username;
    private ArrayList<String> imageUrls; // Updated to ArrayList

    private String description;
    private String category;
    private String condition;
    private String brand;
    private String status;



    public AdDetail() {
        // Required empty public constructor
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString("title");
            price = getArguments().getString("price");
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

        return view;
    }

}