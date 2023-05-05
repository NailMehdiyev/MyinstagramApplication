package com.nailmehdiyev.myinstagramapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nailmehdiyev.myinstagramapplication.Adapter.CommentAdapter;
import com.nailmehdiyev.myinstagramapplication.Model.Comment;
import com.nailmehdiyev.myinstagramapplication.Model.Istifadeciler;
import com.nailmehdiyev.myinstagramapplication.Model.Notifications;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Comments extends AppCompatActivity {


    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ImageView imageView, commentsend;
    private EditText editTextcomment;
    private FirebaseUser bizimid;
    private String postid, gondereninid;
    private CommentAdapter commentAdapter;
    private ArrayList<Comment> commentArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        toolbar = (Toolbar) findViewById(R.id.to);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        bizimid = FirebaseAuth.getInstance().getCurrentUser();


        recyclerView = findViewById(R.id.commentrec);
        imageView = findViewById(R.id.commentresm);
        commentsend = findViewById(R.id.isarecom);
        editTextcomment = findViewById(R.id.editcommentari);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        commentArrayList = new ArrayList<>();

        Intent intent = getIntent();

        postid = intent.getStringExtra("postid");
        gondereninid = intent.getStringExtra("publisher");


        commentAdapter = new CommentAdapter(Comments.this, commentArrayList, postid);


        recyclerView.setAdapter(commentAdapter);

        //  qarsiterefincommentigelse();


        commentsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editTextcomment.getText().toString().equals("")) {

                    Toast.makeText(Comments.this, "can not empty", Toast.LENGTH_SHORT).show();


                } else {
                    commentadd();
                }

            }
        });

        resmal();

        commentlerial();

    }


    private void commentadd() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Comments").child(postid);

        String id = reference.push().getKey();

        Comment comment = new Comment(editTextcomment.getText().toString(), bizimid.getUid(), id);

        reference.child(id).setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    addNotifycation(gondereninid,postid,comment.getComment());

                    Toast.makeText(Comments.this, "Comment added", Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(Comments.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();


            }
        });

        editTextcomment.setText("");

    }


    private void addNotifycation(String gonderenid, String postid,String comment) {

        Notifications notifications = new Notifications(bizimid.getUid(), "commented :" + comment , postid, true);

        FirebaseDatabase.getInstance().getReference().child("Notifycations").child(gonderenid).push().setValue(notifications);

    }


    private void resmal() {

        FirebaseDatabase.getInstance().getReference("users").child(bizimid.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Istifadeciler istifadeciler = snapshot.getValue(Istifadeciler.class);


             try {
                 if (istifadeciler.getImageurl().equals("default")) {

                     imageView.setImageResource(R.mipmap.ic_launcher);

                 } else
                     //  Picasso.get().load(istifadeciler.getImageurl()).into(profil);
                     Glide.with(getApplicationContext()).load(R.mipmap.ic_launcher).load(istifadeciler.getImageurl()).into(imageView);

             }catch (Exception e){
                 e.printStackTrace();
             }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void commentlerial() {


        FirebaseDatabase.getInstance().getReference().child("Comments").child(postid).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        commentArrayList.clear();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            if (dataSnapshot != null) {


                                Comment comment = dataSnapshot.getValue(Comment.class);

                                commentArrayList.add(comment);


                            }


                            commentAdapter.notifyDataSetChanged();


                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }


    private void qarsiterefincommentigelse() {


        FirebaseDatabase.getInstance().getReference().child("Comments").child(postid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        if (snapshot.child(bizimid.getUid()).exists()) {

                            if (!FirebaseAuth.getInstance().getCurrentUser().getUid().equals(bizimid.getUid())) {

                                Notifications notifications = new Notifications(gondereninid, FirebaseAuth.getInstance().getCurrentUser().getUid() + "username is commented to another post", postid, true);

                                FirebaseDatabase.getInstance().getReference().child("Notifycations").child(bizimid.getUid()).push().setValue(notifications);


                                commentAdapter.notifyDataSetChanged();
                            }


                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }


}