package com.example.eslam.mashaweer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Profile_Owner extends AppCompatActivity {

    TextView DisplayName,UserType,Cars ;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef =  db.collection("Users");
    private CollectionReference carsRef =  db.collection("Cars");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__owner);

        Cars = (TextView)findViewById(R.id.cars);

        DisplayName = (TextView)findViewById(R.id.displayname);
        UserType = (TextView)findViewById(R.id.user_Type_O);

        usersRef.whereEqualTo("userID",LogIn_Activity.UserID)
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

        startActivity(new Intent(Profile_Owner.this,Test_Data.class));
    }

    public void getCars(View view) {

        carsRef//.whereEqualTo("userID",LogIn_Activity.UserID)
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
                            String plate = car.getPlatNo();
                            String gov = car.getGov();
                            String city = car.getCity();

                            cars += userId + "\n" +brand + "\n"+ model + "\n" + color + "\n "+ plate + "\n"+ gov + "\n" + city +"\n\n";


                            Cars.setText(cars);

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
