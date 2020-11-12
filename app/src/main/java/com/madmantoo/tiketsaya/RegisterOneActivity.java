package com.madmantoo.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterOneActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnContinue, btnBack;
    EditText edtUsername, edtPassword, edtEmail;
    DatabaseReference reference, referenceUsername;

    String USERNAME_KEY = "username_key";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_one);

        btnBack = findViewById(R.id.btn_back);
        btnContinue = findViewById(R.id.btn_continue);
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        edtEmail = findViewById(R.id.edt_email);

        btnContinue.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.btn_back:
                onBackPressed();
                finish();
            case R.id.btn_continue:
                regisUser();
        }
    }

    public void regisUser() {
        // ubah state menjadi loading
        btnContinue.setEnabled(false);
        btnContinue.setText(R.string.loading);

        //cek username firebase
        referenceUsername = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(edtUsername.getText().toString());
        referenceUsername.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //validasi username apakah ada?
                if (snapshot.exists()) {
                    Toast.makeText(getApplicationContext(), "Username sudah ada", Toast.LENGTH_SHORT).show();
                    btnContinue.setEnabled(true);
                    btnContinue.setText(R.string.continue_label);
                }else {
                    //menyimpan data ke local
                    SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(username_key, edtUsername.getText().toString());
                    editor.apply();

                    //simpan ke db
                    reference = FirebaseDatabase.getInstance().getReference().child("Users").child(edtUsername.getText().toString());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            dataSnapshot.getRef().child("username").setValue(edtUsername.getText().toString());
                            dataSnapshot.getRef().child("password").setValue(edtPassword.getText().toString());
                            dataSnapshot.getRef().child("email_address").setValue(edtEmail.getText().toString());
                            dataSnapshot.getRef().child("user_balance").setValue(800);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), "DB Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Intent goNextRegister = new Intent(RegisterOneActivity.this, RegisterTwoActivity.class);
                    startActivity(goNextRegister);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "DB Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}