package com.example.myandroidproject.account;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myandroidproject.R;
import com.example.myandroidproject.admin.activities.AdminActivity;
import com.example.myandroidproject.customer.activities.HomeActivity;
import com.example.myandroidproject.customer.fragments.NotifyFragment;
import com.example.myandroidproject.helpers.StringHelper;
import com.example.myandroidproject.shipper.activites.ShipperActivity;
import com.example.myandroidproject.utils.SharedPreferencesUtils;
import com.example.myandroidproject.utilss.Constraint;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

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

        TextView createAccount = findViewById(R.id.create_account);
        Button loginBtn = findViewById(R.id.login_btn);
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
        String url = Constraint.URL_SIGN_IN;
        System.out.println(url);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email.getText()).toString();
            jsonBody.put("password", password.getText()).toString();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to create request body.", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> {
                    try {
                        if (response.has("roles")) {
                            String role = response.getString("roles");
                            int idUser = response.getInt("id");
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            System.out.println(sharedPreferences.getAll());
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("role", role);
                            editor.putInt("id", idUser);
                            editor.apply();

                            SharedPreferencesUtils.add(SharedPreferencesUtils.STATE_LOGIN, "TRUE", this);
                            if (role.equals("admin")) {
                                startActivity(new Intent(this, AdminActivity.class));
                            } else if(role.equals("shipper")) {
                                startActivity(new Intent(this, ShipperActivity.class));
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
                }, error -> {
                    error.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Server Lord !!!.", Toast.LENGTH_SHORT).show();
                });
        queue.add(jsonObjectRequest);
    }
}
