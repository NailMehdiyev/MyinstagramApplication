package com.nailmehdiyev.myinstagramapplication.Adapter;

import android.content.Context;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nailmehdiyev.myinstagramapplication.Fragment.ProfileFragment;
import com.nailmehdiyev.myinstagramapplication.Model.Gonderi;
import com.nailmehdiyev.myinstagramapplication.Model.Istifadeciler;
import com.nailmehdiyev.myinstagramapplication.Model.Notifications;
import com.nailmehdiyev.myinstagramapplication.PostDetailFragment;
import com.nailmehdiyev.myinstagramapplication.R;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {


    private Context context;
    private ArrayList<Notifications> notificationsArrayList;
    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

    public NotificationAdapter(Context context, ArrayList<Notifications> notificationsArrayList) {
        this.context = context;
        this.notificationsArrayList = notificationsArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.notify_item, parent, false);

        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Notifications notifications = notificationsArrayList.get(position);

        getUser(holder.profilimageview, holder.userad, notifications.getUserid());

        holder.textcomment.setText(notifications.getText());

        if (notifications.isPost() && !notifications.getUserid().equals(mUser.getUid())) {

            holder.profilimageview.setVisibility(View.VISIBLE);
            holder.userad.setVisibility(View.VISIBLE);
            holder.textcomment.setVisibility(View.VISIBLE);
            holder.postimageview.setVisibility(View.VISIBLE);
            getImagePost(holder.postimageview, notifications.getPostid());


        } else{
            holder.profilimageview.setVisibility(View.GONE);
            holder.userad.setVisibility(View.GONE);
            holder.textcomment.setVisibility(View.GONE);
            holder.postimageview.setVisibility(View.GONE);

        }









        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (notifications.isPost()) {

                    SharedPreferences.Editor sharedPreferences = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();

                    sharedPreferences.putString("postId", notifications.getPostid());
                    sharedPreferences.apply();


                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new PostDetailFragment()).commit();


                } else {
                    SharedPreferences.Editor sharedPreferences = context.getSharedPreferences("PROFILE", Context.MODE_PRIVATE).edit();

                    sharedPreferences.putString("profileId", notifications.getUserid());
                    sharedPreferences.apply();

                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new ProfileFragment()).commit();

                }


            }
        });

    }

    private void getImagePost(ImageView postimageview, String postid) {


        FirebaseDatabase.getInstance().getReference().child("Posts").child(postid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Gonderi gonderi = snapshot.getValue(Gonderi.class);

                        Glide.with(context).load(gonderi.getGonderiresmi()).into(postimageview);//burada menim resmime like yada comment yazilacag deye menim yada herhansisa paylasilan istifadecinin resmi goturulur

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void getUser(ImageView profilimageview, TextView userad, String istifadeciId) {

        FirebaseDatabase.getInstance().getReference().child("users").child(istifadeciId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Istifadeciler istifadeciler = snapshot.getValue(Istifadeciler.class);


                        try {
                            Glide.with(context).load(istifadeciler.getImageurl()).into(profilimageview);

                            userad.setText(istifadeciler.getUsername());


                        }catch (Exception e){

                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


    @Override
    public int getItemCount() {
        return notificationsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private ImageView profilimageview, postimageview;
        private TextView userad, textcomment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userad = itemView.findViewById(R.id.notfyitem_usernam);
            textcomment = itemView.findViewById(R.id.notfyitem_txt);
            profilimageview = itemView.findViewById(R.id.notfyitem_circle);
            postimageview = itemView.findViewById(R.id.notfyitem_postimage);

        }
    }


}
