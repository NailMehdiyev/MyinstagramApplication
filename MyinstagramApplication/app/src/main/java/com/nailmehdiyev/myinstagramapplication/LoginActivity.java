package com.nailmehdiyev.myinstagramapplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {

    private EditText edittextemaillogin, edittextpasswordlogin;
    private TextView logintextview;
    private Button loginbutton;

    private FirebaseAuth mauth;
    private FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edittextemaillogin = findViewById(R.id.editemaillogin);
        edittextpasswordlogin = findViewById(R.id.editpasswordlogin);

        logintextview = findViewById(R.id.registrtextview);
        loginbutton = findViewById(R.id.loginbutton);


        mauth = FirebaseAuth.getInstance();
        mUser = mauth.getCurrentUser();


        logintextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(LoginActivity.this, RegistrActivity.class));
                finish();

            }
        });


        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edittextemaillogin.getText().toString();
                String password = edittextpasswordlogin.getText().toString();
                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {

                    Toast.makeText(LoginActivity.this, "Empty Credentials", Toast.LENGTH_SHORT).show();


                } else
                    loginuser(email, password);

            }
        });


    }


    private void loginuser(String emailtxt, String passwordtxt) {


        mauth.signInWithEmailAndPassword(emailtxt, passwordtxt).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                      .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                Toast.makeText(LoginActivity.this, "Login is Succesfull", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity2.class));
                                finish();


                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


            }
        });

    }







}