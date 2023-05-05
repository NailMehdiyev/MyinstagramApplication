package com.nailmehdiyev.myinstagramapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nailmehdiyev.myinstagramapplication.Adapter.GonderiAdapter;
import com.nailmehdiyev.myinstagramapplication.Model.Gonderi;

import java.util.ArrayList;

public class PostDetailFragment extends Fragment {

    private RecyclerView recyclerView;
    private GonderiAdapter gonderiAdapter;
    private ArrayList<Gonderi> gonderiArrayList;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);


        SharedPreferences sharedPreferences=getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);

        String postid=sharedPreferences.getString("postId","none");


        recyclerView = view.findViewById(R.id.postdetailfragment_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        gonderiArrayList = new ArrayList<>();
        gonderiAdapter = new GonderiAdapter(getContext(), gonderiArrayList);
        recyclerView.setAdapter(gonderiAdapter);





        FirebaseDatabase.getInstance().getReference().child("Posts").child(postid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                gonderiArrayList.clear();

                gonderiArrayList.add(snapshot.getValue(Gonderi.class));

                gonderiAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;


    }
}