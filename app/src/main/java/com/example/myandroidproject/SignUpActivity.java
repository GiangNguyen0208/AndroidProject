package com.example.myandroidproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myandroidproject.helpers.StringHelper;
import com.example.myandroidproject.utils.Constraint;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private TextInputEditText firstName, lastName, emailSignUp, password, passwordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        TextView goBackLogin = findViewById(R.id.go_back_login);
        goBackLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });

        firstName = findViewById(R.id.first_Name);
        lastName = findViewById(R.id.last_name);
        emailSignUp = findViewById(R.id.email_Sign_Up);
        password = findViewById(R.id.password_Sign_Up);
        passwordConfirm = findViewById(R.id.password_Sign_Up_Confirm);

        // Hook Sign Up Button
        MaterialButton signUpButton = findViewById(R.id.sign_Up);

        signUpButton.setOnClickListener(v -> processFormFields());
    }

    public void processFormFields() {
        if (validateFields()) {
            registerUser();
        }
    }

    private boolean validateFields() {
        boolean valid = true;

        if (firstName.getText().toString().isEmpty()) {
            firstName.setError("Tên không được để trống.");
            valid = false;
        } else {
            firstName.setError(null);
        }

        if (lastName.getText().toString().isEmpty()) {
            lastName.setError("Họ không được để trống.");
            valid = false;
        } else {
            lastName.setError(null);
        }

        String email = emailSignUp.getText().toString();
        if (email.isEmpty()) {
            emailSignUp.setError("Email không được để trống.");
            valid = false;
        } else if (!StringHelper.regexEmailValidationPattern(email)) {
            emailSignUp.setError("Vui lòng nhập email hợp lệ.");
            valid = false;
        } else {
            emailSignUp.setError(null);
        }

        String pass = password.getText().toString();
        String passConf = passwordConfirm.getText().toString();

        if (pass.isEmpty()) {
            password.setError("Mật khẩu không được để trống.");
            valid = false;
        } else {
            password.setError(null);
        }

        if (passConf.isEmpty()) {
            passwordConfirm.setError("Xác nhận mật khẩu không được để trống.");
            valid = false;
        } else {
            passwordConfirm.setError(null);
        }

        if (!pass.equals(passConf)) {
            passwordConfirm.setError("Mật khẩu không khớp !!!");
            valid = false;
        } else {
            passwordConfirm.setError(null);
        }

        return valid;
    }

    private void registerUser() {
        RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);
        String url = Constraint.URL_BE + "/api/v1/user/register";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("firstname", firstName.getText().toString());
            jsonBody.put("lastname", lastName.getText().toString());
            jsonBody.put("email", emailSignUp.getText().toString());
            jsonBody.put("password", password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> {
                    firstName.setText("");
                    lastName.setText("");
                    emailSignUp.setText("");
                    password.setText("");
                    passwordConfirm.setText("");
                    Toast.makeText(SignUpActivity.this, "Đăng ký thành công !!!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                    finish();
                },
                error -> Toast.makeText(SignUpActivity.this, "Đăng ký thất bại !!!", Toast.LENGTH_SHORT).show()) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }
}
