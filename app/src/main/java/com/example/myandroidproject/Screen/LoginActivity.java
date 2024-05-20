package com.example.myandroidproject.Screen;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.PixelCopy;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.myandroidproject.Helpers.StringHelper;
import com.example.myandroidproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private TextView createAccount;
    private Button loginBtn;
    private EditText txt_email, txt_password;


    @SuppressLint("MissingInflatedId")
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
        txt_email = findViewById(R.id.email_Sign_In);
        txt_password = findViewById(R.id.password_Sign_In);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                finish();
                authenticationAndSignUserIn();
            }
        });

        loginBtn.setOnClickListener((v)->{
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        });


    }

    private void authenticationAndSignUserIn() {
         RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
         String url = "http://localhost:8080/api/v1/user/signin";
         HashMap<String, String> param = new HashMap<String, String>();
         param.put("email", txt_email.getText().toString());
         param.put("password", txt_password.getText().toString());

//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url);

    }
    public void goToSignUp(View view) {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }
    public void goToHome(View view) {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
    public void goToSignUpAct(View view) {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }
    public boolean validationEmail() {
        String email = txt_email.getText().toString();
        if (email.isEmpty()) {
            txt_email.setError("Email không được để trống.");
            return false;
        } else if (!StringHelper.regexEmailValidationPattern(email)) {
            txt_email.setError("Vui lòng nhập email hợp lệ.");
            return false;
        } else {
            txt_email.setError(null);
            return true;
        }
    }
    public boolean validationPassword() {
        String pass = txt_password.getText().toString();

        if (pass.isEmpty()) {
            txt_password.setError("Mật khẩu không được để trống.");
            return false;
        } else {
            txt_password.setError(null);
            return true;
        }
    }

}