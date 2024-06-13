package com.example.myandroidproject.admin.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myandroidproject.R;
import com.example.myandroidproject.account.LoginActivity;
import com.example.myandroidproject.admin.dialog.LogoutDialog;
import com.example.myandroidproject.utils.SharedPreferencesUtils;
import com.example.myandroidproject.utilss.Constraint;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class AdminActivity extends AppCompatActivity {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.container), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        drawer = findViewById(R.id.drawer_layout);

        Button b = findViewById(R.id.button);
        b.setOnClickListener((view) -> {
            drawer.openDrawer(findViewById(R.id.nav_view), true);
        });

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.admin_fragment);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
        NavigationView nv = findViewById(R.id.nav_view);
        NavigationUI.setupWithNavController(nv, navController);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int userID = sharedPreferences.getInt("id", -1);


        if (userID != -1) {
            checkUserExists(userID);
        }
        nv.getMenu().findItem(R.id.logout).setOnMenuItemClickListener(v -> {
            new LogoutDialog(this).show();
            return false;
        });
    }

    private void checkUserExists(int userID) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constraint.URL_USER_BY_ID + userID;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, onReturn(), onError());
        queue.add(jsonObjectRequest);
    }
    private Response.Listener<JSONObject> onReturn(){
        return response ->  {
            NavigationView nv = findViewById(R.id.nav_view);
            TextView tv = nv.getHeaderView(0).findViewById(R.id.nav_header_textView);
            try {
                tv.setText("Currently login as: " + response.getString("lastname") + " " + response.getString("firstname"));
            } catch (JSONException e) {
                Toast.makeText(getBaseContext(), "Error trying to load", Toast.LENGTH_SHORT).show();
            }
        };
    }
    private Response.ErrorListener onError(){
        return error -> {
            Toast.makeText(getBaseContext(), "Error trying to load log in user", Toast.LENGTH_SHORT).show();
        };
    }
}