package com.example.uniwares;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class signup extends AppCompatActivity {

    ImageButton cab;

    EditText edName, edMail,edPwd;

    TextView tv;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edName = findViewById(R.id.Fname);
        edMail = findViewById(R.id.fmail);
        edPwd = findViewById(R.id.pass1);
        tv= findViewById(R.id.click);
        cab = findViewById(R.id.createac1);

        Intent cabl = new Intent(getApplicationContext(), login.class);

        cab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edName.getText().toString();
                String mail = edMail.getText().toString();
                String pass= edPwd.getText().toString();

                if(username.length()==0|| pass.length()==0||mail.length()==0){
                    Toast.makeText(signup.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                } else{
                    if(isValid(pass)){
                        Toast.makeText(signup.this, "Registered Successfully", Toast.LENGTH_SHORT).show();


                } else{
                        Toast.makeText(signup.this, "Password must be 8 characters with at least one uppercase letter and a special character.", Toast.LENGTH_SHORT).show();
                    }

            }
        }
    });
}

    public static boolean isValid(String passwordhere) {
        int f1 = 0, f2 = 0, f3 = 0;
        if (passwordhere.length() < 8) {
            return false;
        } else {
            for (int p = 0; p < passwordhere.length(); p++) {

                if (Character.isLetter(passwordhere.charAt(p))) {

                    f1 = 1;
                }
            }
            for (int r = 0; r < passwordhere.length(); r++) {
                if (Character.isDigit(passwordhere.charAt(r))) {
                    f2 = 1;
                }
            }

            for (int s = 0; s < passwordhere.length(); s++) {
                char c = passwordhere.charAt(s);
                if (c >= 33 && c <= 46 || c == 64) {
                    f3 = 1;

                }
            }
            return f1 == 1 && f2 == 1 && f3 == 1;
        }
    }
}



//                Toast.makeText(getApplicationContext(), "Account created, login with your credentials", Toast.LENGTH_LONG).show();
//                startActivity(cabl);
//                finish();

