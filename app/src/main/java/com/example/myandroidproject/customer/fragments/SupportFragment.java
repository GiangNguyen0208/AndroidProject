package com.example.myandroidproject.customer.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myandroidproject.R;
import com.example.myandroidproject.models.Message;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

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
            navController.navigate(R.id.action_global_customer_chat);
        });;

        callBtn.setOnClickListener(v->{
            Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:696969"));
            startActivity(i);
        });
    }
}