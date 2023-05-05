package com.nailmehdiyev.myinstagramapplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nailmehdiyev.myinstagramapplication.Model.Istifadeciler;

public class RegistrActivity extends AppCompatActivity {

    private EditText editname, editusername, editemail, editpassword;

    private Button registration;
    private TextView logintextview;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private FirebaseUser mUser;

    private ProgressDialog pg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registr);

        mAuth = FirebaseAuth.getInstance();


        mRef = FirebaseDatabase.getInstance().getReference();

        // mFirestore=FirebaseFirestore.getInstance();


        pg = new ProgressDialog(this);
        editusername = findViewById(R.id.editusername);
        editname = findViewById(R.id.editname);
        editemail = findViewById(R.id.editemail);
        editpassword = findViewById(R.id.editpassword);
        registration = findViewById(R.id.buttonregister);
        logintextview = findViewById(R.id.logintextview);

        logintextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrActivity.this,LoginActivity.class));
                finish();
            }
        });


        try {//bu try catchin icerisinde yazmaq lazimdirki herzaman sifir gelmesin deyer

            registration.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String username = editusername.getText().toString();
                    String name = editname.getText().toString();
                    String emailadresse = editemail.getText().toString();
                    String password = editpassword.getText().toString();

                    if (TextUtils.isEmpty(username) || TextUtils.isEmpty(name) || TextUtils.isEmpty(emailadresse) || TextUtils.isEmpty(password)) {

                        Toast.makeText(RegistrActivity.this, "Empty credentials", Toast.LENGTH_SHORT).show();

                    } else if (password.length() < 6) {
                        Toast.makeText(RegistrActivity.this, "Password short", Toast.LENGTH_SHORT).show();

                    } else {
                      //  pg.setMessage("Wait please...");
                      //  pg.show();

                        mAuth.createUserWithEmailAndPassword(emailadresse, password)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        mUser = mAuth.getCurrentUser();

                                        assert mUser != null;
                                        Istifadeciler istifadeciler = new Istifadeciler(name, username, emailadresse, password, mUser.getUid(),"","default","offline");



                                        // mRef.child("user").push().setValue(istifadeciler);

                                      /*  mFirestore.collection("Users").document(mUser.getUid()).set(istifadeciler)
                                                .addOnCompleteListener(RegistrActivity.this, new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){

                                                            Toast.makeText(getApplicationContext(), "Succesful", Toast.LENGTH_SHORT).show();

                                                            startActivity(new Intent(getApplicationContext(),MainActivity2.class));

                                                            finish();


                                                        }else
                                                            Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();


                                                    }
                                                });

                                       */
                                     //   pg.dismiss();
                                        mRef.child("users").child(mUser.getUid()).setValue(istifadeciler)
                                                .addOnSuccessListener(RegistrActivity.this, new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(getApplicationContext(), "Succesful", Toast.LENGTH_SHORT).show();

                                                        startActivity(new Intent(getApplicationContext(), MainActivity2.class));

                                                        finish();

                                                    }


                                                });


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                      //  pg.dismiss();

                                        Toast.makeText(RegistrActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });


                    }


                }
            });


        } catch (Exception e) {

            e.printStackTrace();

        }


    }

}