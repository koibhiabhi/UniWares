package com.example.uniwares.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.uniwares.R;


public class cart_frag extends Fragment {


    public cart_frag() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_cart_frag, container, false);




        ImageView closeButton = view.findViewById(R.id.closecartBtn);

        // Set click listener for the close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the previous fragment
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });


        return view;
    }
}