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

public class SuccesRegisterActivity extends AppCompatActivity {

    Animation app_splash, btt, ttb;
    Button btn_explore;
    TextView app_title, app_sub;
    ImageView icon_succes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_succes_register);

        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        btt = AnimationUtils.loadAnimation(this, R.anim.btt);
        ttb = AnimationUtils.loadAnimation(this, R.anim.ttb);

        btn_explore = findViewById(R.id.btn_explore);
        app_title = findViewById(R.id.app_title);
        app_sub = findViewById(R.id.app_sub);
        icon_succes = findViewById(R.id.icon_succes);

        icon_succes.startAnimation(app_splash);
        app_title.startAnimation(ttb);
        app_sub.startAnimation(ttb);
        btn_explore.startAnimation(btt);

        btn_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goHome = new Intent(SuccesRegisterActivity.this, HomeActivity.class);
                startActivity(goHome);
            }
        });
    }
}