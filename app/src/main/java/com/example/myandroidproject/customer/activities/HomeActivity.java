package com.example.myandroidproject.customer.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myandroidproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navbar_host);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView navbar = findViewById(R.id.navbar);
        navbar.setOnItemSelectedListener(item -> {

           if (item.getItemId() == R.id.support){
               navController.navigate(R.id.action_global_support_fragment);
           } else if (item.getItemId() == R.id.home) {
               navController.navigate(R.id.action_global_home);
           }else if (item.getItemId() == R.id.user) {
               navController.navigate(R.id.in4_personal);
           }else if (item.getItemId() == R.id.notify) {
               navController.navigate(R.id.action_global_notify_fragment);
           }
            return true;
        });

    }


}