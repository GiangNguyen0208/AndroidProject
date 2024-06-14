package com.example.myandroidproject.shipper.activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myandroidproject.R;
import com.example.myandroidproject.account.LoginActivity;
import com.example.myandroidproject.utils.SharedPreferencesUtils;

public class ShipperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shipper);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button showMap = findViewById(R.id.showMap);
        showMap.setOnClickListener(v -> {
            Intent intent = new Intent(ShipperActivity.this, MapsActivityShipper.class);
            startActivity(intent);
        });

        Button logout = findViewById(R.id.logoutButton);
                logout.setOnClickListener(v -> logoutUser());


    }
    private void logoutUser() {
        SharedPreferencesUtils.clear(this);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }
}