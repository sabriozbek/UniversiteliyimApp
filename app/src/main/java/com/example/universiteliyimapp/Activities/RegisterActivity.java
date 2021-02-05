package com.example.universiteliyimapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.universiteliyimapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {
ImageView userPhoto;
static int PRegCode=1;
    static int REQUESCODE=1;
Uri pickedImgUri;
private EditText userEmail,userPassword,userName;
private ProgressBar loadingProgress;
private Button regButton;
private FirebaseAuth mAuth;
private TextView userLoginTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userEmail=findViewById(R.id.regMail);
        userPassword=findViewById(R.id.regPassword);
        userName=findViewById(R.id.regName);
        loadingProgress=findViewById(R.id.regProgressBar);
        userLoginTxt=findViewById(R.id.userLoginTxt);
        loadingProgress.setVisibility(View.INVISIBLE);
regButton=findViewById(R.id.regBtn);


mAuth=FirebaseAuth.getInstance();
userLoginTxt.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent loginIntent=new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }
});


regButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {


        regButton.setVisibility(View.INVISIBLE);
        loadingProgress.setVisibility(View.VISIBLE);
        final String email=userEmail.getText().toString();
        final String password=userPassword.getText().toString();
        final String name=userName.getText().toString();
        if (email.isEmpty()|| name.isEmpty() || password.isEmpty()){
            showMessage("Lütfen bütün bilgileri eksiksiz giriniz.");
            regButton.setVisibility(View.VISIBLE);
            loadingProgress.setVisibility(View.INVISIBLE);
        }
        else{
            //HErşey tamma ve işlemlerimizi yapalım
            CreateUserAccount(email,name,password);

        }







    }
});
        userPhoto=findViewById(R.id.regUserPhoto);
        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT>=22){

                    checkAndRequestForPermission();
                }
else    {

    openGallery();
                }


            }
        });
    }

    private void CreateUserAccount(String email, final String name, String password) {

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            showMessage("Hesap başarıyla oluşturuldu.");
                            //hesap oluştu işlemleri güncelle
                            updateUserInfo(name,pickedImgUri,mAuth.getCurrentUser());
                        }
                        else    {

                            showMessage("Hesap oluşturulamadı."+task.getException().getMessage());
                            regButton.setVisibility(View.VISIBLE);
                            loadingProgress.setVisibility(View.INVISIBLE);
                        }
                    }
                });

    }

    private void updateUserInfo(final String name, Uri pickedImgUri, final FirebaseUser currentUser) {
        //photo ve isim diğer bilgileri buradan kayıt ediyortuz.
        StorageReference mStorage= FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath=mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //resim başarılı
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileUpdate=new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();
                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()){
                                            showMessage("Kullanıcı bilgileri kaydedildi.");
                                            updateUI();
                                        }
                                        else    {

                                        }
                                    }
                                });

                    }
                });

            }
        });
    }

    private void updateUI() {
        Intent homeActivity=new Intent(getApplicationContext(),Home.class);
        startActivity(homeActivity);
        finish();
    }

    private void showMessage(String s) {

        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    private void openGallery() {
        Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);

    }

    private void checkAndRequestForPermission() {

if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
!= PackageManager.PERMISSION_GRANTED){
    if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
        Toast.makeText(RegisterActivity.this,"Lütfen izin veriniz.",Toast.LENGTH_LONG).show();
    }
    else{

        ActivityCompat.requestPermissions(RegisterActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
    PRegCode);
    }
}
else
    openGallery();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK && requestCode == REQUESCODE && data != null){
pickedImgUri=data.getData();
userPhoto.setImageURI(pickedImgUri);
        }
    }
}
