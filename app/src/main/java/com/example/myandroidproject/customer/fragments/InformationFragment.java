package com.example.myandroidproject.customer.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myandroidproject.LoginActivity;
import com.example.myandroidproject.R;
import com.example.myandroidproject.customer.activities.MyAccount;
import com.example.myandroidproject.customer.activities.MyLicense;
import com.example.myandroidproject.customer.activities.VoucherCustomer;

public class InformationFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_personal_information, container, false);
        TextView myAcc = v.findViewById(R.id.myAccount);
        TextView myGift = v.findViewById(R.id.myGift);
        TextView myLicense = v.findViewById(R.id.my_license);
        Button logout = v.findViewById(R.id.logoutButton);

        myAcc.setOnClickListener(v1 -> startActivity(new Intent(v1.getContext(), MyAccount.class)));
        logout.setOnClickListener(v2 -> logoutUser());
        myGift.setOnClickListener(v3 -> startActivity(new Intent(v3.getContext(), VoucherCustomer.class)));
        myLicense.setOnClickListener(v4 -> startActivity(new Intent(v4.getContext(), MyLicense.class)));
        return v;
    }

    private void logoutUser() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

}