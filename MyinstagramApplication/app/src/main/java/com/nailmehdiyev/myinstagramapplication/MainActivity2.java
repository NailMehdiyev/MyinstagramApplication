package com.nailmehdiyev.myinstagramapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nailmehdiyev.myinstagramapplication.Fragment.AddFragment;
import com.nailmehdiyev.myinstagramapplication.Fragment.FavorteFragment;
import com.nailmehdiyev.myinstagramapplication.Fragment.HomeFragment;
import com.nailmehdiyev.myinstagramapplication.Fragment.ProfileFragment;
import com.nailmehdiyev.myinstagramapplication.Fragment.SearchFragment;
import com.nailmehdiyev.myinstagramapplication.Model.Istifadeciler;
import com.nailmehdiyev.myinstagramapplication.Model.Mesajlar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class MainActivity2 extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private FragmentTransaction transaction;

    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private AddFragment addFragment;
    private FavorteFragment favorteFragment;
    private ProfileFragment profileFragment;
    private String gonderen;
    private Toolbar toolbar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        addFragment = new AddFragment();
        favorteFragment = new FavorteFragment();
        profileFragment = new ProfileFragment();



        bottomNavigationView = findViewById(R.id.naviqation);
        // toolbar = findViewById(R.id.toolbar5);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//


        Bundle intent = getIntent().getExtras();

        if (intent != null) {

            gonderen = intent.getString("gonderenid");

            SharedPreferences.Editor editor = getSharedPreferences("PROFILE", MODE_PRIVATE).edit();

            editor.putString("profileId", gonderen);

            editor.apply();


            getFragment(new ProfileFragment());

        } else
            getFragment(new HomeFragment());








        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {

                    case R.id.homeid:

                        getFragment(new HomeFragment());

                        break;

                    case R.id.searchid:
                        getFragment(new SearchFragment());

                        break;


                    case R.id.addid:

                        startActivity(new Intent(MainActivity2.this, PostActivity.class));
                        finish();
                        break;


                    case R.id.favoritid:

                        getFragment(new FavorteFragment());
                        break;


                    case R.id.personid:

                        SharedPreferences.Editor sharedPreferences = MainActivity2.this.getSharedPreferences("PROFILE", MODE_PRIVATE).edit();

                        sharedPreferences.putString("profileId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        sharedPreferences.apply();

                        getFragment(new ProfileFragment());
                        break;


                }
                return true;
            }
        });


    }


    private void getFragment(Fragment fragment) {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout, fragment).commit();


    }


    private void durumumueyyenet(final String durum) {


        HashMap<String, Object> map = new HashMap<>();
        map.put("durum", durum);
if (FirebaseAuth.getInstance().getUid()!=null){
    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).updateChildren(map);

}else{
    Intent intent = new Intent(MainActivity2.this, MainActivity.class);
    startActivity(intent);
    finish();

}

    }


    @Override
    protected void onStart() {

        durumumueyyenet("online");

        super.onStart();
    }


    @Override
    protected void onStop() {

        durumumueyyenet("offline");

        super.onStop();
    }






//    class Viewpageradapter extends FragmentStatePagerAdapter {
//
//        private ArrayList<Fragment> fragmentArrayList;
//        private ArrayList<String> titles;
//
//        public Viewpageradapter(@NonNull FragmentManager fm) {
//            super(fm);
//            this.fragmentArrayList = fragmentArrayList;
//            this.titles = titles;
//        }
//
//
//        @NonNull
//        @Override
//        public Fragment getItem(int position) {
//            return fragmentArrayList.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return fragmentArrayList.size();
//        }
//
//
//        public void addFragment(Fragment fragment, String title) {
//
//            fragmentArrayList.add(fragment);
//            titles.add(title);
//
//        }
//
//
//
//
//
//
//        //ctrl+O  ------ Char search
//        @Nullable
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return titles.get(position);
//        }
//    }





}