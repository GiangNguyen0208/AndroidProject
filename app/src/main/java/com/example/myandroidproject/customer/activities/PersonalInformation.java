package com.example.myandroidproject.customer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myandroidproject.LoginActivity;
import com.example.myandroidproject.R;
import com.example.myandroidproject.SignUpActivity;

public class PersonalInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_personal_information);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView myAcc = findViewById(R.id.myAccount);
//        TextView myGift = findViewById(R.id.myGift);
//        TextView myLicense = findViewById(R.id.my_license);
//        Button logout = findViewById(R.id.logoutButton);

        myAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonalInformation.this, MyAccount.class));
                finish();
            }
        });

//        myGift.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(PersonalInformation.this, ));
//            }
//        });
    }

}