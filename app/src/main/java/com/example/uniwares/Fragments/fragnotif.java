package com.example.uniwares.Fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.uniwares.R;


public class fragnotif extends Fragment {


    public fragnotif() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_fragnotif, container, false);


        String packageName = "com.example.myapp";


        Button chatbot = view.findViewById(R.id.chatbot);


        chatbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isAppInstalled(packageName)) {
                    // Create an intent with the package name of the app
                    Intent intent = requireActivity().getPackageManager().getLaunchIntentForPackage(packageName);

                    // Start the activity if the intent is not null
                    if (intent != null) {
                        startActivity(intent);
                    }
                } else {
                    // App is not installed, show a message or handle it as needed
                    Toast.makeText(requireContext(), "App is not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    private boolean isAppInstalled(String packageName) {
        try {
            requireActivity().getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}