package com.example.uniwares;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class splash extends AppCompatActivity {

    ImageView i1,i2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        i1 = findViewById(R.id.logo);

        Animation scalinglogo = AnimationUtils.loadAnimation(this, R.anim.splash_scale);
        i1.startAnimation(scalinglogo);

//        int flag = View.SYSTEM_UI_FLAG_FULLSCREEN  | View.SYSTEM_UI_FLAG_FULLSCREEN;
//        getWindow().getDecorView().setSystemUiVisibility(flag);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent splashsc = new Intent(splash.this, signup.class);
                startActivity(splashsc);
                finish();
            }
        },4000);
    }
}