package com.nailmehdiyev.myinstagramapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nailmehdiyev.myinstagramapplication.FollowActivity;
import com.nailmehdiyev.myinstagramapplication.Fragment.ProfileFragment;

import com.nailmehdiyev.myinstagramapplication.MesajActivity;
import com.nailmehdiyev.myinstagramapplication.Model.Istifadeciler;
import com.nailmehdiyev.myinstagramapplication.Model.Mesajlar;
import com.nailmehdiyev.myinstagramapplication.Model.Notifications;

import com.nailmehdiyev.myinstagramapplication.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class IstifadecilerAdapter extends RecyclerView.Adapter<IstifadecilerAdapter.ViewHolder> {

    public Context context;
    public ArrayList<Istifadeciler> istifadecilerArrayList;
    public FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();


    public IstifadecilerAdapter(Context context, ArrayList<Istifadeciler> istifadecilerArrayList) {
        this.context = context;
        this.istifadecilerArrayList = istifadecilerArrayList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);

        return new IstifadecilerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Istifadeciler istifadeciler = istifadecilerArrayList.get(position);


      //  holder.takipbutton.setVisibility(View.VISIBLE);



        try {
            holder.istifadeciad.setText(istifadeciler.getUsername());

            holder.ad.setText(istifadeciler.getName());


            if (istifadeciler.getImageurl().equals("default")){
                holder.profresmi.setImageResource(R.drawable.ic_profil);

            } else
                Glide.with(context).load(istifadeciler.getImageurl()).into(holder.profresmi);





            if (!istifadeciler.getId().equals(mUser.getUid())){


                holder.profresmi.setVisibility(View.VISIBLE);
                holder.istifadeciad.setVisibility(View.VISIBLE);
                holder.ad.setVisibility(View.VISIBLE);
                holder.chatbutton.setVisibility(View.VISIBLE);
                holder.takipbutton.setVisibility(View.VISIBLE);

            }else {
                holder.profresmi.setVisibility(View.VISIBLE);
                holder.istifadeciad.setVisibility(View.VISIBLE);
                holder.ad.setVisibility(View.GONE);
                holder.chatbutton.setVisibility(View.GONE);
                holder.takipbutton.setVisibility(View.GONE);

                holder.online.setVisibility(View.GONE);
                holder.offline.setVisibility(View.GONE);

            }



            if (!istifadeciler.getId().equals(mUser.getUid()) && istifadeciler.getDurum().equals("online")) {

                holder.online.setVisibility(View.VISIBLE);
                holder.offline.setVisibility(View.GONE);


            }


            following(istifadeciler.getId(), holder.takipbutton);





        }catch (Exception e){

            e.printStackTrace();
        }





        holder.chatbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context, MesajActivity.class);

                intent.putExtra("userid", istifadeciler.getId());


                context.startActivity(intent);


            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor sharedPreferences = context.getSharedPreferences("PROFILE", Context.MODE_PRIVATE).edit();

                sharedPreferences.putString("profileId", istifadeciler.getId());
                sharedPreferences.apply();


                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new ProfileFragment()).commit();


            }
        });


        holder.takipbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.takipbutton.getText().toString().equals("follow")) {

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(mUser.getUid()).child("following").child(istifadeciler.getId()).setValue(true);


                    FirebaseDatabase.getInstance().getReference().child("Follow").child(istifadeciler.getId()).child("follower").child(mUser.getUid()).setValue(true);


                    addnotfy(istifadeciler.getId());



                } else {


                    FirebaseDatabase.getInstance().getReference().child("Follow").child(mUser.getUid()).child("following").child(istifadeciler.getId()).removeValue();


                    FirebaseDatabase.getInstance().getReference().child("Follow").child(istifadeciler.getId()).child("follower").child(mUser.getUid()).removeValue();


                }


            }
        });


    }





    @Override
    public int getItemCount() {
        return istifadecilerArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView profresmi, online, offline;
        public TextView istifadeciad, ad;
        public Button takipbutton;
        public ImageView chatbutton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profresmi = itemView.findViewById(R.id.circleimage);
            istifadeciad = itemView.findViewById(R.id.us);
            ad = itemView.findViewById(R.id.user_item_ad);
            takipbutton = itemView.findViewById(R.id.fol);
            chatbutton = itemView.findViewById(R.id.chatbutton);
            online = itemView.findViewById(R.id.online);
            offline = itemView.findViewById(R.id.offline);



        }
    }

    private void following(final String iid, final Button takipbutton) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Follow").child(mUser.getUid())
                .child("following");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(iid).exists()) {

                    takipbutton.setText("following");

                } else
                    takipbutton.setText("follow");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }




    private void addnotfy(String userid) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Notifycations").child(userid);

        Notifications notifications = new Notifications(mUser.getUid(), "started following you", "", false);

        reference.push().setValue(notifications);

    }



}
