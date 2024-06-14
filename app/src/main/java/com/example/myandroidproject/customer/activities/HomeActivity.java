package com.example.myandroidproject.customer.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.myandroidproject.account.LoginActivity;
import com.example.myandroidproject.R;
import com.example.myandroidproject.utils.SharedPreferencesUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



//        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navbar_host);
//        assert navHostFragment != null;
//        NavController navController = navHostFragment.getNavController();
//        BottomNavigationView navbar = findViewById(R.id.navbar);
//        navbar.setOnItemSelectedListener(item -> {
//
//           if (item.getItemId() == R.id.customer_support){
//               navController.navigate(R.id.action_global_support_fragment);
//           } else if (item.getItemId() == R.id.customer_home) {
//               navController.navigate(R.id.action_global_home);
//           }else if (item.getItemId() == R.id.customer_user) {
//               navController.navigate(R.id.in4_personal);
//           }else if (item.getItemId() == R.id.customer_notify) {
//               navController.navigate(R.id.action_global_notify_fragment);
//           }
//            return true;
//        });
        BottomNavigationView bnv = findViewById(R.id.navbar);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navbar_host);
        assert navHostFragment != null;
        NavController nc = navHostFragment.getNavController();
//        NavController nc = Navigation.findNavController(findViewById(R.id.navbar_host));
        NavigationUI.setupWithNavController(bnv, nc);
    }
}