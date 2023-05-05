package com.nailmehdiyev.myinstagramapplication.Fragment;


import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nailmehdiyev.myinstagramapplication.Adapter.PhotoAdapter;
import com.nailmehdiyev.myinstagramapplication.EditProfile;
import com.nailmehdiyev.myinstagramapplication.FollowActivity;
import com.nailmehdiyev.myinstagramapplication.LoginActivity;
import com.nailmehdiyev.myinstagramapplication.MainActivity;
import com.nailmehdiyev.myinstagramapplication.Model.Gonderi;
import com.nailmehdiyev.myinstagramapplication.Model.Istifadeciler;
import com.nailmehdiyev.myinstagramapplication.Model.Notifications;
import com.nailmehdiyev.myinstagramapplication.R;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private CircleImageView profil;
    private ImageView resmsecimi;
    private ImageButton imagbtnresmlerim, imagbtnresmlerikaydet;
    private TextView istifadeciad, ad, biografi, gonderilenler, follower, following;
    private Button buttonedit;
    private String profileid;
    private FirebaseUser user;

    private ArrayList<Gonderi> gonderiArrayList;
    private PhotoAdapter photoAdapter;
    private RecyclerView recyclerViewphoto;


    private ArrayList<Gonderi> gonderisaveArrayList;
    private PhotoAdapter photosaveAdapter;
    private RecyclerView recyclerViewsavephoto;


    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profil = view.findViewById(R.id.circleimage_profilfragment);
        resmsecimi = view.findViewById(R.id.profsecimler_profilefragment);


        imagbtnresmlerikaydet = view.findViewById(R.id.profilfragment_kaydetmek);
        imagbtnresmlerim = view.findViewById(R.id.profilfragment_resmlerim);
        istifadeciad = view.findViewById(R.id.istifadeciadi_profilefragment);
        ad = view.findViewById(R.id.ad_profilfragment);
        biografi = view.findViewById(R.id.bioqrafiya_profilfragment);
        gonderilenler = view.findViewById(R.id.gonderilener_profilfragment);
        follower = view.findViewById(R.id.follower_profilfragment);
        following = view.findViewById(R.id.following_profilfragment);
        buttonedit = view.findViewById(R.id.button_profilfragment);

        recyclerViewphoto = view.findViewById(R.id.recyclerview_profilfragment);
        recyclerViewphoto.setHasFixedSize(true);
        recyclerViewphoto.setLayoutManager(new GridLayoutManager(getContext(), 3));

        gonderiArrayList = new ArrayList<>();

        photoAdapter = new PhotoAdapter(getContext(), gonderiArrayList);

        recyclerViewphoto.setAdapter(photoAdapter);

        recyclerViewphoto.setVisibility(View.VISIBLE);


        recyclerViewsavephoto = view.findViewById(R.id.recyclerview_profilfragment2);
        recyclerViewsavephoto.setHasFixedSize(true);
        recyclerViewsavephoto.setLayoutManager(new GridLayoutManager(getContext(), 3));

        gonderisaveArrayList = new ArrayList<>();

        photosaveAdapter = new PhotoAdapter(getContext(), gonderisaveArrayList);

        recyclerViewsavephoto.setAdapter(photosaveAdapter);


        imagbtnresmlerim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recyclerViewphoto.setVisibility(View.VISIBLE);

                recyclerViewsavephoto.setVisibility(View.GONE);

            }
        });


        imagbtnresmlerikaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recyclerViewsavephoto.setVisibility(View.VISIBLE);
                recyclerViewphoto.setVisibility(View.GONE);

            }
        });


        resmsecimi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(requireActivity(), v);

                popupMenu.getMenuInflater().inflate(R.menu.ustmenu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.logoutid:

                                FirebaseAuth.getInstance().signOut();

                                break;


                            default:
                                return false;

                        }

                        return true;
                    }


                });
                popupMenu.show();
            }
        });


        follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getContext(), FollowActivity.class);
                intent.putExtra("id", profileid);
                intent.putExtra("title", "follower");

                startActivity(intent);


            }
        });


        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), FollowActivity.class);
                intent.putExtra("id", profileid);
                intent.putExtra("title", "following");

                startActivity(intent);


            }
        });


        user = FirebaseAuth.getInstance().getCurrentUser();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE);

        String data = sharedPreferences.getString("profileId", "none");


        if (data.equals("none")) {

            profileid = user.getUid();

        } else {
            profileid = data;
            getContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).edit().clear().apply();

        }


        istifadecilerinmelumatinal();

        takibcilervetakibedilenlerial();

        gonderilenlrial();


        myphototake();

        resmlerisaveet();


        if (profileid.equals(user.getUid())) {

            buttonedit.setText("Profile edit");

        } else {

            takibkontrolet();

            imagbtnresmlerikaydet.setVisibility(View.GONE);


        }


        buttonedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String button = buttonedit.getText().toString();


                if (button.equals("Profile edit")) {


                    startActivity(new Intent(getContext(), EditProfile.class));

                } else if (button.equals("follow")) {

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getUid()).child("following").child(profileid).setValue(true);


                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid).child("follower").child(user.getUid()).setValue(true);

                    addNotifycation(profileid);//yeniden eger profilinden follow atsaq o zamanda notifications versin bize


                } else if (button.equals("following")) {


                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getUid()).child("following").child(profileid).removeValue();


                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid).child("follower").child(user.getUid()).removeValue();


                }


            }
        });


        return view;
    }

    private void addNotifycation(String profileid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Notifycations").child(profileid);

        Notifications notifications = new Notifications(user.getUid(), "started following you", "", false);

        reference.push().setValue(notifications);
    }


    private void takibkontrolet() {

        FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getUid()).child("following")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        if (snapshot.child(profileid).exists()) {


                            buttonedit.setText("following");


                        } else
                            buttonedit.setText("follow");


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }


    private void istifadecilerinmelumatinal() {


        FirebaseDatabase.getInstance().getReference().child("users").child(profileid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        if (getContext() == null) {

                            return;
                        }

                        Istifadeciler istifadeciler = snapshot.getValue(Istifadeciler.class);

                        istifadeciad.setText(istifadeciler.getUsername());
                        ad.setText(istifadeciler.getName());

                        Glide.with(getContext()).load(istifadeciler.getImageurl()).into(profil);

                        biografi.setText(istifadeciler.getBio());


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }


    private void takibcilervetakibedilenlerial() {

        FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid).child("follower")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        follower.setText(String.valueOf(snapshot.getChildrenCount()));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid).child("following")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        following.setText(String.valueOf(snapshot.getChildrenCount()));


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }


    private void gonderilenlrial() {


        FirebaseDatabase.getInstance().getReference("Posts")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        int say = 0;

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            Gonderi gonderi = dataSnapshot.getValue(Gonderi.class);

                            if (gonderi.getPublisher().equals(profileid)) {

                                say++;

                                gonderilenler.setText(String.valueOf(say));

                            }


                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }


    private void myphototake() {


        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                gonderiArrayList.clear();


                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Gonderi gonderi = dataSnapshot.getValue(Gonderi.class);

                    if (gonderi.getPublisher().equals(profileid)) {

                        gonderiArrayList.add(gonderi);

                    }


                }

                Collections.reverse(gonderiArrayList);

                photoAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void resmlerisaveet() {

        ////burani ona gore edirikki Saves etdiyin qarsiteref istifadecinin post idsini aliriq daha sonra Posts dan gonderilenleri cagiraraq hemin qarsiterefin postid si gonderi clasindaki postid e beraberse ordaki butun melumatlari atiriq listeye vetricalformada recyclerview formada.
        final ArrayList<String> resmlersaveidlist = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("Saves").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            resmlersaveidlist.add(dataSnapshot.getKey());

                        }//buradan postidini aldigimiz ucun asagidada post referencdan postidni cagirib bir birine beraberlesdiririkki eger beraberdirse gelsin listenin icine atsin
                        //eger oradan post yox qarwiterefin istifadeci idsin alsaydiq asagidaki post referencdan qarwiteref idsini cagiracaqdiq
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot1) {

                gonderisaveArrayList.clear();

                for (DataSnapshot dataSnapshot1 : snapshot1.getChildren()) {

                    for (String stringid : resmlersaveidlist) {

                        Gonderi gonderi = dataSnapshot1.getValue(Gonderi.class);

                        if (gonderi.getPostid().equals(stringid)) {

                            gonderisaveArrayList.add(gonderi);

                        }


                    }

                    photosaveAdapter.notifyDataSetChanged();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}



