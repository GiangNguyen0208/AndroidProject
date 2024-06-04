package com.example.myandroidproject.customer.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myandroidproject.R;
import com.example.myandroidproject.admin.activities.AdminActivity;
import com.example.myandroidproject.helpers.StringHelper;
import com.example.myandroidproject.shipper.activites.ShipperActivity;
import com.example.myandroidproject.utilss.Constraint;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private TextView createAccount;
    private Button loginBtn;
    private TextInputEditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        createAccount = findViewById(R.id.create_account);
        loginBtn = findViewById(R.id.login_btn);
        email = findViewById(R.id.email_Sign_In);
        password = findViewById(R.id.password_Sign_In);

        createAccount.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            finish();
        });

        loginBtn.setOnClickListener(v -> {
            if (validateLoginForm()) {
                performLogin();
            }
        });
    }

    private boolean validateLoginForm() {
        String emailInput = email.getText().toString().trim();
        String passwordInput = password.getText().toString().trim();

        if (emailInput.isEmpty()) {
            email.setError("Email không được để trống.");
            return false;
        } else if (!StringHelper.regexEmailValidationPattern(emailInput)) {
            email.setError("Vui lòng nhập email hợp lệ.");
            return false;
        } else {
            email.setError(null);
        }

        if (passwordInput.isEmpty()) {
            password.setError("Mật khẩu không được để trống.");
            return false;
        } else {
            password.setError(null);
        }
        return true;
    }

    private void performLogin() {
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        String url = "http://" + Constraint.URL_BE + ":" + Constraint.PORT_BE + "/api/v1/user/signin";
//            String url = Constraint.URL + "/api/v1/user/signin";
        System.out.println(url);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email.getText().toString());
            jsonBody.put("password", password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to create request body.", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("roles")) {
                                String role = response.getString("roles");
                                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("role", role);
                                editor.apply();

                                if (role.equals("admin")) {
                                    startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                                } else if(role.equals("shipper")) {
                                    startActivity(new Intent(LoginActivity.this, ShipperActivity.class));
                                } else {
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                }
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Error occurred while parsing response.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(LoginActivity.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);
    }
}
