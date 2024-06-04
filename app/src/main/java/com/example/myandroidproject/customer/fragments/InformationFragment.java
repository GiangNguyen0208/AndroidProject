package com.example.myandroidproject.customer.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myandroidproject.R;
import com.example.myandroidproject.customer.activities.MyAccount;
public class InformationFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_personal_information, container, false);
        TextView myAcc = v.findViewById(R.id.myAccount);
//        TextView myGift = v.findViewById(R.id.myGift);
//        TextView myLicense = v.findViewById(R.id.my_license);
//        Button logout = v.findViewById(R.id.logoutButton);

        myAcc.setOnClickListener(v1 -> startActivity(new Intent(v1.getContext(), MyAccount.class)));

//        myGift.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(v.getContext(), MyAccount.class));
//            }
//        });
        return v;
    }
}