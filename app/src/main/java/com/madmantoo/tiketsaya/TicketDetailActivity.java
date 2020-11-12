package com.madmantoo.tiketsaya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class TicketDetailActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnBuy, btnBack;
    DatabaseReference reference;
    TextView titleTicket, location, photoSpot, wifiTicket, festivalTicket, shortDesc;
    ImageView headerBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);

        btnBuy = findViewById(R.id.btn_buy_ticket);
        btnBack = findViewById(R.id.btn_back);
        titleTicket = findViewById(R.id.title_ticket);
        location = findViewById(R.id.location_ticket);
        photoSpot = findViewById(R.id.ticket_photo);
        wifiTicket = findViewById(R.id.ticket_wifi);
        festivalTicket = findViewById(R.id.ticket_festival);
        shortDesc = findViewById(R.id.short_desc);
        headerBg = findViewById(R.id.header_ticket_detail);

        getData();

        btnBuy.setOnClickListener(this);

        btnBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_buy_ticket) {
            //mengambil data intent
            Bundle bundle = getIntent().getExtras();
            final String jenis_tiket_baru = bundle.getString("jenis_ticket");

            Intent goCheckout = new Intent(TicketDetailActivity.this, TicketCheckoutActivity.class);
            goCheckout.putExtra("jenis_ticket", jenis_tiket_baru);
            startActivity(goCheckout);
        }
        if (v.getId() == R.id.btn_back) {
            onBackPressed();
        }
    }

    public void getData() {
        //mengambil data intent
        Bundle bundle = getIntent().getExtras();
        final String jenis_tiket_baru = bundle.getString("jenis_ticket");

        //mengambil data firebase berdasarkan intent
        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //menimpa data dengan data yang baru
                titleTicket.setText(snapshot.child("nama_wisata").getValue().toString());
                location.setText(snapshot.child("lokasi").getValue().toString());
                photoSpot.setText(snapshot.child("is_photo_spot").getValue().toString());
                wifiTicket.setText(snapshot.child("is_wifi").getValue().toString());
                festivalTicket.setText(snapshot.child("is_festival").getValue().toString());
                shortDesc.setText(snapshot.child("short_desc").getValue().toString());
                Picasso.with(TicketDetailActivity.this)
                        .load(snapshot.child("url_thumbnail").getValue().toString())
                        .centerCrop()
                        .fit()
                        .into(headerBg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}