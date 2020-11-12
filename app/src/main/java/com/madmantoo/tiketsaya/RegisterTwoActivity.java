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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class RegisterTwoActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnContinue, btnAddPhoto, btnBack;
    ImageView picPhotoRegister;
    EditText edtBio, edtName;

    Uri photoLocation;
    Integer photoMax = 1;

    DatabaseReference reference;
    StorageReference storage;

    String USERNAME_KEY = "username_key";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);

        btnContinue = findViewById(R.id.btn_continue);
        btnBack = findViewById(R.id.btn_back);
        btnAddPhoto = findViewById(R.id.btn_add_photo);
        picPhotoRegister = findViewById(R.id.pic_photo_register_user);
        edtBio = findViewById(R.id.edt_bio);
        edtName = findViewById(R.id.edt_name);

        getUsernameLocal();

        btnContinue.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnAddPhoto.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_add_photo:
                findPhoto();
                break;
            case R.id.btn_continue:
                uploadData();
                break;
        }
    }

    public void uploadData() {
        // ubah state menjadi loading
        btnContinue.setEnabled(false);
        btnContinue.setText(R.string.loading);
        //simpan ke firebase
        reference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(username_key_new);
        storage = FirebaseStorage.getInstance().getReference().child("Photousers").child(username_key_new);

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
                            reference.getRef().child("nama_lengkap").setValue(edtName.getText().toString());
                            reference.getRef().child("bio").setValue(edtBio.getText().toString());
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Intent goSucces = new Intent(RegisterTwoActivity.this, SuccesRegisterActivity.class);
                            startActivity(goSucces);
                        }
                    });
                }
            });
        } else {
            reference.getRef().child("url_photo_profile").setValue("...");
            reference.getRef().child("nama_lengkap").setValue(edtName.getText().toString());
            reference.getRef().child("bio").setValue(edtBio.getText().toString());
            Intent goSucces = new Intent(RegisterTwoActivity.this, SuccesRegisterActivity.class);
            startActivity(goSucces);
        }
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
            Picasso.with(this).load(photoLocation).centerCrop().fit().into(picPhotoRegister);
        }
    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");

    }


}