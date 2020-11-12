package com.madmantoo.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyProfileActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout btnMyTicket;
    Button btnEditProfile, btnBack, btnSignOut;
    TextView tvName, tvBio;
    ImageView photoProfile;
    DatabaseReference reference, reference2;

    String USERNAME_KEY = "username_key";
    String username_key = "";
    String username_key_new = "";

    RecyclerView myticket_place;
    ArrayList<MyTicket> list;
    TicketAdapter ticketAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        getUsernameLocal();
        getData();
        showRecycler();

        btnMyTicket = findViewById(R.id.item_my_ticket);
        btnEditProfile = findViewById(R.id.btn_edit_profile);
        btnBack = findViewById(R.id.btn_back);
        btnSignOut = findViewById(R.id.btn_signOut);

        tvName = findViewById(R.id.tv_name);
        tvBio = findViewById(R.id.tv_bio);
        photoProfile = findViewById(R.id.photo_profile);

        myticket_place = findViewById(R.id.myticketplace);
        myticket_place.setLayoutManager(new LinearLayoutManager(this));

        btnEditProfile.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_edit_profile:
                Intent goEditProfile = new Intent(MyProfileActivity.this, EditProfileActivity.class);
                startActivity(goEditProfile);
                break;

            case R.id.btn_signOut:
                //menghapus username local
                //menyimpan data ke local
                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(username_key, null);
                editor.apply();

                //pindah activity
                Intent gohome = new Intent(MyProfileActivity.this, SignInActivity.class);
                startActivity(gohome);
                finish();
        }
    }

    public void getData() {
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvName.setText(snapshot.child("nama_lengkap").getValue().toString());
                tvBio.setText(snapshot.child("bio").getValue().toString());
                tvBio.setText(snapshot.child("bio").getValue().toString());
                Picasso.with(MyProfileActivity.this)
                        .load(snapshot.child("url_photo_profile")
                                .getValue().toString()).centerCrop().fit()
                        .into(photoProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void showRecycler() {
        list = new ArrayList<>();
        reference2 = FirebaseDatabase.getInstance()
                .getReference().child("MyTicket").child(username_key_new);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    MyTicket p = dataSnapshot1.getValue(MyTicket.class);
                    list.add(p);
                }
                ticketAdapter = new TicketAdapter(MyProfileActivity.this, list);
                myticket_place.setAdapter(ticketAdapter);
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