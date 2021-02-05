package com.example.universiteliyimapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.universiteliyimapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText userMail,userPassword;
    private Button btnLogin;
    private ProgressBar loginProgress;
    private FirebaseAuth mAuth;
    private Intent HomeActivity;
    private ImageView loginPhoto;
    private TextView newUserTxt;
    private TextView textView9;

    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

Intent loginActivity=getIntent();
String userName=loginActivity.getStringExtra("userName");
TextView textView=findViewById(R.id.textView88);


//textView.setText(userName);




        SharedPreferences sharedPref = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        String savedString = sharedPref.getString("stringValue","Üniversiteli!");
        textView.setText(savedString);



        userMail=findViewById(R.id.logMail);
        userPassword=findViewById(R.id.logPassword);
        btnLogin=findViewById(R.id.loginBtn);
        loginProgress=findViewById(R.id.loginProgress);
        loginProgress.setVisibility(View.INVISIBLE);
        mAuth =FirebaseAuth.getInstance();
        loginPhoto=findViewById(R.id.logUserPhoto);
        newUserTxt=findViewById(R.id.uyeOlTxt);
        textView9=findViewById(R.id.textView9);
        newUserTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerActivity=new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(registerActivity);
                finish();
            }
        });
        loginPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerActivity=new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(registerActivity);
                finish();
            }
        });
        HomeActivity=new Intent(this,com.example.universiteliyimapp.Activities.Home.class);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProgress.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.INVISIBLE);
                final String mail=userMail.getText().toString();
                final String password=userPassword.getText().toString();
                if (mail.isEmpty()|| password.isEmpty()){
                    showMessage("Mail veya şifre eksik girildi. ");
                    btnLogin.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);
                }
                else{
                    sıgnIn(mail,password);
                }

            }
        });
    }

    private void sıgnIn(String mail, String password) {

mAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()){
            loginProgress.setVisibility(View.INVISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
            updateUI();
        }
else{
    showMessage(task.getException().getMessage());
            btnLogin.setVisibility(View.VISIBLE);
            loginProgress.setVisibility(View.INVISIBLE);
        }

    }
});

    }

    private void updateUI() {
startActivity(HomeActivity);
finish();

    }

    private void showMessage(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=mAuth.getCurrentUser();

        if (user!=null){
            //Burada ana sayfaya yonledrime
            updateUI();
        }
    }
}
