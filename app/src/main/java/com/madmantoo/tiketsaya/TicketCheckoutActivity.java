package com.madmantoo.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import java.util.Random;

public class TicketCheckoutActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnBuyTicket, btnMin, btnPlus, btnBack;
    TextView jmtTicket, txtHarga, txtBalace, txtNamaWisata, txtLocation, txtDesc;
    ImageView noticeUang;
    Integer vJumlah = 1;
    Integer myBalance = 0;
    Integer totalHarga = 0;
    Integer hargaTicket = 0;
    Integer sisa_balance = 0;

    String date_wisata = "";
    String time_wisata = "";

    //generate nomor random
    //agar nomor transaksi unik
    Integer noTransaksi = new Random().nextInt();

    DatabaseReference reference, reference2, reference3, reference4;

    String USERNAME_KEY = "username_key";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_checkout);

        btnBuyTicket = findViewById(R.id.btn_buy_ticket);
        btnMin = findViewById(R.id.btn_min);
        btnPlus = findViewById(R.id.btn_plus);
        btnBack = findViewById(R.id.btn_back);

        jmtTicket = findViewById(R.id.jml_ticket);
        txtBalace = findViewById(R.id.txt_balance);
        txtHarga = findViewById(R.id.txt_harga);
        noticeUang = findViewById(R.id.notice_uang);
        txtNamaWisata = findViewById(R.id.nama_wisata);
        txtLocation = findViewById(R.id.txt_location);
        txtDesc = findViewById(R.id.txt_desc);

        //setting value baru
        jmtTicket.setText(vJumlah.toString());
        noticeUang.setVisibility(View.GONE);

        //menyembunyikan btn min secara default
        btnMin.animate().alpha(0).setDuration(300).start();
        btnMin.setEnabled(false);

        getUsernameLocal();
        loadUserData();
        loadWisata();

        btnMin.setOnClickListener(this);
        btnPlus.setOnClickListener(this);
        btnBuyTicket.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_min:
                calculateMin();
                break;

            case R.id.btn_plus:
                calculatePlus();
                break;

            case R.id.btn_buy_ticket:
                buyTicket();
                break;

            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }

    public void calculatePlus() {
        vJumlah += 1;
        jmtTicket.setText(vJumlah.toString());
        if (vJumlah > 1) {
            btnMin.animate().alpha(1).setDuration(300).start();
            btnMin.setEnabled(true);
        }
        totalHarga = hargaTicket * vJumlah;
        txtHarga.setText("$US " + totalHarga + "");
        if (totalHarga > myBalance) {
            btnBuyTicket.animate().translationY(250).alpha(0).setDuration(350).start();
            btnBuyTicket.setEnabled(false);
            txtBalace.setTextColor(Color.parseColor("#D1206B"));
            noticeUang.setVisibility(View.VISIBLE);
        }
    }

    public void calculateMin() {
        vJumlah -= 1;
        jmtTicket.setText(vJumlah.toString());
        if (vJumlah < 2) {
            btnMin.animate().alpha(0).setDuration(300).start();
            btnMin.setEnabled(false);
        }
        totalHarga = hargaTicket * vJumlah;
        txtHarga.setText("$US " + totalHarga + "");
        if (totalHarga < myBalance) {
            btnBuyTicket.animate().translationY(0).alpha(1).setDuration(350).start();
            btnBuyTicket.setEnabled(true);
            txtBalace.setTextColor(Color.parseColor("#203DD1"));
            noticeUang.setVisibility(View.GONE);
        }
    }

    public void buyTicket() {
        //menyimpan data user ke firebase dan buat table baru
        reference3 = FirebaseDatabase.getInstance()
                .getReference().child("MyTicket")
                .child(username_key_new).child(txtNamaWisata.getText().toString() + noTransaksi);

        reference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reference3.getRef().child("id_ticket").setValue(txtNamaWisata.getText().toString() + noTransaksi);
                reference3.getRef().child("nama_wisata").setValue(txtNamaWisata.getText().toString());
                reference3.getRef().child("lokasi").setValue(txtLocation.getText().toString());
                reference3.getRef().child("ketentuan").setValue(txtDesc.getText().toString());
                reference3.getRef().child("jumlah_tiket").setValue(vJumlah.toString());
                reference3.getRef().child("date_wisata").setValue(date_wisata);
                reference3.getRef().child("time_wisata").setValue(time_wisata);

                Intent goSuccesTicket = new Intent(TicketCheckoutActivity.this, SuccesBuyActivity.class);
                startActivity(goSuccesTicket);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Database Error", Toast.LENGTH_SHORT).show();
            }
        });

        reference4 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sisa_balance = myBalance - totalHarga;
                reference4.getRef().child("user_balance").setValue(sisa_balance);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void loadUserData() {
        //mengambil data user dari firebase
        reference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myBalance = Integer.valueOf(snapshot.child("user_balance").getValue().toString());
                txtBalace.setText("$US " + myBalance + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadWisata() {
        //mengambil data dari intent
        Bundle bundle = getIntent().getExtras();
        String jenis_tiket_baru = bundle.getString("jenis_ticket");
        //mengambil data firebase berdasarkan intent
        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //menimpa data dengan data yang baru
                txtNamaWisata.setText(snapshot.child("nama_wisata").getValue().toString());
                txtLocation.setText(snapshot.child("lokasi").getValue().toString());
                txtDesc.setText(snapshot.child("ketentuan").getValue().toString());
                date_wisata = snapshot.child("date_wisata").getValue().toString();
                time_wisata = snapshot.child("time_wisata").getValue().toString();

                hargaTicket = Integer.valueOf(snapshot.child("harga_tiket").getValue().toString());
                totalHarga = hargaTicket * vJumlah;
                txtHarga.setText("$US " + totalHarga + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}