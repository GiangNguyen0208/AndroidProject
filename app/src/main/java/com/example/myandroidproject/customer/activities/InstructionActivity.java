package com.example.myandroidproject.customer.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myandroidproject.R;

public class InstructionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        int layout = getIntent().getIntExtra("layout", R.layout.activity_support_instruction1);
        setContentView(layout);
        super.onCreate(savedInstanceState);
    }
}
