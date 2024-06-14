package com.example.myandroidproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myandroidproject.account.LoginActivity;
import com.example.myandroidproject.admin.activities.AdminActivity;
import com.example.myandroidproject.customer.activities.HomeActivity;
import com.example.myandroidproject.shipper.activites.ShipperActivity;
import com.example.myandroidproject.utils.SharedPreferencesUtils;
import com.example.myandroidproject.utilss.Constraint;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainAppActivity extends AppCompatActivity {

    //where the program should start
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // delay animation for 5275
        new Handler().postDelayed(() -> {
            SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            String role = preferences.getString("role", null);
            // Check Login, Don't need Login again.
            boolean isLogin = SharedPreferencesUtils.checkLogin(this);
            if (!isLogin)
                startActivity(new Intent(this, LoginActivity.class));
            if (role == null)
                startActivity(new Intent(this, LoginActivity.class));
            else if (role.equals("admin"))
                startActivity(new Intent(this, AdminActivity.class));
            else if (role.equals("shipper"))
                startActivity(new Intent(this, ShipperActivity.class));
            else
                startActivity(new Intent(this, HomeActivity.class));
            finish();
        }, 2275);
    }


}