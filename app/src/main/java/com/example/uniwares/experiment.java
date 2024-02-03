package com.example.uniwares;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;

public class experiment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment);


        LottieAnimationView animationView = findViewById(R.id.animationView);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        // Adjust the width and height of the LottieAnimationView based on screen dimensions
        int newWidth = (int) (screenWidth * 0.8); // Adjust as needed
        int newHeight = (int) (screenHeight * 0.2); // Adjust as needed

        ViewGroup.LayoutParams layoutParams = animationView.getLayoutParams();
        layoutParams.width = newWidth;
        layoutParams.height = newHeight;
        animationView.setLayoutParams(layoutParams);
    }
}