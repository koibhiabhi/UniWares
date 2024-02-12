package com.example.uniwares;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class account extends AppCompatActivity {


    ImageButton cls;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        cls = findViewById(R.id.cl);

        cls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pro = new Intent(account.this, profile.class);
                startActivity(pro);
                finish();
            }
        });






    }
}