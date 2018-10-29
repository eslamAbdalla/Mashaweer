package com.example.eslam.mashaweer;




//Todo ================= Check Plate no If Empty ===================
//Todo =================  ===================

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Add_Car extends AppCompatActivity  {

//=============== User Input ====================================
//    private EditText editTextBrand;
//    private EditText editTextModel;
    private EditText editTextYear;
    private EditText editTextColor;
    private EditText editTextPlate;
    private EditText editTextGov;
    private EditText editTextCity;
    private String platNo;

//========= URL ==============================
    private String imageUrl;
    private String URL ;
    private List<String> imageUrlList = new ArrayList<>() ;


    private String imageName ;
    private String plate ;

    private ImageView imageView;


    ProgressBar addCarProgressBar;

    private ProgressBar uploadImageProgressBar;



    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int RESULT_LOAD_IMAGE = 1;
    private Uri imageUri;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Button buttonChooseImage;

    private CollectionReference carsRef = db.collection("Cars");
    private CollectionReference brandsRef = db.collection("Brands");
    private CollectionReference modelsRef = db.collection("Models");

    private StorageReference carsImageStorageRef;

    private Button saveCareButton;

    private int imageAdded ;

    private Spinner spinner_Brands,spinner_Models ;
    private List<String> Brands = new ArrayList<>();
    private List<String> Models  = new ArrayList<>();

    private String selected_Brand ,selected_Model ;

    ArrayAdapter<String> models_Adapter ;

    StorageTask storageTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__car);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        saveCareButton = (Button) findViewById(R.id.btnSaveCar);
        spinner_Brands = (Spinner)findViewById(R.id.sp_brands);
        spinner_Models = (Spinner)findViewById(R.id.sp_model);
        addCarProgressBar = (ProgressBar)findViewById(R.id.progressBar_add_car);


        Brands.add("All");
//        Models.add("All");

//        getModels();
        getBrands(null);




        uploadImageProgressBar = findViewById(R.id.progress_bar_upload_imag);



        imageView = findViewById(R.id.image_view_upload_image);

//        editTextBrand = (EditText) findViewById(R.id.editText_brand);
//        editTextModel = (EditText) findViewById(R.id.editText_model);
        editTextYear = (EditText) findViewById(R.id.editText_year);
        editTextColor = (EditText) findViewById(R.id.editText_color);
        editTextPlate = (EditText) findViewById(R.id.editText_plate);
        editTextGov = (EditText) findViewById(R.id.editText_gov);
        editTextCity = (EditText) findViewById(R.id.editText_city);


        buttonChooseImage = findViewById(R.id.btn_choose_image);


        buttonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buttonChooseImage.setClickable(false);


                openMultiFileChooser();

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

        ArrayAdapter<String> brands_Adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Brands);
        spinner_Brands.setAdapter(brands_Adapter);

         models_Adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Models);
        spinner_Models.setAdapter(models_Adapter);





        spinner_Brands.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               selected_Brand = parent.getSelectedItem().toString();

               if (selected_Brand.equals("All")){
                   getModels();
               }else {
                   getModelsForSelectedBrand();
               }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_Models.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selected_Model = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });






    }


    // ================================= Action for Back Button in ActionBar==============================
    public boolean onOptionsItemSelected (MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), Profile_Owner.class);
        startActivityForResult(myIntent, 0);

        return true;
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
//    private void openFileChooser() {
//
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, PICK_IMAGE_REQUEST);
//    }
//========================================================================================

    //============Get File URI on Phone ======================================================
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        addCarProgressBar.setVisibility(View.VISIBLE);
//=========================Check Car =====================================================
        platNo = editTextPlate.getText().toString().replace(" ","");

        carsRef.whereEqualTo("platNo",platNo)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String cars = "";

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Cars car = documentSnapshot.toObject(Cars.class);
//                            cars.setDocumentId(documentSnapshot.getId());

                            plate = car.getPlatNo();



                                                                              }
                        if (platNo.equals(plate) ){
                            Toast.makeText(Add_Car.this,"Car Already Exist",Toast.LENGTH_LONG).show();

                        }else {
                            uploadFile();

                        }


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });



//========================================================================================

            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                    && data != null && data.getData() != null) {
                imageUri = data.getData();

            }

            imageAdded = imageUrlList.size();
//            platNo = editTextPlate.getText().toString().replace(" ", "");


//================= Image Name =====================================

            if (imageAdded == 0) {
                imageName = platNo + "." + getFileExtension(imageUri);
            } else {
                imageName = platNo + imageAdded + "." + getFileExtension(imageUri);
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

            carsImageStorageRef = FirebaseStorage.getInstance().getReference(platNo+"_"+"Images");

                final StorageReference fileReference = carsImageStorageRef.child(imageName);
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
                            addCarProgressBar.setVisibility(View.GONE);
//========== Display Uploaded Image ========================================================
                            Picasso.get().load(imageUri).into(imageView);
//========== Reset Progress Bar After Finish ===============================================
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    uploadImageProgressBar.setProgress(0);
                                }
                            }, 5000);

                            Toast.makeText(Add_Car.this, "Upload Successful", Toast.LENGTH_LONG).show();
                            buttonChooseImage.setText("Add More Images");
                            buttonChooseImage.setClickable(true);
//========== Get Uploaded Image URL To Add In Cars DataBase ================================
                            Uri downloadUri = task.getResult();
                            URL = downloadUri.toString();

                            imageUrlList.add(URL);



//========== Call AddCar Method After Geting URL ===========================================
//                            addCar(null);
//
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

//        uploadFile();
        addCar(null);
    }

    //=========================================================================================
//============ Adding Car To DataBase =====================================================
    public void addCar(View view) {


        platNo = editTextPlate.getText().toString().replace(" ","");

        carsRef.whereEqualTo("platNo",platNo)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        String cars = "";

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Cars car = documentSnapshot.toObject(Cars.class);
//                            cars.setDocumentId(documentSnapshot.getId());

                            plate = car.getPlatNo();



                        }
                        if (platNo.equals(plate) || platNo == plate ){
                            Toast.makeText(Add_Car.this,"Car Already Exist",Toast.LENGTH_LONG).show();

                        }else {


                            String userId = LogIn_Activity.UserID;
//                            String brand = editTextBrand.getText().toString();
//                            String model = editTextModel.getText().toString();

                            String brand = selected_Brand;
                            String model = selected_Model;

                            String year = editTextYear.getText().toString();
                            String color = editTextColor.getText().toString();
                            String gov = editTextGov.getText().toString();
                            String city = editTextCity.getText().toString();

                            Cars cars = new Cars(userId, brand, model, year, color, platNo, gov, city, imageUrlList);

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
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });











    }


    public void getBrands(View view) {



        brandsRef
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Brands brand = documentSnapshot.toObject(Brands.class);

                            String Brand = brand.getBrands();
                            Brands.add(Brand);

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    public void getModels() {
        Models = new ArrayList<>();
        Models.add("All");

        modelsRef
//                .whereEqualTo("brand",selected_Brand)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String cars = "";

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Models models = documentSnapshot.toObject(Models.class);
//                            cars.setDocumentId(documentSnapshot.getId());

                            String model = models.getModels();
                            String brand = models.getBrand();
                            Models.add(model);
                                                 }
                        models_Adapter = new ArrayAdapter<>(Add_Car.this, android.R.layout.simple_spinner_dropdown_item, Models);
                        spinner_Models.setAdapter(models_Adapter);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }


    public void getModelsForSelectedBrand() {

        Models = new ArrayList<>();
        Models.add("All");

        modelsRef
                .whereEqualTo("brand",selected_Brand)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String cars = "";

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Models models = documentSnapshot.toObject(Models.class);
//                            cars.setDocumentId(documentSnapshot.getId());

                            String model = models.getModels();
                            String brand = models.getBrand();
                            Models.add(model);
                        }

                        models_Adapter = new ArrayAdapter<>(Add_Car.this, android.R.layout.simple_spinner_dropdown_item, Models);
                        spinner_Models.setAdapter(models_Adapter);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }



}
