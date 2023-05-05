package com.nailmehdiyev.myinstagramapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nailmehdiyev.myinstagramapplication.Adapter.MesajlarAdapter;
import com.nailmehdiyev.myinstagramapplication.Model.Comment;
import com.nailmehdiyev.myinstagramapplication.Model.Istifadeciler;
import com.nailmehdiyev.myinstagramapplication.Model.Mesajlar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MesajActivity extends AppCompatActivity {

    private ImageView profilresm, fotograf, send, back;
    private TextView istifadeciad;
    private EditText mesaj;
    private RecyclerView recyclerView;
    private ArrayList<Mesajlar> mesajlarArrayList = new ArrayList<>();
    private MesajlarAdapter mesajlarAdapter;


    private FirebaseUser MUser = FirebaseAuth.getInstance().getCurrentUser();
    private String userid;
    private ValueEventListener value;
    private DatabaseReference databaseReference;
    private Uri resmuri;

    private StringBuilder tarix = new StringBuilder();
    private StringBuilder saat = new StringBuilder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesaj);

        profilresm = findViewById(R.id.mesajactivity_profilresm);
        fotograf = findViewById(R.id.mesajactivity_fotograf);
        send = findViewById(R.id.mesajactivity_send);
        istifadeciad = findViewById(R.id.mesajactivity_textad);
        mesaj = findViewById(R.id.mesajactivity_editcomment);
        back = findViewById(R.id.mesajactivity_back);


//
//        Date bugun = Calendar.getInstance().getTime();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
//        String data = simpleDateFormat.format(bugun);
//        tarix.append(data);
//
//
//        Date saatvaxt = Calendar.getInstance().getTime();
//        SimpleDateFormat simpleDateFormatsaat = new SimpleDateFormat("hh:mm");
//        String datasaat = simpleDateFormatsaat.format(saatvaxt);
//        saat.append(datasaat);
//
//        Date bugun = Calendar.getInstance().getTime();
//        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy hh:mm a");
//        String stringdate= simpleDateFormat.format(bugun);
//        tarix.append(stringdate);
////


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        fotograf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ImagePicker.with(MesajActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(2048)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(400, 300)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();


            }
        });


        recyclerView = findViewById(R.id.mesajactivity_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mesajlarAdapter = new MesajlarAdapter(this, mesajlarArrayList, true);

        recyclerView.setAdapter(mesajlarAdapter);


        Intent intent = getIntent();

        userid = intent.getStringExtra("userid");

        istifadecimelumatinal(istifadeciad, userid);


        goruldu();


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String edittextmesaj = mesaj.getText().toString();

                if (!TextUtils.isEmpty(edittextmesaj)) {

                    mesajgonderen();


                    mesajalan();


                } else {
                    Toast.makeText(MesajActivity.this, "Empty credentials", Toast.LENGTH_SHORT).show();
                }


                mesaj.setText("");

            }


        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == ImagePicker.REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            resmuri = data.getData();
            resmyukle();

        }


        super.onActivityResult(requestCode, resultCode, data);
    }


    private void resmyukle() {

        if (resmuri != null) {

            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Mesaggesimages").child(System.currentTimeMillis() + "." + MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().getType(resmuri)));

            storageReference.putFile(resmuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {


                            String uristring = uri.toString();


                            //burasida oz idmizin altina mesajyoluyla resm gondererken olan yol  her ikisinde olmalidirki gonderdiyimiz resmi bizde gorebilek qarsiterefde
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Mesagges").child(MUser.getUid());

                            String postid = databaseReference.push().getKey();


                            Mesajlar mesajlar = new Mesajlar("", MUser.getUid(), userid, uristring, tarix.toString(),false);


                            databaseReference.child(postid).setValue(mesajlar);


                            //qarsiterefin idsinin altina mesajyoluyla resm gonderereken

                            DatabaseReference databaseReferences = FirebaseDatabase.getInstance().getReference().child("Mesagges").child(userid);

                            String postidd = databaseReferences.push().getKey();


                            Mesajlar mesajlarr = new Mesajlar("", MUser.getUid(), userid, uristring, tarix.toString(), false);


                            databaseReferences.child(postidd).setValue(mesajlarr);


                            Toast.makeText(MesajActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MesajActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });


        }


    }


    private void mesajgonderen() {

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm aa");
        String stringdate = simpleDateFormat.format(date);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Mesagges").child(MUser.getUid());

        String msjid = databaseReference.push().getKey();

        //   String timestamp=String.valueOf(System.currentTimeMillis());
        Mesajlar mesajlar = new Mesajlar(mesaj.getText().toString(), MUser.getUid(), userid, "", stringdate, false);


        databaseReference.child(msjid).setValue(mesajlar);

    }


    private void mesajalan() {

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm aa");
        String stringdate = simpleDateFormat.format(date);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Mesagges").child(userid);

        String ms = databaseReference.push().getKey();

        //   String timestamp=String.valueOf(System.currentTimeMillis());

        Mesajlar mesajlar = new Mesajlar(mesaj.getText().toString(), MUser.getUid(), userid, "", stringdate.toString(),  false);

        databaseReference.child(ms).setValue(mesajlar);

    }


    private void istifadecimelumatinal(TextView istifadeciad, String uid) {

        FirebaseDatabase.getInstance().getReference().child("users").child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        //   for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        Istifadeciler istifadeciler = snapshot.getValue(Istifadeciler.class);

                        istifadeciad.setText(istifadeciler.getUsername());

                        Glide.with(MesajActivity.this).load(istifadeciler.getImageurl()).into(profilresm);

                        mesajlarial(FirebaseAuth.getInstance().getCurrentUser().getUid(), userid);
                        //}


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void mesajlarial(final String muserid, final String userid) {

        FirebaseDatabase.getInstance().getReference().child("Mesagges").child(userid).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        mesajlarArrayList.clear();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            if (dataSnapshot != null) {

                                Mesajlar mesajlar = dataSnapshot.getValue(Mesajlar.class);


                                assert mesajlar != null;

                                if (mesajlar.getMesajgonderenid() != null && userid != null && mesajlar.getMesajalanid() != null && muserid != null) {

                                    if (mesajlar.getMesajgonderenid().equals(userid) && mesajlar.getMesajalanid().equals(muserid)
                                            || mesajlar.getMesajalanid().equals(userid) && mesajlar.getMesajgonderenid().equals(muserid)) {

                                        mesajlarArrayList.add(mesajlar);
                                    }
                                }


                            }


                        }

                        mesajlarAdapter.notifyDataSetChanged();// avtomatik sehivenin oz icinde yenilenmeyi ucun yeni mesajlar gelende avtomatik sehive yenilemesi ucucndur bu


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }


    private void goruldu() {

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Mesagges").child(MUser.getUid());
        value = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                    Mesajlar mesajlar = snapshot1.getValue(Mesajlar.class);

                    if (mesajlar.getMesajalanid() != null && MUser.getUid() != null) {

                        if (mesajlar.getMesajalanid().equals(MUser.getUid()) && mesajlar.getMesajgonderenid().equals(userid)) {

                            HashMap<String, Object> map = new HashMap<>();

                            map.put("goruldu", true);

                            snapshot1.getRef().updateChildren(map);

                        }
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    protected void onPause() {
        super.onPause();
        databaseReference.removeEventListener(value);
    }

}