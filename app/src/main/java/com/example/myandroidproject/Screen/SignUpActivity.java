package com.example.myandroidproject.Screen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myandroidproject.ConnectionDBSQLite;
import com.example.myandroidproject.Models.User;
import com.example.myandroidproject.R;
import com.google.android.material.button.MaterialButton;


public class SignUpActivity extends AppCompatActivity {
    private TextView goBackLogin;
    private EditText firstName, lastName, emailSignUp, password, passwordConfirm;
    private MaterialButton signUpButton;
    private ConnectionDBSQLite DB;

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

        goBackLogin = findViewById(R.id.go_back_login);
        goBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });

        firstName = findViewById(R.id.first_Name);
        lastName = findViewById(R.id.last_name);
        emailSignUp = findViewById(R.id.email_Sign_Up);
        password = findViewById(R.id.password_Sign_Up);
        passwordConfirm = findViewById(R.id.password_Sign_Up_Confirm);
        signUpButton = findViewById(R.id.sign_Up);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstName.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Enter First Name", Toast.LENGTH_SHORT).show();
                } else if (lastName.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Enter Last Name", Toast.LENGTH_SHORT).show();
                } else if (emailSignUp.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
                } else if (password.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                } else if (!password.getText().toString().trim().equals(passwordConfirm.getText().toString().trim())) {
                    Toast.makeText(SignUpActivity.this, "Enter valid password", Toast.LENGTH_SHORT).show();
                } else {
                    if (emailChecker(emailSignUp.getText().toString().trim())) {
                        createUser(emailSignUp.getText().toString().trim(), password.getText().toString().trim());
                    } else {
                        Toast.makeText(SignUpActivity.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    boolean emailChecker(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    void createUser(String email, String password) {
        User user = new User(email, password);
        DB =  new ConnectionDBSQLite(this);
        if(!DB.checkusername(email)){
            Boolean inserted = DB.insertData(email, password);
            if(inserted){
                Toast.makeText(SignUpActivity.this, "Sign Up Success !!!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SignUpActivity.this, "Sign Up Fail !!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}