package com.madmantoo.tiketsaya;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView photoImgProfile;
    EditText edtNamaLengkap, edtBio, edtUsername, edtPassword, edtEmail;
    Button btnSave, btnEdtImg, btnBack;

    DatabaseReference reference;
    StorageReference storage;

    Uri photoLocation;
    Integer photoMax = 1;

    String USERNAME_KEY = "username_key";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        photoImgProfile = findViewById(R.id.photo_edit_profile);
        edtNamaLengkap = findViewById(R.id.xnama_lengkap);
        edtBio = findViewById(R.id.xbio);
        edtUsername = findViewById(R.id.xusername);
        edtPassword = findViewById(R.id.xpassword);
        edtEmail = findViewById(R.id.xemail);

        btnSave = findViewById(R.id.btn_save);
        btnBack = findViewById(R.id.btn_back);
        btnEdtImg = findViewById(R.id.btn_edt_img);

        getUsernameLocal();
        getData();

        btnSave.setOnClickListener(this);
        btnEdtImg.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_edt_img:
                findPhoto();
                break;
            case R.id.btn_save:
                updateData();
                break;
        }
    }

    public void updateData() {
        btnSave.setEnabled(false);
        btnSave.setText(R.string.loading);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().child("nama_lengkap").setValue(edtNamaLengkap.getText().toString());
                snapshot.getRef().child("bio").setValue(edtBio.getText().toString());
                snapshot.getRef().child("username").setValue(edtUsername.getText().toString());
                snapshot.getRef().child("password").setValue(edtPassword.getText().toString());
                snapshot.getRef().child("email_address").setValue(edtEmail.getText().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Database error.", Toast.LENGTH_SHORT).show();
            }
        });

        //validasi file
        if (photoLocation != null) {
            final StorageReference storageReference =
                    storage.child(System.currentTimeMillis() + "." +
                            getFileExtension(photoLocation));
            storageReference.putFile(photoLocation).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String uri_photo = uri.toString();
                            reference.getRef().child("url_photo_profile").setValue(uri_photo);
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Intent goMyProfile = new Intent(EditProfileActivity.this, MyProfileActivity.class);
                            startActivity(goMyProfile);
                            finish();
                        }
                    });
                }
            });
        } else {
            Intent goMyProfile = new Intent(EditProfileActivity.this, MyProfileActivity.class);
            startActivity(goMyProfile);
            finish();
        }
    }

    public void getData() {
        reference = FirebaseDatabase.getInstance()
                .getReference().child("Users").child(username_key_new);
        storage = FirebaseStorage.getInstance()
                .getReference().child("Photousers").child(username_key_new);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                edtNamaLengkap.setText(snapshot.child("nama_lengkap").getValue().toString());
                edtBio.setText(snapshot.child("bio").getValue().toString());
                edtUsername.setText(snapshot.child("username").getValue().toString());
                edtPassword.setText(snapshot.child("password").getValue().toString());
                edtEmail.setText(snapshot.child("email_address").getValue().toString());


                Picasso.with(EditProfileActivity.this)
                        .load(snapshot.child("url_photo_profile").getValue().toString())
                        .centerCrop()
                        .fit()
                        .into(photoImgProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Database error.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void findPhoto() {
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photoMax);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == photoMax && resultCode == RESULT_OK && data != null && data.getData() != null) {

            photoLocation = data.getData();
            Picasso.with(this).load(photoLocation).centerCrop().fit().into(photoImgProfile);
        }
    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}