package com.example.eslam.mashaweer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Profile_Owner extends AppCompatActivity implements Cars_Adapter.ItemClickListener {

    TextView DisplayName, UserType;

    Cars_Adapter adapter;

    ArrayList<String> carBrandName = new ArrayList<>();
    ArrayList<String> carModel = new ArrayList<>();
    ArrayList<String> carYear = new ArrayList<>();


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("Users");
    private CollectionReference carsRef = db.collection("Cars");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__owner);

        DisplayName = (TextView) findViewById(R.id.displayname);
        UserType = (TextView) findViewById(R.id.user_Type_O);


        getCars(null);
//
        usersRef.whereEqualTo("userID", LogIn_Activity.UserID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Users user = documentSnapshot.toObject(Users.class);
                            user.setDocumentId(documentSnapshot.getId());

                            String userId = user.getUserID();
                            String userName = user.getUserName();
                            String userType = user.getUserType();
                            String mobile = user.getMobile();

                            DisplayName.setText(userName);
                            UserType.setText(userType);


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
//        DisplayName = (TextView)findViewById(R.id.displayname);
//        UserType = (TextView)findViewById(R.id.user_Type_O);
//
//        DisplayName.setText(LogIn_Activity.User);
//        UserType.setText(LogIn_Activity.User_Type);


    }

    public void addCars(View view) {

        startActivity(new Intent(Profile_Owner.this, Test_Data.class));
    }

    public void getCars(View view) {

        carsRef.whereEqualTo("userID",LogIn_Activity.UserID)
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

                            carBrandName.add(brand);
                            carModel.add(model);
                            carYear.add(year);

                        }


                        // set up the RecyclerView
                        RecyclerView recyclerView = findViewById(R.id.rvCars);
                        recyclerView.setLayoutManager(new LinearLayoutManager(Profile_Owner.this));
                        adapter = new Cars_Adapter(Profile_Owner.this, carBrandName,carModel,carYear);
                        adapter.setClickListener(Profile_Owner.this);
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
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }


}

