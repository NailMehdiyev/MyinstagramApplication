package com.nailmehdiyev.myinstagramapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.nailmehdiyev.myinstagramapplication.Fragment.HomeFragment;
import com.nailmehdiyev.myinstagramapplication.Fragment.ProfileFragment;
import com.nailmehdiyev.myinstagramapplication.MesajActivity;
import com.nailmehdiyev.myinstagramapplication.Model.Istifadeciler;
import com.nailmehdiyev.myinstagramapplication.Model.Mesajlar;
import com.nailmehdiyev.myinstagramapplication.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DirektmesajAdapter extends RecyclerView.Adapter<DirektmesajAdapter.ViewHolder> {


    private Context context;
    private ArrayList<Istifadeciler> istifadecilerArrayList;
    private Boolean isChat;
    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private DirektmesajAdapter direktmesajAdapter;
    private OnItemclicklistener onitemclicklistener;


    public interface OnItemclicklistener {

        void onitemclick(int position);
        void ondeleteclick(int position);
    }

    public void setOnitemclicklistener(OnItemclicklistener listener) {

        onitemclicklistener = listener;

    }


    public String stringlastmesaj;

    public DirektmesajAdapter(Context context, ArrayList<Istifadeciler> istifadecilerArrayList, Boolean isChat) {
        this.context = context;
        this.istifadecilerArrayList = istifadecilerArrayList;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.direktmesaj_item, parent, false);

        return new DirektmesajAdapter.ViewHolder(view, onitemclicklistener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Istifadeciler istifadeciler = istifadecilerArrayList.get(position);


        holder.textistifadead.setText(istifadeciler.getUsername());

        if (istifadeciler.getId() != null) {

            if (istifadeciler.getId().equals(mUser.getUid())) {

                holder.profilresm.setVisibility(View.GONE);
                holder.textistifadead.setVisibility(View.GONE);
                holder.textmesaj.setVisibility(View.GONE);


            }

        }
        if (isChat) {
            sonnmesajlar(istifadeciler.getId(), holder.textmesaj, holder.gedenmesaj, holder.gelenmesaj);

        } else {
            holder.textmesaj.setVisibility(View.GONE);
        }


        if (istifadeciler.getImageurl().equals("default")) {

            holder.profilresm.setImageResource(R.drawable.ic_profil);

        } else
            Glide.with(context).load(istifadeciler.getImageurl()).into(holder.profilresm);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, MesajActivity.class);
                intent.putExtra("userid", istifadeciler.getId());
                context.startActivity(intent);


                holder.textmesaj.setVisibility(View.GONE);
                sonnmesajlar(istifadeciler.getId(), holder.textmesaj, holder.gedenmesaj, holder.gelenmesaj);
                holder.textmesaj.setVisibility(View.VISIBLE);

                holder.gelenmesaj.setVisibility(View.GONE);


                if (onitemclicklistener != null) {

                    int position = holder.getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {

                        onitemclicklistener.onitemclick(holder.getAdapterPosition());
                    }

                }

            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                holder.relativeLayout.setBackgroundColor(Color.parseColor("#A19999"));
                holder.sil.setVisibility(View.VISIBLE);

                return true;
            }
        });




        holder.sil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            // FirebaseDatabase.getInstance().getReference("Mesajlar").child(mUser.getUid()).child(FirebaseAuth.getInstance().getUid()).removeValue();


               // FirebaseDatabase.getInstance().getReference("Mesajlar").child(FirebaseAuth.getInstance().getUid()).removeValue();



            }
        });



    }


    private void sonnmesajlar(final String userid, final TextView mesajyaz, final ImageView gedenmesaj, final ImageView gelenmesaj) {

        // stringlastmesaj = "default";   //

        FirebaseDatabase.getInstance().getReference("Mesagges").child(mUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            Mesajlar mesajlar = dataSnapshot.getValue(Mesajlar.class);

                            int mesajsay = 0;
                            int a = 0;
                            if (mesajlar.getMesajalanid() != null && mesajlar.getGoruldu() != null && mUser.getUid() != null && userid != null) {

                                if (mesajlar.getMesajalanid().equals(mUser.getUid()) && mesajlar.getMesajgonderenid().equals(userid)
                                        || mesajlar.getMesajalanid().equals(userid) && mesajlar.getMesajgonderenid().equals(mUser.getUid())) {

                                    mesajyaz.setText(mesajlar.getMesajlar());//mesajlar gondermek istediyimizde gonderdiyimiz profil


                                }


                                if (mesajlar.getMesajalanid().equals(mUser.getUid()) && mesajlar.getMesajgonderenid().equals(userid) && !mesajlar.getGoruldu() && mesajlar.getMesajlar() != null) {

                                    mesajsay++;

                                    mesajyaz.setText(mesajsay + "yeni mesaj");//mesajgelende mesaji qebul eden profilteref


                                    gelenmesaj.setVisibility(View.VISIBLE);

                                }


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


    @Override
    public int getItemCount() {
        return istifadecilerArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView profilresm;
        private TextView textistifadead;
        private TextView textmesaj;
        private ImageView gedenmesaj, gelenmesaj, sil;
        private RelativeLayout relativeLayout;


        public ViewHolder(@NonNull View itemView, OnItemclicklistener listener) {
            super(itemView);

            profilresm = itemView.findViewById(R.id.direktmesaj_circleimage);
            textistifadead = itemView.findViewById(R.id.direktmesaj_us);
            textmesaj = itemView.findViewById(R.id.direktmesaj_mesajyazmaq);
            gedenmesaj = itemView.findViewById(R.id.direktmesaj_geden);
            gelenmesaj = itemView.findViewById(R.id.direktmesaj_gelen);
            sil = itemView.findViewById(R.id.direktactivity__sil);

            relativeLayout = itemView.findViewById(R.id.uuu);





        }
    }


//    private void gelenmesajsaylarinal(TextView textView, String muser, String istifadeciid,ImageView gelenmesaj) {
//
//        FirebaseDatabase.getInstance().getReference().child("Mesajlar").child(istifadeciid)
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//
//                            int mesajsay = 0;
//                            Mesajlar mesajlar = dataSnapshot.getValue(Mesajlar.class);
//
//
//                            if (mesajlar.getMesajalanid().equals(muser)) {
//
//
//
//                                mesajsay++;
//                                textView.setText(mesajsay+"yeni mesaj");
//
//                                gelenmesaj.setVisibility(View.VISIBLE);
//
//
//                            }
//
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//
//    }


}
