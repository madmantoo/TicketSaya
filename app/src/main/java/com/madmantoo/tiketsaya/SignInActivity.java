package com.madmantoo.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvCreateAccount;
    Button btnSignIn;
    EditText edtUsername, edtPassword;

    DatabaseReference reference;
    String USERNAME_KEY = "username_key";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        tvCreateAccount = findViewById(R.id.txt_new_account);
        edtUsername = findViewById(R.id.edt_sign_username);
        edtPassword = findViewById(R.id.edt_sign_password);
        btnSignIn = findViewById(R.id.btn_signIn);

        tvCreateAccount.setOnClickListener(this);

        btnSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txt_new_account) {
            Intent goRegister = new Intent(SignInActivity.this, RegisterOneActivity.class);
            startActivity(goRegister);
        }
        if (v.getId() == R.id.btn_signIn) {
            signIn();
        }


    }

    public void signIn() {
        // ubah state menjadi loading
        btnSignIn.setEnabled(false);
        btnSignIn.setText("Loading...");

        String username = edtUsername.getText().toString();
        final String password = edtPassword.getText().toString();

        if (username.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Username Kosong", Toast.LENGTH_SHORT).show();
            btnSignIn.setEnabled(true);
            btnSignIn.setText("SIGN IN");
        } else {
            if (password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Password Kosong", Toast.LENGTH_SHORT).show();
                btnSignIn.setEnabled(true);
                btnSignIn.setText("SIGN IN");
            } else {
                reference = FirebaseDatabase.getInstance().getReference()
                        .child("Users").child(username);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            //ambil data password dari firebase
                            String passwordDb = snapshot.child("password").getValue().toString();
                            //validasi password db
                            if (password.equals(passwordDb)) {
                                // simpan usernamekey kelokal
                                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(username_key, edtUsername.getText().toString());
                                editor.apply();
                                //pindah activity
                                Intent goHome = new Intent(SignInActivity.this, HomeActivity.class);
                                startActivity(goHome);
                            } else {
                                Toast.makeText(getApplicationContext(), "Password salah", Toast.LENGTH_SHORT).show();
                                btnSignIn.setEnabled(true);
                                btnSignIn.setText("SIGN IN");
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Username tidak ada", Toast.LENGTH_SHORT).show();
                            btnSignIn.setEnabled(true);
                            btnSignIn.setText("SIGN IN");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Database Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}