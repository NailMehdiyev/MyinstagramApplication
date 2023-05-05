package com.nailmehdiyev.myinstagramapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nailmehdiyev.myinstagramapplication.Adapter.IstifadecilerAdapter;
import com.nailmehdiyev.myinstagramapplication.Fragment.HomeFragment;
import com.nailmehdiyev.myinstagramapplication.Model.Istifadeciler;

import java.util.ArrayList;

public class FollowActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String id, title;
    private ArrayList<String> stringArrayListid;
    private ImageView imagebacking;


    private ArrayList<Istifadeciler> istifadecilerArrayList;
    private IstifadecilerAdapter istifadecilerAdapter;
    private RecyclerView recyclerView;
    private FirebaseUser Muser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);


        Intent intent = getIntent();

        id = intent.getStringExtra("id");
        title = intent.getStringExtra("title");


        switch (title) {

            case "follower":

                getfollowers();
                break;

            case "following":

                getfollowings();
                break;


            case "beyenmetextview":

                getlikes();

                break;


        }


        toolbar = (Toolbar) findViewById(R.id.followactivit_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        recyclerView = findViewById(R.id.followactivity_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        istifadecilerArrayList = new ArrayList<>();

        istifadecilerAdapter = new IstifadecilerAdapter(this, istifadecilerArrayList);

        recyclerView.setAdapter(istifadecilerAdapter);


        stringArrayListid = new ArrayList<>();


    }

    private void getlikes() {


        FirebaseDatabase.getInstance().getReference().child("Beyen").child(id).child("liked").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                stringArrayListid.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    stringArrayListid.add(dataSnapshot.getKey());

                }


                showusers();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



    private void getfollowers() {

        FirebaseDatabase.getInstance().getReference().child("Follow").child(id).child("follower")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        stringArrayListid.clear();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            stringArrayListid.add(dataSnapshot.getKey());

                        }

                        showusers();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }


    private void getfollowings() {

        FirebaseDatabase.getInstance().getReference().child("Follow").child(id).child("following")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        stringArrayListid.clear();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            stringArrayListid.add(dataSnapshot.getKey());
                        }
                        showusers();


                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }


    private void showusers() {

        FirebaseDatabase.getInstance().getReference().child("users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        istifadecilerArrayList.clear();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            Istifadeciler istifadeciler = dataSnapshot.getValue(Istifadeciler.class);

                            for (String dd : stringArrayListid) {

                                if (istifadeciler.getId()!=null && dd!=null){

                                    if (istifadeciler.getId().equals(dd)) {

                                        istifadecilerArrayList.add(istifadeciler);
                                    }
                                }



                            }


                        }

                        istifadecilerAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }


}