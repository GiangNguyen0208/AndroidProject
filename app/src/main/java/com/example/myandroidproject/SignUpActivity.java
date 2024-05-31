package com.example.myandroidproject;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.myandroidproject.helpers.StringHelper;
import com.example.myandroidproject.utils.Constraint;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class SignUpActivity extends AppCompatActivity {
    private TextInputEditText firstName, lastName, emailSignUp, password, passwordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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
    public void goToSignInAct() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public void processFormFields() {

        if (!validationFirstName()
                || !validationLastName()
                || !validationEmail()
                || !validationPasswordAndPassConfirm()) {
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);

        String url = Constraint.URL_BE + "/api/v1/user/register";


        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("firstname", Objects.requireNonNull(firstName.getText()).toString());
            jsonBody.put("lastname", Objects.requireNonNull(lastName.getText()).toString());
            jsonBody.put("email", Objects.requireNonNull(emailSignUp.getText()).toString());
            jsonBody.put("password", Objects.requireNonNull(password.getText()).toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, response -> {
            firstName.setText(null);
            lastName.setText(null);
            emailSignUp.setText(null);
            password.setText(null);
            passwordConfirm.setText(null);
            Toast.makeText(SignUpActivity.this, "Đăng ký thành công !!!", Toast.LENGTH_SHORT).show();
            goToSignInAct();
        }, error -> Toast.makeText(SignUpActivity.this, "Đăng ký thất bại !!!", Toast.LENGTH_SHORT).show()){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public boolean validationFirstName() {
        String firstname = Objects.requireNonNull(firstName.getText()).toString();
        if (firstname.isEmpty()) {
            firstName.setError("Tên không được để trống.");
            return false;
        } else {
            firstName.setError(null);
            return true;
        }
    }
    public boolean validationLastName() {
        String lastname = Objects.requireNonNull(lastName.getText()).toString();
        if (lastname.isEmpty()) {
            lastName.setError("Họ không được để trống.");
            return false;
        } else {
            lastName.setError(null);
            return true;
        }
    }
    public boolean validationEmail() {
        String email = Objects.requireNonNull(emailSignUp.getText()).toString();
        if (email.isEmpty()) {
            emailSignUp.setError("Email không được để trống.");
            return false;
        } else if (!StringHelper.regexEmailValidationPattern(email)) {
            emailSignUp.setError("Vui lòng nhập email hợp lệ.");
            return false;
        } else {
            emailSignUp.setError(null);
            return true;
        }
    }

    public boolean validationPasswordAndPassConfirm() {
        String pass = Objects.requireNonNull(password.getText()).toString();
        String passConf = Objects.requireNonNull(passwordConfirm.getText()).toString();
        if (pass.isEmpty()) {
            password.setError("Mật khẩu không được để trống.");
            return false;
        } else if (!pass.equals(passConf)) {
            password.setError("Mật khẩu không khớp !!!");
            return false;
        } else {
            password.setError(null);
            passwordConfirm.setError(null);
            return true;
        }
    }
}