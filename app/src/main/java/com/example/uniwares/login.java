package com.example.uniwares;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class login extends AppCompatActivity {

    EditText eMail, ePass;

    ImageButton btn;

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        eMail = findViewById(R.id.Edmail);
        ePass = findViewById(R.id.Edpass);
        tv = findViewById(R.id.signup);
        btn = findViewById(R.id.imageButton);


//        int flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//        getWindow().getDecorView().setSystemUiVisibility(flag);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = eMail.getText().toString();
                String password = ePass.getText().toString();

                if(mail.length()==0 || password.length()==0){
                    Toast.makeText(login.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this, signup.class));

            }
        });

    }
}