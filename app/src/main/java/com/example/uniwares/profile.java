package com.example.uniwares;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class profile extends AppCompatActivity {

    Button signout, fb, myAc, myOrd, myList, don;
    ImageButton close;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        close = findViewById(R.id.cl);
        signout = findViewById(R.id.sgnout);
        fb = findViewById(R.id.fdbck);
        myAc = findViewById(R.id.myac);
        myOrd = findViewById(R.id.myorder);
        myList = findViewById(R.id.mylist);
        don = findViewById(R.id.donateit);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cls = new Intent(profile.this, home.class);
                startActivity(cls);
                finish();
            }
        });



        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut(); //for signout button firebase se milega
                Toast.makeText(profile.this, "successfully logged out", Toast.LENGTH_SHORT).show();
                //jab user logout krega toh login page pe redirect ho jayega
                Intent intent = new Intent(profile.this, login.class);
                startActivity(intent);
                finish();
            }
        });





        myAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ac = new Intent(profile.this, account.class);
                startActivity(ac);
                finish();
            }
        });







    }
}