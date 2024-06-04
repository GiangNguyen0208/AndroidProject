package com.example.myandroidproject.customer.activities;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myandroidproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    static final int supportId = R.id.support;
    TextView goto_showroom;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navbar_host);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView navbar = findViewById(R.id.navbar);

        navbar.setOnItemSelectedListener(item -> {
           if (item.getItemId() == R.id.support){
               navController.navigate(R.id.action_global_support_fragment);
           } else if (item.getItemId() == R.id.home) {
               navController.navigate(R.id.action_global_home);
           }
            return true;
        });

    }
}