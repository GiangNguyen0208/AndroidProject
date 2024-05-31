package com.example.myandroidproject.customer.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import com.example.myandroidproject.R;

public class MyAccount extends AppCompatActivity {

    private EditText etUsername, etFirstname, etLastname, etPhone, etEmail, etBirthday;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        etUsername = findViewById(R.id.etUsername);
        etFirstname = findViewById(R.id.etFirstname);
        etLastname = findViewById(R.id.etLastname);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etBirthday = findViewById(R.id.etBirthday);
        btnSave = findViewById(R.id.btnSave);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null); // giả sử userId đã được lưu sau khi đăng nhập

        if (userId != null) {
            loadUserData(userId);
        }

        btnSave.setOnClickListener(v -> {
            saveUserData(userId);
        });
    }

    private void loadUserData(String email) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://your-backend-url/api/v1/users/" + email;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject user = new JSONObject(response);
                        etUsername.setText(user.optString("username", ""));
                        etFirstname.setText(user.optString("firstname", ""));
                        etLastname.setText(user.optString("lastname", ""));
                        etPhone.setText(user.optString("phone", ""));
                        etEmail.setText(user.optString("email", ""));
                        etBirthday.setText(user.optString("birthDay", ""));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Handle error
                });

        queue.add(stringRequest);
    }

    private void saveUserData(String userId) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://your-backend-url/api/v1/users/" + userId;

        JSONObject user = new JSONObject();
        try {
            // Kiểm tra và thêm từng trường vào JSON object nếu có giá trị
            if (!etUsername.getText().toString().isEmpty()) {
                user.put("username", etUsername.getText().toString());
            }
            if (!etFirstname.getText().toString().isEmpty()) {
                user.put("firstname", etFirstname.getText().toString());
            }
            if (!etLastname.getText().toString().isEmpty()) {
                user.put("lastname", etLastname.getText().toString());
            }
            if (!etPhone.getText().toString().isEmpty()) {
                user.put("phone", etPhone.getText().toString());
            }
            if (!etEmail.getText().toString().isEmpty()) {
                user.put("email", etEmail.getText().toString());
            }
            if (!etBirthday.getText().toString().isEmpty()) {
                user.put("birthDay", etBirthday.getText().toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, user,
                response -> {
                    Toast.makeText(MyAccount.this, "User updated successfully", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Toast.makeText(MyAccount.this, "Error updating user", Toast.LENGTH_SHORT).show();
                });

        queue.add(jsonObjectRequest);
    }
}
