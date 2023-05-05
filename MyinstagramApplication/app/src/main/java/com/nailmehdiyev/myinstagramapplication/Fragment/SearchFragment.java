package com.nailmehdiyev.myinstagramapplication.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nailmehdiyev.myinstagramapplication.Adapter.IstifadecilerAdapter;

import com.nailmehdiyev.myinstagramapplication.Model.Istifadeciler;
import com.nailmehdiyev.myinstagramapplication.R;

import java.util.ArrayList;

public class SearchFragment extends Fragment {


    private RecyclerView recyclerView;
    private IstifadecilerAdapter istifadecilerAdapter;
    private ArrayList<Istifadeciler>istifadecilerArrayList;
    private EditText editText;
    private AppBarLayout appBarLayout;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);


        istifadecilerArrayList = new ArrayList<>();

        appBarLayout=view.findViewById(R.id.barlyaout);
        recyclerView = view.findViewById(R.id.rcycler);
        editText = view.findViewById(R.id.searcedit);


        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        istifadecilerAdapter = new IstifadecilerAdapter(getContext(), istifadecilerArrayList);

        recyclerView.setAdapter(istifadecilerAdapter);

        istifadecimelumatinal();


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                axtarisad(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return view;
    }

    private void axtarisad(String s) {

        Query query = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("username")
                .startAt(s).endAt(s + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                istifadecilerArrayList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Istifadeciler istifadeciler = dataSnapshot.getValue(Istifadeciler.class);
                                                                                                  // burada getchildren olan hissede user yazilannin altindaki id burada yerlewsir ve onunda icerisinde ad ,email,sifre,resm kimi melumatlar yerlewir


                        assert istifadeciler != null;

                        istifadecilerArrayList.add(istifadeciler);


                }

                istifadecilerAdapter.notifyDataSetChanged();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void istifadecimelumatinal() {


        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotss) {

                if (editText.getText().toString().equals("")) {


                    istifadecilerArrayList.clear();


                    for (DataSnapshot snapshot1 : snapshotss.getChildren()) {// burada getchildren olan hissede user yazilannin altindaki id burada yerlewsir


//                        String name=snapshot1.child("name").getValue().toString();
//                        String username=snapshot1.child("username").getValue().toString();
//                        String emailadresse=snapshot1.child("emailadresse").getValue().toString();
//                        String password=snapshot1.child("password").getValue().toString();
//
//                        String id=snapshot1.child("id").getValue().toString();
//
//                        String bio=snapshot1.child("bio").getValue().toString();
//
//                        String  imageurl=snapshot1.child("imageurl").getValue().toString();
//
//                        Istifadeciler istifadeciler=new Istifadeciler(name,username,emailadresse,password,id,bio,imageurl);


                        Istifadeciler istifadeciler=snapshot1.getValue(Istifadeciler.class);



                        assert istifadeciler != null;

                            istifadecilerArrayList.add(istifadeciler);



                   }

                    istifadecilerAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}