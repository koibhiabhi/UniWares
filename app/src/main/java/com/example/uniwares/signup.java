package com.example.uniwares;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class signup extends AppCompatActivity {

    ImageButton cab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


//        int flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//        getWindow().getDecorView().setSystemUiVisibility(flag);


        cab = findViewById(R.id.createac1);

//        Button cabb = (Button) cab;

        Intent cabl = new Intent(getApplicationContext(), login.class);

        cab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Account created, login with your credentials", Toast.LENGTH_LONG).show();
                startActivity(cabl);
                finish();
            }
        });


    }
}