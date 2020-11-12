package com.madmantoo.tiketsaya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GetStartedActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSigin, btnNewAccount;
    ImageView logo_smal;
    TextView intro_app;
    Animation ttb, btt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        ttb = AnimationUtils.loadAnimation(this, R.anim.ttb);
        btt = AnimationUtils.loadAnimation(this, R.anim.btt);

        btnSigin = findViewById(R.id.btn_signIn);
        btnNewAccount = findViewById(R.id.btn_new_account);

        logo_smal = findViewById(R.id.logo_small);
        intro_app = findViewById(R.id.intro_app);

        logo_smal.startAnimation(ttb);
        intro_app.startAnimation(ttb);
        btnSigin.startAnimation(btt);
        btnNewAccount.startAnimation(btt);

        btnSigin.setOnClickListener(this);
        btnNewAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_signIn) {
            Intent goSignIn = new Intent(GetStartedActivity.this, SignInActivity.class);
            startActivity(goSignIn);
        }
        if (v.getId() == R.id.btn_new_account) {
            Intent goNewAcount = new Intent(GetStartedActivity.this, RegisterOneActivity.class);
            startActivity(goNewAcount);
        }
    }
}