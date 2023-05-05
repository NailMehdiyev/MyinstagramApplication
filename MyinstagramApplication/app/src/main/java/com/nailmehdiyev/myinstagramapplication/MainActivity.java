package com.nailmehdiyev.myinstagramapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private LinearLayout layout;
    private Button sign,registr;
    private ImageView imginstagramlogo;

    private FirebaseAuth mauth;
    private FirebaseUser mUser;

    @Override
    protected void onStart() {
        super.onStart();
        mauth=FirebaseAuth.getInstance();
        mUser=mauth.getCurrentUser();

        if (mUser!= null) {


            startActivity(new Intent(MainActivity.this, MainActivity2.class));
            finish();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout=findViewById(R.id.linear);
        sign=findViewById(R.id.sign);
        registr=findViewById(R.id.registr);

        imginstagramlogo=findViewById(R.id.img);











        layout.animate().alpha(0f).setDuration(1);


        TranslateAnimation animation=new TranslateAnimation(0,0,0,-1500);

        animation.setDuration(1000);
        animation.setFillAfter(false);
        animation.setAnimationListener(new Mainanimationlistener());

        imginstagramlogo.setAnimation(animation);


        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            startActivity(new Intent(MainActivity.this,LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();

            }
        });




        registr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(MainActivity.this,RegistrActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);

                finish();


            }
        });



    }


    private  class Mainanimationlistener implements Animation.AnimationListener{


        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

            imginstagramlogo.clearAnimation();

            imginstagramlogo.setVisibility(View.INVISIBLE);

            layout.animate().alpha(1f).setDuration(1000);

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }



}