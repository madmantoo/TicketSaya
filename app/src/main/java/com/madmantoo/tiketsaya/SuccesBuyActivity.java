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

public class SuccesBuyActivity extends AppCompatActivity implements View.OnClickListener {

    Animation btt, ttb, app_splash;
    Button btnView, btnDashboard;
    ImageView app_succes;
    TextView app_title, app_sub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_succes_buy);

        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        btt = AnimationUtils.loadAnimation(this, R.anim.btt);
        ttb = AnimationUtils.loadAnimation(this, R.anim.ttb);

        app_succes = findViewById(R.id.app_succes);
        app_title = findViewById(R.id.app_title);
        app_sub = findViewById(R.id.app_sub);
        btnView = findViewById(R.id.btn_view_ticket);
        btnDashboard = findViewById(R.id.btn_dashboard);

        app_succes.startAnimation(app_splash);
        app_title.startAnimation(ttb);
        app_sub.startAnimation(ttb);
        btnDashboard.startAnimation(btt);
        btnView.startAnimation(btt);

        btnView.setOnClickListener(this);
        btnDashboard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_dashboard) {
            Intent goDashboard = new Intent(SuccesBuyActivity.this, HomeActivity.class);
            startActivity(goDashboard);
        }
        if (v.getId() == R.id.btn_view_ticket) {
            Intent goView = new Intent(SuccesBuyActivity.this, MyProfileActivity.class);
            startActivity(goView);
        }
    }
}