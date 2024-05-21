package com.example.myandroidproject.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.myandroidproject.R;
import com.example.myandroidproject.Screen.SupportActivity;

public class NavigatorFragment extends Fragment {
    //Navigator button defined here
    public ImageButton supportBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_navigator, container, false);
        supportBtn = v.findViewById(R.id.supportBtn);
        supportBtn.setOnClickListener((view)->{
            startActivity(new Intent(v.getContext(), SupportActivity.class));
        });
        return v;
    }

}