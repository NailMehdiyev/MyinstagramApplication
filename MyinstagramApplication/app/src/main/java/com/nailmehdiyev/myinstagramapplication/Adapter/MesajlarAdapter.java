package com.nailmehdiyev.myinstagramapplication.Adapter;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nailmehdiyev.myinstagramapplication.Model.Mesajlar;
import com.nailmehdiyev.myinstagramapplication.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class MesajlarAdapter extends RecyclerView.Adapter<MesajlarAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Mesajlar> mesajlarArrayList;
    public static final int Mesagright = 0, Mesagleft = 1;
    private FirebaseUser muser = FirebaseAuth.getInstance().getCurrentUser();
    private Boolean isgoruldu;

    private int mesajstatus = -1;


    public MesajlarAdapter(Context context, ArrayList<Mesajlar> mesajlarArrayList, Boolean isgoruldu) {
        this.context = context;
        this.mesajlarArrayList = mesajlarArrayList;
        this.isgoruldu = isgoruldu;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == Mesagright) {

            View view = LayoutInflater.from(context).inflate(R.layout.sag_item, parent, false);

            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.sol_item, parent, false);
            return new MesajlarAdapter.ViewHolder(view);

        }


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        position = holder.getAdapterPosition();

        final Mesajlar mesajlar = mesajlarArrayList.get(position);

        //  String timestamp = mesajlarArrayList.get(holder.getAdapterPosition()).getTarix();

//        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
//        calendar.setTimeInMillis(Long.parseLong(timestamp));
//        String datetime=DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString;


        holder.txtmesajtarix.setText(mesajlar.getTarix());



        if (position == mesajlarArrayList.size() - 1) { //bu -1 o demekdirki mesaj en sonuncu positiondadirsa gostersin
            if (mesajlar.getGoruldu()) {

                holder.txtgoruldu.setText("Seen");

            } else
                holder.txtgoruldu.setText("Delivered");

        } else {
            holder.txtgoruldu.setVisibility(View.GONE);
        }


        holder.txtmesaj.setText(mesajlar.getMesajlar());
        holder.txtmesajtarix.setText(mesajlar.getTarix());
        Glide.with(context).load(mesajlar.getResm()).into(holder.gonderilenresm);


        if (mesajlar.getResm().equals("")) {
            holder.gonderilenresm.setVisibility(View.GONE);
        }


        if (mesajlar.getMesajlar() != null) {

            holder.txtmesaj.setVisibility(View.VISIBLE);

        } else
            holder.txtmesaj.setVisibility(View.GONE);


        int finalPosition = position;
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mesajstatus = finalPosition;

                notifyDataSetChanged();
            }
        });


        try {


            if (mesajstatus == position) {
                holder.relativeLayout.setBackgroundColor(Color.parseColor("#A19999"));
                holder.sil.setVisibility(View.VISIBLE);
                holder.copyala.setVisibility(View.VISIBLE);
            }


            holder.sil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("Delete");
                    builder.setMessage("Are you sure delete this message?");
                    builder.setCancelable(false);

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            mesajsil();

                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.show();

                }
            });


        } catch (Exception e) {

            e.printStackTrace();
        }


        holder.copyala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

                ClipData clipData = ClipData.newPlainText("", holder.txtmesaj.getText());

                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(context, "is Copyied", Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void mesajsil() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Mesagges").child(muser.getUid());

        String msjid = databaseReference.push().getKey();


        databaseReference.child(msjid).removeValue();

    }

    @Override
    public int getItemCount() {
        return mesajlarArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtmesaj, txtmesajtarix, txtgoruldu, txtsaat;
        public ImageView gonderilenresm, sil, copyala;
        public RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtmesaj = itemView.findViewById(R.id.item_mesaj);
            txtmesajtarix = itemView.findViewById(R.id.item_tarix);
            // txtsaat = itemView.findViewById(R.id.item_saat);
            txtgoruldu = itemView.findViewById(R.id.item_goruldu);
            gonderilenresm = itemView.findViewById(R.id.mesajresm);
            sil = itemView.findViewById(R.id.sildelete);
            copyala = itemView.findViewById(R.id.copyala);
            relativeLayout = itemView.findViewById(R.id.relativelayoutenn);


        }
    }


    @Override
    public int getItemViewType(int position) {

        if (mesajlarArrayList.get(position).getMesajgonderenid().equals(muser.getUid())) {

            return Mesagright;

        } else
            return Mesagleft;

    }
}
