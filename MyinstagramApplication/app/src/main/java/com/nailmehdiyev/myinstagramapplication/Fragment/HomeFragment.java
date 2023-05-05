package com.nailmehdiyev.myinstagramapplication.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nailmehdiyev.myinstagramapplication.Adapter.GonderiAdapter;
import com.nailmehdiyev.myinstagramapplication.DirektmesajActivity;
import com.nailmehdiyev.myinstagramapplication.MainActivity2;
import com.nailmehdiyev.myinstagramapplication.Model.Gonderi;
import com.nailmehdiyev.myinstagramapplication.Model.Mesajlar;
import com.nailmehdiyev.myinstagramapplication.R;

import java.util.ArrayList;


public class HomeFragment extends Fragment {


    private RecyclerView recyclerView;
    private ArrayList<Gonderi> gonderiArrayList;
    private ArrayList<String> stringArrayList;
    private GonderiAdapter gonderiAdapter;
    private ImageView direktmesajlar;
    private FirebaseUser muser = FirebaseAuth.getInstance().getCurrentUser();
    private ImageView zinqirov;

    Toolbar toolbar;
    String sss, ddr;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.homerec);
        direktmesajlar = view.findViewById(R.id.homefragment_direkt);
        zinqirov = view.findViewById(R.id.homefragment_direkzinqirov);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        gonderiArrayList = new ArrayList<>();
        gonderiAdapter = new GonderiAdapter(getContext(), gonderiArrayList);

        recyclerView.setAdapter(gonderiAdapter);

        followcontrol();

        sonnmesajlar(muser.getUid(), zinqirov);


        // view.setSupportActionBar(toolbar);
        // getSupportActionBar().setTitle("");
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        direktmesajlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DirektmesajActivity.class);

                startActivity(intent);

                requireActivity().finish();

            }
        });


        return view;
    }


    private void followcontrol() {//burani ona gore edirikki following etdiyin qarsiteref istifadecinin useridsini aliriq daha sonra Posts dan gonderilenleri cagiraraq hemin qarsiterefin id si gonderi clasindaki id e beraberse ordaki butun melumatlari atiriq listeye vetricalformada recyclerview formada.

        stringArrayList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot5) {

                        stringArrayList.clear();

                        for (DataSnapshot dataSnapshot5 : snapshot5.getChildren()) {

                            stringArrayList.add(dataSnapshot5.getKey());


                        }

                        stringArrayList.add(FirebaseAuth.getInstance().getCurrentUser().getUid());

                        gonderilenlerial();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }


    private void gonderilenlerial() {

        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                gonderiArrayList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Gonderi gonderi = dataSnapshot.getValue(Gonderi.class);

                    for (String id : stringArrayList) {

                        assert gonderi != null;

                        if (gonderi.getPublisher().equals(id)) {

                            gonderiArrayList.add(gonderi);


                        }

                    }


                }

                gonderiAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void sonnmesajlar(final String userid, final ImageView zinqirov) {

        // stringlastmesaj = "default";   //

        FirebaseDatabase.getInstance().getReference("Mesagges").child(muser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            Mesajlar mesajlar = dataSnapshot.getValue(Mesajlar.class);

//                            int mesajsay = 0;
//                            int a = 0;
                            if (mesajlar.getMesajalanid() != null && mesajlar.getGoruldu() != null && muser.getUid() != null && userid != null) {

//                                if (mesajlar.getMesajalanid().equals(muser.getUid()) && mesajlar.getMesajgonderenid().equals(userid)
//                                        || mesajlar.getMesajalanid().equals(userid) && mesajlar.getMesajgonderenid().equals(muser.getUid())) {
//
//                                    //   mesajyaz.setText(mesajlar.getMesajlar());//mesajlar gondermek istediyimizde gonderdiyimiz profil
//
//
//                                }


                                if (mesajlar.getMesajalanid().equals(muser.getUid())  && !mesajlar.getGoruldu() && mesajlar.getMesajlar() != null) {

                                   // mesajsay++;

                                    zinqirov.setVisibility(View.VISIBLE);//biz mesajalan teref olduqda bu serti yazmaliyiqki notifikation gorunsun
//

                                }else
                                    zinqirov.setVisibility(View.GONE);


                            }




                        }

//                        switch (stringlastmesaj) {
//
//                            case "default":
//
//                                mesajyaz.setText("Mesaj yox");
//                                break;
//
//
//                            default:
//
//                                mesajyaz.setText(stringlastmesaj);
//
//                                break;
//
//                        }
//
//
//                        stringlastmesaj = "default";


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

}