package com.example.uniwares;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signup extends AppCompatActivity {

    ImageButton cab;

    EditText edName, edMail,edPwd;

    TextView tv;

    FirebaseAuth signupAuth; //including the Firebase object


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
        signupAuth = FirebaseAuth.getInstance(); // initialising FAuth


        Intent cabl = new Intent(getApplicationContext(), login.class);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(cabl);
                finish();
            }
        });

        cab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = edName.getText().toString();
                String mail = edMail.getText().toString();
                String pass= edPwd.getText().toString();

                if (username.length()==0|| pass.length()==0||mail.length()==0) {
                    Toast.makeText(signup.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                } else {
                    if(isValid(pass)){
                        register(mail, pass); // agar details valid hai toh register funtion mein email and pwd
                        Toast.makeText(signup.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(cabl);
                        finish();
                } else {
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

    public void register(String email, String pwd){

        signupAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    Toast.makeText(signup.this, " ", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(signup.this, " ", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}

//abhi name wala aur karna hai abhi ki user ki emial and pwd ke saath name kaise save hoga DB mein
//toh usse name bhi aa jayega


