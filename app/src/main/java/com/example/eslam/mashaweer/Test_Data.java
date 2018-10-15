package com.example.eslam.mashaweer;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class Test_Data extends AppCompatActivity {

//
//    private static final String KEY_User = "UserID";
//    private static final String KEY_Brand = "Brand";
//    private static final String KEY_Model = "Model";
//    private static final String KEY_Year = "Year";
//    private static final String KEY_Color = "Color";
//    private static final String KEY_Plat = "Plate";
//    private static final String KEY_Gov = "Cov";
//    private static final String KEY_City = "City";


    private static final int PICK_IMAGE_REQUEST = 1;

    private Button buttonChooseImage, buttonUploadImage;
    private TextView showUploads;
    private EditText fileName;
    private ImageView imageView;
    private ProgressBar uploadImageProgressBar;

    private Uri imageUri;

    private StorageReference mStorageRef ;
    private DatabaseReference mDatabaseRef ;

    StorageTask storageTask ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test__data);




        buttonChooseImage = findViewById(R.id.btn_choose_image);

        imageView = findViewById(R.id.image_view_upload_image);
        uploadImageProgressBar = findViewById(R.id.progress_bar_upload_imag);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");


        buttonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFileChooser();

            }
        });

        buttonUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (storageTask != null && storageTask.isInProgress()){

                    Toast.makeText(Test_Data.this,"Inprogress",Toast.LENGTH_LONG).show();
                }else {
                    uploadFile();
                }
            }
        });

        showUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }


    private void openFileChooser(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null ){
            imageUri = data.getData();


            Picasso.with(this).load(imageUri).into(imageView);


//            imageView.setImageURI(imageUri);

        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(){
        if (imageUri != null){

                final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()+ "."+getFileExtension(imageUri));

            storageTask =  fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {



                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    uploadImageProgressBar.setProgress(0);
                                }
                            },5000);
                            Toast.makeText(Test_Data.this,"Upload Successful",Toast.LENGTH_LONG).show();
                            UploadImage uploadImage = new UploadImage(fileName.getText().toString().trim(),
                                   fileReference.getDownloadUrl().toString()


                            );
                            String a = fileReference.getBucket();
                            String b = fileReference.getName();
                            String c = fileReference.getPath();


                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(uploadImage);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Test_Data.this,e.getMessage(),Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            uploadImageProgressBar.setProgress((int)progress);
                        }
                    });



            //=========================================================================================================
//        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                @Override
//                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                    if (!task.isSuccessful()) {
//                        throw task.getException();
//                    }
//
//                    // Continue with the task to get the download URL
//                    return fileReference.getDownloadUrl();
//                }
//            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                @Override
//                public void onComplete(@NonNull Task<Uri> task) {
//                    if (task.isSuccessful()) {
//                        Uri downloadUri = task.getResult();
//                    } else {
//                        // Handle failures
//                        // ...
//                    }
//                }
//            });
//=========================================================================================================

        }else {
            Toast.makeText(this,"No File selected ",Toast.LENGTH_LONG).show();
        }

    }


    public void out(View view) {
    }
}
