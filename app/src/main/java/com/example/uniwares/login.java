package com.example.uniwares;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    EditText eMail, ePass;

    ImageButton btn;

    TextView tv;

    FirebaseAuth signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        eMail = findViewById(R.id.Edmail);
        ePass = findViewById(R.id.Edpass);
        tv = findViewById(R.id.signup);
        btn = findViewById(R.id.imageButton);
        signin = FirebaseAuth.getInstance();

        // Check if the user is already logged in
        if (signin.getCurrentUser() != null) {
            Intent loggedInIntent = new Intent(login.this, home.class);
            startActivity(loggedInIntent);
            finish();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = eMail.getText().toString();
                String password = ePass.getText().toString();

                if (mail.length() == 0 && password.length() == 0) {
                    Toast.makeText(login.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                } else if (mail.length() == 0) {
                    Toast.makeText(login.this, "Please enter email", Toast.LENGTH_SHORT).show();
                } else if (password.length() == 0) {
                    Toast.makeText(login.this, "Please enter password", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser();
                }
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this, signup.class));
                finish();
            }
        });
    }

    private void loginUser(){
        String email = eMail.getText().toString();
        String passwd = ePass.getText().toString();

        signin.signInWithEmailAndPassword(email, passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                    Intent loggedin = new Intent(login.this, home.class);
                    startActivity(loggedin);
                    finish();
                }
                else if (task.getException() != null) {
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Wrong credentials, Try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}