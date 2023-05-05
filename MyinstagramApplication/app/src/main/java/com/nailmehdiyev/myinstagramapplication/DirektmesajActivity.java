package com.nailmehdiyev.myinstagramapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nailmehdiyev.myinstagramapplication.Adapter.DirektmesajAdapter;
import com.nailmehdiyev.myinstagramapplication.Model.Istifadeciler;
import com.nailmehdiyev.myinstagramapplication.Model.Mesajlar;

import java.util.ArrayList;

public class DirektmesajActivity extends AppCompatActivity {


    private ArrayList<String> stringArrayList = new ArrayList<>();
    private ArrayList<Istifadeciler> istifadecilerArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DirektmesajAdapter direktmesajAdapter;
    private FirebaseUser muser;
    private Toolbar toolbar;
    private ImageView sil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direktmesaj);

        toolbar = findViewById(R.id.direktactivity__rel);
        //sil=findViewById(R.id.direktactivity__sil);


//        sil.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                for (int i=0;i<istifadecilerArrayList.size();i++){
//                    istifadecilerArrayList.remove(i);
//                    direktmesajAdapter.notifyDataSetChanged();
//
//                    break;
//
//                }


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Direktmessages");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        muser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = findViewById(R.id.direktmesaj_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        direktmesajAdapter = new DirektmesajAdapter(this, istifadecilerArrayList, true);

        //  new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(direktmesajAdapter);


        direktmesajAdapter.notifyDataSetChanged();


        mesajlarlistesinial();


        direktmesajAdapter.setOnitemclicklistener(new DirektmesajAdapter.OnItemclicklistener() {
            @Override
            public void onitemclick(int position) {

                Toast.makeText(DirektmesajActivity.this, "Position"+position, Toast.LENGTH_SHORT).show();

            }


            @Override
            public void ondeleteclick(int position) {
                istifadecilerArrayList.remove(position);
                direktmesajAdapter.notifyDataSetChanged();

            }
        });


    }


    private void mesajlarlistesinial() {

        stringArrayList = new ArrayList<>();


        FirebaseDatabase.getInstance().getReference().child("Mesagges").child(muser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        stringArrayList.clear();

                        if (snapshot.exists()) {

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                Mesajlar mesajlar = dataSnapshot.getValue(Mesajlar.class);


                                if (mesajlar.getMesajgonderenid() != null && muser.getUid() != null) {
                                    if (mesajlar.getMesajgonderenid().equals(muser.getUid())) {

                                        stringArrayList.add(mesajlar.getMesajalanid());
                                    }

                                }


                                if (mesajlar.getMesajalanid() != null && muser != null) {

                                    if (mesajlar.getMesajalanid().equals(muser.getUid())) {

                                        stringArrayList.add(mesajlar.getMesajgonderenid());

                                    }


                                }


                            }

                        }

                        istifadecilerial();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        // 2ci ucun


    }


    private void istifadecilerial() {


        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                istifadecilerArrayList.clear();

                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {

                    Istifadeciler istifadeciler = dataSnapshot1.getValue(Istifadeciler.class);


                    for (String id : stringArrayList) {

                        if (id != null && istifadeciler.getId() != null && istifadeciler.getId().equals(id) && !istifadecilerArrayList.contains(istifadeciler)) {

                            istifadecilerArrayList.add(istifadeciler);


                            if (istifadecilerArrayList.size() != 0) {


                            } else {
                                istifadecilerArrayList.add(istifadeciler);
                            }

                        }


                    }


                }
                direktmesajAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {


            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

//            istifadecilerArrayList.remove(viewHolder.getAdapterPosition());
//            direktmesajAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
        }
    };

}