package com.example.eslam.mashaweer;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class OrderCar_Activity extends AppCompatActivity {

    TextView PlateNo ;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference carsRef = db.collection("Cars");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_car_);

        PlateNo = (TextView)findViewById(R.id.orderCarPlateNo);

        String plateNo = Car_Request.SelectedCarPlateNo;
        PlateNo.setText(plateNo);




        carsRef
                .whereEqualTo("platNo",plateNo)
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
                            String imagUrl = car.getImageUrl();

                        }


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });







    }
}
