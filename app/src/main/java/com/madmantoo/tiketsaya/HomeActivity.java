package com.madmantoo.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.florent37.shapeofview.shapes.CircleView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout btnTicketPisa,
            btnTicketTori, btnTicketPagoda,
            btnTicketCandi, btnTicketSphinx,
            btnTicketMonas;

    CircleView btnProfile;
    ImageView photoUser;
    TextView namaLengkap, bio, userBalance;

    DatabaseReference reference;
    String USERNAME_KEY = "username_key";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnTicketPisa = findViewById(R.id.btn_ticket_pisa);
        btnTicketTori = findViewById(R.id.btn_ticket_torri);
        btnTicketPagoda = findViewById(R.id.btn_ticket_pagoda);
        btnTicketCandi = findViewById(R.id.btn_ticket_candi);
        btnTicketSphinx = findViewById(R.id.btn_ticket_sphinx);
        btnTicketMonas = findViewById(R.id.btn_ticket_monas);

        btnProfile = findViewById(R.id.btn_to_profile);
        photoUser = findViewById(R.id.photo_home_user);
        namaLengkap = findViewById(R.id.nama_lengkap);
        bio = findViewById(R.id.bio);
        userBalance = findViewById(R.id.user_balance);

        getUsernameLocal();
        getData();

        btnTicketPisa.setOnClickListener(this);
        btnTicketTori.setOnClickListener(this);
        btnTicketPagoda.setOnClickListener(this);
        btnTicketCandi.setOnClickListener(this);
        btnTicketSphinx.setOnClickListener(this);
        btnTicketMonas.setOnClickListener(this);
        btnProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ticket_pisa:
                Intent goPisa = new Intent(HomeActivity.this, TicketDetailActivity.class);
                goPisa.putExtra("jenis_ticket", "Pisa");
                startActivity(goPisa);
                break;
            case R.id.btn_ticket_torri:
                Intent goTori = new Intent(HomeActivity.this, TicketDetailActivity.class);
                goTori.putExtra("jenis_ticket", "Torri");
                startActivity(goTori);
                break;
            case R.id.btn_ticket_pagoda:
                Intent goPagoda = new Intent(HomeActivity.this, TicketDetailActivity.class);
                goPagoda.putExtra("jenis_ticket", "Pagoda");
                startActivity(goPagoda);
                break;
            case R.id.btn_ticket_candi:
                Intent goCandi = new Intent(HomeActivity.this, TicketDetailActivity.class);
                goCandi.putExtra("jenis_ticket", "Candi");
                startActivity(goCandi);
                break;
            case R.id.btn_ticket_sphinx:
                Intent goSphinx = new Intent(HomeActivity.this, TicketDetailActivity.class);
                //meletakan data ke intent
                goSphinx.putExtra("jenis_ticket", "Sphinx");
                startActivity(goSphinx);
                break;
            case R.id.btn_ticket_monas:
                Intent goMonas = new Intent(HomeActivity.this, TicketDetailActivity.class);
                goMonas.putExtra("jenis_ticket", "Monas");
                startActivity(goMonas);
                break;

            case R.id.btn_to_profile:
                Intent goProfile = new Intent(HomeActivity.this, MyProfileActivity.class);
                startActivity(goProfile);
                break;
        }
    }

    public void getData() {
        reference = FirebaseDatabase.getInstance()
                .getReference().child("Users").child(username_key_new);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                namaLengkap.setText(snapshot.child("nama_lengkap").getValue().toString());
                bio.setText(snapshot.child("bio").getValue().toString());
                userBalance.setText("$ " + snapshot.child("user_balance").getValue().toString());

                Picasso.with(HomeActivity.this)
                        .load(snapshot.child("url_photo_profile").getValue().toString())
                        .centerCrop()
                        .fit()
                        .into(photoUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}