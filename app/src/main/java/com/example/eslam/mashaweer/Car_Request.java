package com.example.eslam.mashaweer;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Car_Request extends AppCompatActivity implements Car_Request_Adapter.ItemClickListener {


    Car_Request_Adapter adapter;

    ArrayList<String> carBrandName = new ArrayList<>();
    ArrayList<String> carModel = new ArrayList<>();
    ArrayList<String> carYear = new ArrayList<>();
    ArrayList<String> plateNo = new ArrayList<>();
    ArrayList<String> uploadImages = new ArrayList<>();

    String imagesUrl = "" ;


    public static String SelectedCarPlateNo;

    private Context mContext;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference carsRef = db.collection("Cars");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car__request);


        getCars(null);


    }


    public void getCars(View view) {

        carsRef
//                .whereEqualTo("userID",LogIn_Activity.UserID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String cars = "";

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Cars car = documentSnapshot.toObject(Cars.class);
//                            cars.setDocumentId(documentSnapshot.getId());

                            String userId = car.getUserID();
                            String brand = car.getBrand();
                            String model = car.getModel();
                            String color = car.getColor();
                            String year = car.getYear();
                            String plate = car.getPlatNo();
                            String gov = car.getGov();
                            String city = car.getCity();
                            List<String> imagesList = car.getImageList();

                            if (imagesList.size() == 0 ){
                                imagesUrl = "null" ;
                                uploadImages.add(imagesUrl);
                            }else {

                            for (int i = 0; i < imagesList.size(); i++) {




                                    imagesUrl = imagesList.get(i);
                                    uploadImages.add(imagesUrl);
                                    if (i == 0) {
                                        break;
                                    }

                                }
                            }


                            //  ImageView carImage  ;

                            carBrandName.add(brand);
                            carModel.add(model);
                            carYear.add(year);
                            plateNo.add(plate);
//                            uploadImages.add(imagUrl);

//                            uploadImages.add(Picasso.with(mContext).load(imagUrl).fit().centerCrop().into(carImage));


                        }


                        // set up the RecyclerView
                        RecyclerView recyclerView = findViewById(R.id.rvCars_Requests);
                        recyclerView.setLayoutManager(new LinearLayoutManager(Car_Request.this));
                        adapter = new Car_Request_Adapter(Car_Request.this, carBrandName, carModel, carYear, plateNo, uploadImages);
                        adapter.setClickListener(Car_Request.this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number "
                + position, Toast.LENGTH_SHORT).show();

        SelectedCarPlateNo = adapter.getItem(position);
        startActivity(new Intent(Car_Request.this, OrderCar_Activity.class));


    }


}
