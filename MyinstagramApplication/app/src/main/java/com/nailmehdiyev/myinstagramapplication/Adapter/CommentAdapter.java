package com.nailmehdiyev.myinstagramapplication.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nailmehdiyev.myinstagramapplication.Model.Comment;
import com.nailmehdiyev.myinstagramapplication.MainActivity2;
import com.nailmehdiyev.myinstagramapplication.Model.Istifadeciler;
import com.nailmehdiyev.myinstagramapplication.R;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Comment> commentArrayList;
    private FirebaseUser firebaseUser;
    private String postid;

    public CommentAdapter(Context context, ArrayList<Comment> commentArrayList,String postid) {
        this.context = context;
        this.commentArrayList = commentArrayList;
        this.postid=postid;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false);

        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final Comment comment = commentArrayList.get(position);

            //duzelt burani

            holder.commentyaz.setText(comment.getComment());


        istifadecimeluymatlarinial(holder.profil, holder.istifadeciad, comment.getGonderen());


        holder.commentyaz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity2.class);

                intent.putExtra("gonderenid", comment.getGonderen());

                context.startActivity(intent);

            }
        });


        holder.profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity2.class);

                intent.putExtra("gonderenid", comment.getGonderen());

                context.startActivity(intent);

            }
        });



        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if(comment.getGonderen().endsWith(firebaseUser.getUid())){

                    AlertDialog.Builder  alertDialog=new AlertDialog.Builder(context);
                    alertDialog.setTitle("are you sure delete?");

                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();

                        }
                    });




                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            FirebaseDatabase.getInstance().getReference().child("Comments").child(postid).child(comment.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        Toast.makeText(context, "Comment is succesful", Toast.LENGTH_SHORT).show();



                                        dialog.dismiss();

                                        notifyDataSetChanged();

                                    }

                                }
                            });


                        }
                    });

                    alertDialog.show();

                }
                return true;




            }
        });





    }

    @Override
    public int getItemCount() {

        return commentArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView profil;
        public TextView istifadeciad, commentyaz;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profil = itemView.findViewById(R.id.circ);
            istifadeciad = itemView.findViewById(R.id.txtis);
            commentyaz = itemView.findViewById(R.id.constausernam);
        }
    }


    private void istifadecimeluymatlarinial(final ImageView profilrsm, final TextView istifadeciad, String gonderenid) {

        FirebaseDatabase.getInstance().getReference().child("users").child(gonderenid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Istifadeciler istifadeciler = snapshot.getValue(Istifadeciler.class);



               try {
                   istifadeciad.setText(istifadeciler.getUsername());

                   if(istifadeciler.getImageurl().equals("default")){

                       profilrsm.setImageResource(R.drawable.ic_profil);

                   }else
                       Glide.with(context).load(istifadeciler.getImageurl()).into(profilrsm);


               }catch (Exception e){
                   e.printStackTrace();
               }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }






}
