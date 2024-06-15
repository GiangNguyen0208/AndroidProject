package com.example.myandroidproject.customer.fragments;

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
import com.example.myandroidproject.customer.activities.InstructionActivity;

public class SupportFragment extends Fragment {

    LinearLayout msgBtn, callBtn, support_instruction1_btn, support_instruction2_btn, company_btn, policy_btn, secure_btn, qA_btn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_support, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        msgBtn = view.findViewById(R.id.msgBtn);
        callBtn = view.findViewById(R.id.callBtn);
        support_instruction1_btn = view.findViewById(R.id.support_instruction1_btn);
        support_instruction2_btn = view.findViewById(R.id.support_instruction2_btn);
        company_btn= view.findViewById(R.id.company_btn);
        policy_btn= view.findViewById(R.id.policy_btn);
        secure_btn = view.findViewById(R.id.secure_btn);
        qA_btn = view.findViewById(R.id.qA_btn);


        msgBtn.setOnClickListener(v->{
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_global_customer_chat);
        });;

        callBtn.setOnClickListener(v->{
            Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:696969"));
            startActivity(i);
        });

        support_instruction1_btn.setOnClickListener(v->{
            Intent intent = new Intent(requireContext(), InstructionActivity.class);
            Bundle b = new Bundle();
            b.putInt("layout", R.layout.activity_support_instruction1);
            intent.putExtras(b);
            this.startActivity(intent);
        });

        support_instruction2_btn.setOnClickListener(v->{
            Intent intent = new Intent(requireContext(), InstructionActivity.class);
            Bundle b = new Bundle();
            b.putInt("layout", R.layout.activity_support_instruction2);
            intent.putExtras(b);
            this.startActivity(intent);
        });

        company_btn.setOnClickListener(v->{
            Intent intent = new Intent(requireContext(), InstructionActivity.class);
            Bundle b = new Bundle();
            b.putInt("layout", R.layout.activity_support_info_company);
            intent.putExtras(b);
            this.startActivity(intent);
        });
        policy_btn.setOnClickListener(v->{
            Intent intent = new Intent(requireContext(), InstructionActivity.class);
            Bundle b = new Bundle();
            b.putInt("layout", R.layout.activity_support_info_secure);
            intent.putExtras(b);
            this.startActivity(intent);
        });
        secure_btn.setOnClickListener(v->{
            Intent intent = new Intent(requireContext(), InstructionActivity.class);
            Bundle b = new Bundle();
            b.putInt("layout", R.layout.activity_support_info_qa);
            intent.putExtras(b);
            this.startActivity(intent);
        });
        qA_btn.setOnClickListener(v->{
            Intent intent = new Intent(requireContext(), InstructionActivity.class);
            Bundle b = new Bundle();
            b.putInt("layout", R.layout.activity_support_info_policy);
            intent.putExtras(b);
            this.startActivity(intent);
        });

    }
}