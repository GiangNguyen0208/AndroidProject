package com.example.myandroidproject.admin.dialog;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.myandroidproject.R;
import com.example.myandroidproject.account.LoginActivity;
import com.example.myandroidproject.utils.SharedPreferencesUtils;

public class LogoutDialog extends Dialog implements View.OnClickListener {
    public Activity c;
    public Dialog d;
    public Button yes, no;

    public LogoutDialog(Activity a) {
        super(a);
        this.c = a;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.logout_dialog);
        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_yes) {
            SharedPreferencesUtils.clear(v.getContext());
            v.getContext().startActivity(new Intent(c, LoginActivity.class));
            c.finish();
        }
        else {
            dismiss();
        }
    }
}
