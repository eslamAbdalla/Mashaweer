package com.example.eslam.mashaweer;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;

public class Add_Car extends AppCompatActivity {

    private EditText editTextBrand;
    private EditText editTextModel;
    private EditText editTextYear;
    private EditText editTextColor;
    private EditText editTextPlate;
    private EditText editTextGov;
    private EditText editTextCity;
    private String imageUrl;

    private String platNo;

    private ImageView imageView;

    private Button btnAddMoreImgs;

    private ProgressBar uploadImageProgressBar;

    private TextView textViewData;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int RESULT_LOAD_IMAGE = 1;
    private Uri imageUri;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Button buttonChooseImage;

    private CollectionReference carsRef = db.collection("Cars");

    private StorageReference carsImageStorageRef;

    private Button saveCareButton;


    StorageTask storageTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__car);

        saveCareButton = (Button) findViewById(R.id.btnSaveCar);


        carsImageStorageRef = FirebaseStorage.getInstance().getReference("CarsImages");

        uploadImageProgressBar = findViewById(R.id.progress_bar_upload_imag);

        btnAddMoreImgs = findViewById(R.id.btn_moreImages);

        imageView = findViewById(R.id.image_view_upload_image);

        editTextBrand = (EditText) findViewById(R.id.editText_brand);
        editTextModel = (EditText) findViewById(R.id.editText_model);
        editTextYear = (EditText) findViewById(R.id.editText_year);
        editTextColor = (EditText) findViewById(R.id.editText_color);
        editTextPlate = (EditText) findViewById(R.id.editText_plate);
        editTextGov = (EditText) findViewById(R.id.editText_gov);
        editTextCity = (EditText) findViewById(R.id.editText_city);


        buttonChooseImage = findViewById(R.id.btn_choose_image);

        btnAddMoreImgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMultiFileChooser();
            }
        });

        buttonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFileChooser();

            }
        });


//===========Check for uploading file ==================================================
//
//                if (storageTask != null && storageTask.isInProgress()){
//
//                    Toast.makeText(Add_Car.this,"Inprogress",Toast.LENGTH_LONG).show();
//                }else {
//                    uploadFile();
//
//                }
//
//            }
//        });
//========================================================================================


    }

    //============Open phone Storage To Get Multible Files (Image )=====================================
    private void openMultiFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Images"), RESULT_LOAD_IMAGE);

    }


    //============Open phone Storage To Get File (Image )=====================================
    private void openFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
//========================================================================================

    //============Get File URI on Phone ======================================================
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();


        }
    }

    //========================================================================================
//TODO ================================Log Out Function===================================
    public void out(View view) {
        FirebaseAuth.getInstance().signOut();
    }
//========================================================================================

    //============== Get File Extention To Name It On Firebase Storage =======================
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    //========================================================================================
//==================== Upload Image Method================================================
    private void uploadFile() {
        if (imageUri != null) {
//============== Get Entered Plate No To Name Eache Image With Its Plate NO ==============
            platNo = editTextPlate.getText().toString();

            final StorageReference fileReference = carsImageStorageRef.child(platNo + "." + getFileExtension(imageUri));
//===================Uploading Image Progress Action ======================================
            fileReference.putFile(imageUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    uploadImageProgressBar.setProgress((int) progress);

                }
            }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
//========== Display Uploaded Image ========================================================
                        Picasso.with(Add_Car.this).load(imageUri).into(imageView);
//========== Reset Progress Bar After Finish ===============================================
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                uploadImageProgressBar.setProgress(0);
                            }
                        }, 5000);

                        Toast.makeText(Add_Car.this, "Upload Successful", Toast.LENGTH_LONG).show();
//========== Get Uploaded Image URL To Add In Cars DataBase ================================
                        Uri downloadUri = task.getResult();
                        imageUrl = downloadUri.toString();
//========== Call AddCar Method After Geting URL ===========================================
                        addCar(null);

                    } else {
                        Toast.makeText(Add_Car.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

//========================================================================================

        } else {
            Toast.makeText(this, "No File selected ", Toast.LENGTH_LONG).show();
        }

    }

    //=========== Save Button Click Actions ===================================================
    public void saveCar(View view) {
        saveCareButton.setClickable(false);
        uploadFile();
    }

    //=========================================================================================
//============ Adding Car To DataBase =====================================================
    public void addCar(View view) {

        String userId = LogIn_Activity.UserID;
        String brand = editTextBrand.getText().toString();
        String model = editTextModel.getText().toString();
        String year = editTextYear.getText().toString();
        String color = editTextColor.getText().toString();
        String gov = editTextGov.getText().toString();
        String city = editTextCity.getText().toString();

        Cars cars = new Cars(userId, brand, model, year, color, platNo, gov, city, imageUrl);

        carsRef.add(cars).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(Add_Car.this, "Car Added", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Add_Car.this, Profile_Owner.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Add_Car.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
