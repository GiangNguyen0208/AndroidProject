package com.example.myandroidproject.admin.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myandroidproject.R;
import com.example.myandroidproject.admin.dialog.LogoutDialog;
import com.google.android.material.navigation.NavigationView;

public class AdminActivity extends AppCompatActivity {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);

        drawer = findViewById(R.id.drawer_layout);
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


        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.admin_fragment);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();

        NavigationView nv = findViewById(R.id.nav_view);
        nv.setNavigationItemSelectedListener((item) -> {
            drawer.closeDrawer(findViewById(R.id.nav_view), true);
            if (item.getItemId() == R.id.nav_item_one)
                navController.navigate(R.id.action_admin_user);
            else if (item.getItemId() == R.id.nav_item_two)
                navController.navigate(R.id.action_admin_vehicle);
            else if (item.getItemId() == R.id.logout) {
                new LogoutDialog(this).show();
            }
            return true;
        });
    }
}