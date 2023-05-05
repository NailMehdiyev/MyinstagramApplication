package com.nailmehdiyev.myinstagramapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.nailmehdiyev.myinstagramapplication.Comments;
import com.nailmehdiyev.myinstagramapplication.FollowActivity;
import com.nailmehdiyev.myinstagramapplication.Fragment.ProfileFragment;
import com.nailmehdiyev.myinstagramapplication.Model.Gonderi;
import com.nailmehdiyev.myinstagramapplication.Model.Istifadeciler;
import com.nailmehdiyev.myinstagramapplication.Model.Notifications;
import com.nailmehdiyev.myinstagramapplication.PostDetailFragment;
import com.nailmehdiyev.myinstagramapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GonderiAdapter extends RecyclerView.Adapter<GonderiAdapter.ViewHolder> {


    public Context context;
    public ArrayList<Gonderi> arraylist;
    private FirebaseUser myuser;

    public GonderiAdapter(Context context, ArrayList<Gonderi> arraylist) {
        this.context = context;
        this.arraylist = arraylist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);


        return new GonderiAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        myuser = FirebaseAuth.getInstance().getCurrentUser();
        Gonderi gonderi = arraylist.get(position);

        if (gonderi.getGonderiresmi() != null) {

            Glide.with(context).load(gonderi.getGonderiresmi()).into(holder.gonderiresmi);

        }


        if (gonderi.getDescription().equals("")) {

            holder.paylasmahaqqindatextview.setVisibility(View.GONE);

        } else {

            holder.paylasmahaqqindatextview.setVisibility(View.VISIBLE);
            holder.paylasmahaqqindatextview.setText(gonderi.getDescription());

        }


        gondereninmelumatlarinal(holder.profilresmi, holder.istifadeciadi, holder.paylasanaditextview, gonderi.getPublisher());

        beyenmeniclicket(holder.beyenmeresmi, gonderi.getPublisher());
        beyenmesayisi(holder.beyenmetextview, gonderi.getPublisher());
        commentsget(gonderi.getPostid(), holder.commentlertextview);
        kaydetresmiControl(holder.kaydetresmi, gonderi.getPostid());


        holder.beyenmeresmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.beyenmeresmi.getTag().equals("beyen")) {
                    FirebaseDatabase.getInstance().getReference().child("Beyen").child(gonderi.getPublisher()).child("liked").child(myuser.getUid()).setValue(true);


                    addNotifycation(gonderi.getPublisher(), gonderi.getPostid());


                } else
                    FirebaseDatabase.getInstance().getReference().child("Beyen").child(gonderi.getPublisher()).child("liked").child(myuser.getUid()).removeValue();


            }
        });


        holder.profilresmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor sharedPreferences = context.getSharedPreferences("PROFILE", Context.MODE_PRIVATE).edit();

                sharedPreferences.putString("profileId", gonderi.getPublisher());
                sharedPreferences.apply();


                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new ProfileFragment()).commit();


            }
        });


        holder.istifadeciadi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor sharedPreferences = context.getSharedPreferences("PROFILE", Context.MODE_PRIVATE).edit();

                sharedPreferences.putString("profileId", gonderi.getPublisher());
                sharedPreferences.apply();


                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new ProfileFragment()).commit();

            }
        });


        holder.gonderiresmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor sharedPreferences = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();

                sharedPreferences.putString("postId", gonderi.getPostid());
                sharedPreferences.apply();


                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new PostDetailFragment()).commit();


            }
        });


        holder.paylasanaditextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor sharedPreferences = context.getSharedPreferences("PROFILE", Context.MODE_PRIVATE).edit();

                sharedPreferences.putString("profileId", gonderi.getPublisher());
                sharedPreferences.apply();


                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new ProfileFragment()).commit();

            }
        });


        holder.commentresmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, Comments.class);

                intent.putExtra("publisher", gonderi.getPublisher());
                intent.putExtra("postid", gonderi.getPostid());


                context.startActivity(intent);


            }
        });


        holder.commentlertextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Comments.class);

                intent.putExtra("publisher", gonderi.getPublisher());
                intent.putExtra("postid", gonderi.getPostid());


                context.startActivity(intent);

            }
        });


        holder.kaydetresmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.kaydetresmi.getTag().equals("Save")) {

                    FirebaseDatabase.getInstance().getReference().child("Saves").child(myuser.getUid()).child(gonderi.getPostid()).setValue(true);

                    //   FirebaseDatabase.getInstance().getReference().child("Saves").child(gonderi.getPostid()).child(user.getUid()).setValue(true);

                } else {

                    FirebaseDatabase.getInstance().getReference().child("Saves").child(myuser.getUid()).child(gonderi.getPostid()).removeValue();
                }


                //  FirebaseDatabase.getInstance().getReference().child("Saves").child(gonderi.getPostid()).child(user.getUid()).removeValue();

            }
        });


        holder.beyenmetextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Istifadeciler istifadeciler = new Istifadeciler();

                if (istifadeciler.getId() != null) {

                    if (istifadeciler.getId().equals(myuser.getUid())) {

                        holder.profilresmi.setVisibility(View.VISIBLE);
                        holder.istifadeciadi.setVisibility(View.VISIBLE);

                    }
                }


                Intent intent = new Intent(context, FollowActivity.class);

                intent.putExtra("id", gonderi.getPublisher());
                intent.putExtra("title", "beyenmetextview");

                context.startActivity(intent);
            }
        });


    }

    private void addNotifycation(String gonderenid, String postid) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Notifycations").child(gonderenid);

        Notifications notifications = new Notifications(myuser.getUid(), "liked your photo", postid, true);

        reference.push().setValue(notifications);
    }


    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView profilresmi;
        public ImageView gonderiresmi, beyenmeresmi, commentresmi, kaydetresmi;

        public TextView istifadeciadi, beyenmetextview, paylasanaditextview, paylasmahaqqindatextview, commentlertextview;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profilresmi = itemView.findViewById(R.id.profresm);
            gonderiresmi = itemView.findViewById(R.id.paylawilanresm);
            beyenmeresmi = itemView.findViewById(R.id.lke);
            commentresmi = itemView.findViewById(R.id.yorum1);
            kaydetresmi = itemView.findViewById(R.id.save_postitem);

            istifadeciadi = itemView.findViewById(R.id.usernametext11);
            beyenmetextview = itemView.findViewById(R.id.likestextview12);
            paylasanaditextview = itemView.findViewById(R.id.publishertext13);
            paylasmahaqqindatextview = itemView.findViewById(R.id.aboutposttext);
            commentlertextview = itemView.findViewById(R.id.commenttxt18);


        }
    }

    private void gondereninmelumatlarinal(final ImageView profilresmi, final TextView istifadeciadi, final TextView gonderenadi, final String istifadeciId) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("usaq").child(istifadeciId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Istifadeciler istifadeciler = snapshot.getValue(Istifadeciler.class);

                try {

                    if (istifadeciler.getImageurl().equals("default")) {

                        profilresmi.setImageResource(R.drawable.ic_profil);


                    } else
                        Picasso.get().load(istifadeciler.getImageurl()).into(profilresmi);


                    istifadeciadi.setText(istifadeciler.getUsername());

                    gonderenadi.setText(istifadeciler.getUsername());


                } catch (Exception e) {

                    e.printStackTrace();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void beyenmeniclicket(final ImageView image, final String istifadeciidsi) {

        final FirebaseUser movcuduser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase.getInstance().getReference().child("Beyen").child(istifadeciidsi).child("liked")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        if (snapshot.child(movcuduser.getUid()).exists()) {

                            image.setImageResource(R.drawable.ic_urek);

                            image.setTag("beyenildi");


                        } else {
                            image.setImageResource(R.drawable.ic_favor);

                            image.setTag("beyen");


                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    private void beyenmesayisi(TextView textView, String id) {


        FirebaseDatabase.getInstance().getReference().child("Beyen").child(id).child("liked").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                textView.setText(snapshot.getChildrenCount() + "beyenme");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void commentsget(String postid, final TextView textView) {


        FirebaseDatabase.getInstance().getReference().child("Comments").child(postid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        textView.setText(snapshot.getChildrenCount() + "commentlerin hamisini gor");


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }


    private void kaydetresmiControl(final ImageView imagegonderi, final String postid) {


        FirebaseDatabase.getInstance().getReference().child("Saves").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshott) {

                if (snapshott.child(postid).exists()) {

                    imagegonderi.setImageResource(R.drawable.ic_bordermm);

                    imagegonderi.setTag("Saved");

                } else {

                    imagegonderi.setImageResource(R.drawable.ic_border);

                    imagegonderi.setTag("Save");


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
