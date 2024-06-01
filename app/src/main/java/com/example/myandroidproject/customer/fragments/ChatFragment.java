package com.example.myandroidproject.customer.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.myandroidproject.R;

public class ChatFragment extends Fragment {
    ImageView chatExitBtn;
    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_customer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        chatExitBtn = view.findViewById(R.id.chatExitBtn);
        chatExitBtn.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_global_fragment_support);
        });
    }
}