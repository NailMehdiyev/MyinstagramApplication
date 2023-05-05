package com.nailmehdiyev.myinstagramapplication.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
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
import com.nailmehdiyev.myinstagramapplication.Adapter.NotificationAdapter;
import com.nailmehdiyev.myinstagramapplication.Model.Notifications;
import com.nailmehdiyev.myinstagramapplication.R;

import java.util.ArrayList;
import java.util.Collections;

public class FavorteFragment extends Fragment {

        private RecyclerView recyclerView;
        private ArrayList<Notifications>notificationsArrayList;
        private NotificationAdapter notificationAdapter;
        private FirebaseUser mUser=FirebaseAuth.getInstance().getCurrentUser();

        private Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_favorte, container, false);

       // toolbar=view.findViewById(R.id.favorittol);



        recyclerView=view.findViewById(R.id.fovoritefragment_rec);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));

        notificationsArrayList=new ArrayList<>();

        notificationAdapter=new NotificationAdapter(view.getContext(),notificationsArrayList);

        recyclerView.setAdapter(notificationAdapter);




        getnotfy();




        return view;
    }

    private void getnotfy() {

        FirebaseDatabase.getInstance().getReference().child("Notifycations").child(mUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        notificationsArrayList.clear();

                        for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                            Notifications notifications=dataSnapshot.getValue(Notifications.class);

                            notificationsArrayList.add(notifications);

                        }

                        Collections.reverse(notificationsArrayList);
                        notificationAdapter.notifyDataSetChanged();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}