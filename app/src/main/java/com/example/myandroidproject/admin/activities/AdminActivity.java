package com.example.myandroidproject.admin.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myandroidproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class AdminActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);

        drawer = findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }
        });

        Button b = findViewById(R.id.button);
        b.setOnClickListener((view) -> {
            drawer.openDrawer(findViewById(R.id.nav_view), true);
        });
        NavigationView n = drawer.findViewById(R.id.nav_view);

//        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.admin_fragment);
//        NavController navController = navHostFragment.getNavController();
//        navbar.setOnItemSelectedListener(item -> {
//
//            if (item.getItemId() == R.id.support){
//                navController.navigate(R.id.action_global_support_fragment);
//            } else if (item.getItemId() == R.id.home) {
//                navController.navigate(R.id.action_global_home);
//            }else if (item.getItemId() == R.id.user) {
//                navController.navigate(R.id.in4_personal);
//            }else if (item.getItemId() == R.id.notify) {
//                navController.navigate(R.id.action_global_notify_fragment);
//            }
//            return true;
//        });
    }
}