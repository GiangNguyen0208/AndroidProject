package com.example.myandroidproject.customer.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myandroidproject.R;

public class SupportFragment extends Fragment {

    LinearLayout msgBtn, callBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_support, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        msgBtn = view.findViewById(R.id.msgBtn);
        callBtn = view.findViewById(R.id.callBtn);

        msgBtn.setOnClickListener(v->{
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_global_fragment_chat_customer);
        });;

        callBtn.setOnClickListener(v->{
            Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:696969"));
            startActivity(i);
        });
    }
}