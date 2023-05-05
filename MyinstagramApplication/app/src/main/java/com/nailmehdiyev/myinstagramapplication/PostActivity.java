package com.nailmehdiyev.myinstagramapplication;

import static java.security.AccessController.getContext;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.nailmehdiyev.myinstagramapplication.Model.Gonderi;
import com.nailmehdiyev.myinstagramapplication.databinding.ActivityMainBinding;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executor;

public class PostActivity extends AppCompatActivity {

    private ImageView closed, imageView;
    private TextView posttextview;
    private EditText descriptiontext;
    private StorageReference storageReference;

    private ActivityMainBinding activityMainBinding;

    private ActivityResultLauncher<String> resultLauncher;
    private Uri result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_post);

        closed = findViewById(R.id.idclos);
        imageView = findViewById(R.id.resmlanch);
        posttextview = findViewById(R.id.posttext);
        descriptiontext = findViewById(R.id.descriptiontext);


        storageReference = FirebaseStorage.getInstance().getReference("Path");


        posttextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (result != null) {

                    uploiding(result);
                }


            }
        });


        closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PostActivity.this, MainActivity2.class));
                finish();
            }
        });


        ImagePicker.with(PostActivity.this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start();





      /*  ImagePicker.with(this)
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                startForProfileImageResult.launch(intent)

       */


    }


    private void uploiding(Uri resmuri) {

        ProgressDialog dialog = new ProgressDialog(PostActivity.this);
        dialog.setMessage("UpLoading....");
        dialog.show();


        if (resmuri != null) {

            final StorageReference storage = storageReference.child(System.currentTimeMillis() + "." + dosyayolu(resmuri));

            storage.putFile(resmuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String myUri = uri.toString();


                            FirebaseUser muser = FirebaseAuth.getInstance().getCurrentUser();

                            DatabaseReference dataref = FirebaseDatabase.getInstance().getReference("Posts");
                            String postid = dataref.push().getKey();

                            assert muser != null;
                            Gonderi gonderi = new Gonderi(postid, myUri, descriptiontext.getText().toString(), muser.getUid());


                            assert postid != null;
                            dataref.child(postid).setValue(gonderi);


                            dialog.dismiss();
                            startActivity(new Intent(PostActivity.this, MainActivity2.class));
                            finish();


                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(PostActivity.this, "Add is not Succesful", Toast.LENGTH_SHORT).show();

                }
            });


        }


    }


    private String dosyayolu(Uri uri) {

        MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));

        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ImagePicker.REQUEST_CODE && resultCode == RESULT_OK && data != null)

            result = data.getData();

        imageView.setImageURI(result);


        super.onActivityResult(requestCode, resultCode, data);
    }
}