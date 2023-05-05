package com.nailmehdiyev.myinstagramapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nailmehdiyev.myinstagramapplication.Model.Istifadeciler;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {

    private ImageView close;
    private CircleImageView profil;
    private TextView Name, username, bio, save, changprofil;
    private FirebaseUser user;
    private Uri uriresm;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        close = findViewById(R.id.editprofile_close);
        profil = findViewById(R.id.editprofile_profilresm);
        Name = findViewById(R.id.editprofil_name);
        username = findViewById(R.id.editprofile_username);
        changprofil = findViewById(R.id.editprofile_changephoto);
        bio = findViewById(R.id.editprofil_bio);
        save = findViewById(R.id.editprofile_save);

        user = FirebaseAuth.getInstance().getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference().child("Uploads");


        FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Istifadeciler istifadeciler = snapshot.getValue(Istifadeciler.class);

                Name.setText(istifadeciler.getName());
                username.setText(istifadeciler.getUsername());
                bio.setText(istifadeciler.getBio());

            try {
                if (istifadeciler.getImageurl().equals("default")) {

                    profil.setImageResource(R.mipmap.ic_launcher);

                } else
                    //  Picasso.get().load(istifadeciler.getImageurl()).into(profil);
                    Glide.with(getApplicationContext()).load(R.mipmap.ic_launcher).load(istifadeciler.getImageurl()).into(profil);

            }catch (Exception e){

                e.printStackTrace();
            }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImagePicker.with(EditProfile.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();


            }
        });


        changprofil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImagePicker.with(EditProfile.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();


            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               progressDialog = new ProgressDialog(EditProfile.this);
                progressDialog.setMessage("Updaiting...");
                progressDialog.show();


                HashMap<String, Object> map = new HashMap<>();

                map.put("name", Name.getText().toString());
                map.put("username", username.getText().toString());
                map.put("bio", bio.getText().toString());


                FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).updateChildren(map);

                progressDialog.dismiss();

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == ImagePicker.REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            uriresm = data.getData();

            resmideyismek();


        } else

            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void resmideyismek() {

        ProgressDialog dialog = new ProgressDialog(this);

        dialog.setMessage("Uploading....");

        dialog.show();

        if (uriresm != null) {

            final StorageReference firebaseStorage = storageReference.child(System.currentTimeMillis() + "." + MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uriresm)));

            firebaseStorage.putFile(uriresm).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    firebaseStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String stringresm = uri.toString();

                            HashMap<String,Object>map=new HashMap<>();
                            map.put("imageurl",stringresm);

                            FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(map);

                            dialog.dismiss();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });


                }
            });


        } else
            Toast.makeText(EditProfile.this, "No image selected", Toast.LENGTH_SHORT).show();


    }
}