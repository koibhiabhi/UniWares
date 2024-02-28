package com.example.uniwares;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;


public class signup extends AppCompatActivity {

    ImageButton cab;
    EditText edName, edMail, edPwd;
    TextView tv;
    FirebaseAuth signupAuth; // including the Firebase Auth object
    GoogleSignInClient client;
    boolean doubleBackToExitPressedOnce = false;

    FirebaseDatabase database;
    DatabaseReference usersRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edName = findViewById(R.id.Fname);
        edMail = findViewById(R.id.fmail);
        edPwd = findViewById(R.id.pass1);
        tv = findViewById(R.id.click);
        cab = findViewById(R.id.createac1);
        signupAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        client = GoogleSignIn.getClient(this,options);

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
                String mail = edMail.getText().toString();
                String pass = edPwd.getText().toString();
                String username = edName.getText().toString().trim();

                if (username.length() == 0 || pass.length() == 0 || mail.length() == 0) {
                    Toast.makeText(signup.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                } else {
                    if (isValid(pass)) {
                        register(username, mail, pass);
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

    public void register(String username, String mail, String pass) {
        signupAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String userId = task.getResult().getUser().getUid();

                    Map<String, Object> userData = new HashMap<>();
                    userData.put("userId", userId); // Store the user ID
                    userData.put("username", username);
                    userData.put("email", mail);
                    userData.put("password", pass);

                    usersRef.child(userId).setValue(userData)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(signup.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(signup.this, home.class));
                                        finish();
                                    } else {
                                        Toast.makeText(signup.this, "Failed to store user data", Toast.LENGTH_SHORT).show();
                                        Log.e("FirebaseError", "Error storing user data: " + task.getException().getMessage());
                                    }
                                }
                            });
                } else {
                    Toast.makeText(signup.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("FirebaseError", "Registration Failed: " + task.getException().getMessage());
                }
            }
        });
    }

    // Method for Google Sign-In
    public void logingoo(View view) {
        Intent i = client.getSignInIntent();
        startActivityForResult(i, 1234);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1234){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Intent in = new Intent(getApplicationContext(), home.class);
                                    startActivity(in);
                                } else {
                                    Toast.makeText(signup.this, task.getException().getMessage(), Toast.LENGTH_SHORT ).show();
                                }
                            }
                        });
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        }
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












//abhi name wala aur karna hai abhi ki user ki emial and pwd ke saath name kaise save hoga DB mein
//toh usse name bhi aa jayega


//import com.example.uniwares.Models.Users;

//import com.google.firebase.database.FirebaseDatabase;

//FirebaseDatabase userdb; //including the fbdb object

//       userdb = FirebaseDatabase.getInstance();// initialising FDB


//       String id = task.getResult().getUser().getUid();
//       Users users = new Users("", edName.getText().toString(), edMail.getText().toString(), edPwd.getText().toString(), id, "");
//       userdb.getReference().child("Users").child(id).setValue(users);

