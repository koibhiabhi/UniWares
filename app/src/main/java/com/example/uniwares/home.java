package com.example.uniwares;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class home extends AppCompatActivity {

    Button signout, next;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        signout = findViewById(R.id.logoutbtn);
        next = findViewById(R.id.nn);

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut(); //for signout button firebase se milega
                Toast.makeText(home.this, "successfully logged out", Toast.LENGTH_SHORT).show();
                //jab user logout krega toh login page pe redirect ho jayega
                Intent intent = new Intent(home.this, login.class);
                startActivity(intent);
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n = new Intent(home.this, profile.class);
                startActivity(n);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }



}