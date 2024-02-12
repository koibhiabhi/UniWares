package com.example.uniwares;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class login extends AppCompatActivity {

    EditText eMail, ePass;

    ImageButton btn;
    Button gbtn;

    TextView tv;

    FirebaseAuth signin;
    GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        eMail = findViewById(R.id.Edmail);
        ePass = findViewById(R.id.Edpass);
        tv = findViewById(R.id.signup);
        btn = findViewById(R.id.imageButton);
        gbtn = findViewById(R.id.login_google_btn);
        signin = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        gbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });

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

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        signin.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent loggedin = new Intent(login.this, home.class);
                            startActivity(loggedin);
                            finish();
                        } else {
                            Toast.makeText(login.this, "Google sign in failed", Toast.LENGTH_SHORT).show();
                        }
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
