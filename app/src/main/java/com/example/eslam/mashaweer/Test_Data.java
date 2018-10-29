package com.example.eslam.mashaweer;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class Test_Data extends AppCompatActivity  {

    Spinner spinner_Brands ;
    EditText model ;
    List<String> Brands = new ArrayList<>();
    String selectedbrand , enteredModel ;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference brandsRef = db.collection("Brands");
    private CollectionReference modelsRef = db.collection("Models");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test__data);

        spinner_Brands = (Spinner)findViewById(R.id.spinner3);
        model = (EditText)findViewById(R.id.model);


        Brands.add("All");



        getCars(null);



        ArrayAdapter<String> brands_Adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Brands);
        spinner_Brands.setAdapter(brands_Adapter);

        spinner_Brands.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });






    }

//    public void onItemSelected(AdapterView<?> parent, View view,
//                               int pos, long id) {
//
//      selectedbrand = spinner_Brands.getSelectedItem().toString();
//
//
//    }
//
//    public void onNothingSelected(AdapterView<?> parent) {
//        // Another interface callback
//    }
//
//
    public void getCars(View view) {

        brandsRef
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String cars = "";

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Brands brand = documentSnapshot.toObject(Brands.class);
//                            cars.setDocumentId(documentSnapshot.getId());

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
//
//
    }
//
//
//    public void addModel(View view) {
//
//
//        enteredModel = model.getText().toString();
//
//
//       Models models = new Models(selectedbrand,enteredModel);
//
//        modelsRef.add(models).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });
//
//
//
//
//
//    }
}


