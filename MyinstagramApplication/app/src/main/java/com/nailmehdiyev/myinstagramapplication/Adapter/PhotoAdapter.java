package com.nailmehdiyev.myinstagramapplication.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nailmehdiyev.myinstagramapplication.Fragment.ProfileFragment;
import com.nailmehdiyev.myinstagramapplication.Model.Gonderi;
import com.nailmehdiyev.myinstagramapplication.PostDetailFragment;
import com.nailmehdiyev.myinstagramapplication.R;

import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.Viewholder> {

    private Context context;
    private ArrayList<Gonderi> gonderiArrayList;


    public PhotoAdapter(Context context, ArrayList<Gonderi> gonderiArrayList) {
        this.context = context;
        this.gonderiArrayList = gonderiArrayList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.photo_item, parent, false);

        return new PhotoAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

     final    Gonderi gonderi = gonderiArrayList.get(position);

        Glide.with(context).load(gonderi.getGonderiresmi()).into(holder.resmphotogonderi);

        holder.resmphotogonderi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor sharedPreferences= context.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();

                sharedPreferences.putString("postId", gonderi.getPostid());
                sharedPreferences.apply();


                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new PostDetailFragment()).commit();


            }
        });

    }





    @Override
    public int getItemCount() {
        return gonderiArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        public ImageView resmphotogonderi;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            resmphotogonderi = itemView.findViewById(R.id.resmgondermek_photoitem);
        }
    }

}
