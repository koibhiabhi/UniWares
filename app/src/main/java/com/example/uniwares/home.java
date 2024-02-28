package com.example.uniwares;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.uniwares.Fragments.account_frag;
import com.example.uniwares.Fragments.chat_frag;
import com.example.uniwares.Fragments.donate_frag;
import com.example.uniwares.Fragments.explore_frag;
import com.example.uniwares.Fragments.home_frag;
import com.example.uniwares.Fragments.listing;
import com.example.uniwares.Fragments.order;
import com.example.uniwares.Fragments.transact_frag;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private static final String ROOT_FRAGMENT_TAG = "ROOT_FRAGMENT";
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    Button signout;

    private boolean doubleBackToExitPressedOnce = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navtooldrawbar);
        signout = findViewById(R.id.sgnbtn);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText("UniWares");
        setSupportActionBar(toolbar);

        actionBarDrawerToggle = new ActionBarDrawerToggle(home.this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);


        BottomNavigationView navbar = findViewById(R.id.btmnavbar);
        navbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.home){
                    loadFrag(new home_frag(), false);
                } else if (id == R.id.explore){
                    loadFrag(new explore_frag(), false);
                } else if (id == R.id.transact){
                    loadFrag(new transact_frag(), false);
                } else if (id == R.id.donate){
                    loadFrag(new donate_frag(), false);
                } else { //chats
                    loadFrag(new chat_frag(), false);
                }
                return true;
            }
        });

        navbar.setSelectedItemId(R.id.home);


        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(home.this, "Successfully Logged out! see ya soon", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(home.this, login.class);
                startActivity(intent);
                finish();

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() > 1) {
                fm.popBackStack();
            } else {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(home.this, "Press BACK twice to exit", Toast.LENGTH_SHORT).show();

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce=false;
                    }
                }, 2000);
            }
        }
    }


    public void loadFrag (Fragment fragment, boolean flag){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (flag){
            ft.add(R.id.container, fragment);
            fm.popBackStack(ROOT_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ft.addToBackStack(ROOT_FRAGMENT_TAG);
        } else {
            ft.replace(R.id.container, fragment);
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.account) {
            loadFrag(new account_frag(), false);
        } else if (id ==R.id.order ) {
            loadFrag(new order(), false);
        } else if (id ==R.id.listing) {
            loadFrag(new listing(), false);
        } else {
            loadFrag(new donate_frag(), false);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}











