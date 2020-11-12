package com.madmantoo.tiketsaya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyTicketDetailActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference reference;
    TextView xnama_wisata, xlokasi, xtime_wisata, xdate_wisata, xketentuan;
    Button btnBack, btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ticket_detail);

        xnama_wisata = findViewById(R.id.xnama_wisata);
        xlokasi = findViewById(R.id.xlokasi);
        xtime_wisata = findViewById(R.id.xtime_wisata);
        xdate_wisata = findViewById(R.id.xdate_wisata);
        xketentuan = findViewById(R.id.xketentuan);

        btnBack = findViewById(R.id.btn_back);
        btnHome = findViewById(R.id.btn_home);

        showData();

        btnBack.setOnClickListener(this);
        btnHome.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;

            case R.id.btn_home:
                Intent home = new Intent(MyTicketDetailActivity.this, HomeActivity.class);
                startActivity(home);
                finish();
                break;
        }
    }

    public void showData() {
        //mengambil data dari intent
        Bundle bundle = getIntent().getExtras();
        String nama_wisata_baru = bundle.getString("nama_wisata");
        //ambil db dari firebase
        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(nama_wisata_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                xnama_wisata.setText(snapshot.child("nama_wisata").getValue().toString());
                xlokasi.setText(snapshot.child("lokasi").getValue().toString());
                xtime_wisata.setText(snapshot.child("time_wisata").getValue().toString());
                xdate_wisata.setText(snapshot.child("date_wisata").getValue().toString());
                xketentuan.setText(snapshot.child("ketentuan").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}