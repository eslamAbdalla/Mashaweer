package com.example.eslam.mashaweer;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


public class Test_Data extends AppCompatActivity {


    private static final String KEY_User = "UserID";
    private static final String KEY_Brand = "Brand";
    private static final String KEY_Model = "Model";
    private static final String KEY_Year = "Year";
    private static final String KEY_Color = "Color";
    private static final String KEY_Plat = "Plate";
    private static final String KEY_Gov = "Cov";
    private static final String KEY_City = "City";

    private EditText editTextBrand;
    private EditText editTextModel;
    private EditText editTextYear;
    private EditText editTextColor;
    private EditText editTextPlate;
    private EditText editTextGov;
    private EditText editTextCity;

    private TextView textViewData;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private DocumentReference carRef =  db.collection("Cars").document("aa");
    private CollectionReference carsRef =  db.collection("Cars");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test__data);

        editTextBrand = (EditText) findViewById(R.id.editText_brand);
        editTextModel = (EditText) findViewById(R.id.editText_model);
        editTextYear = (EditText) findViewById(R.id.editText_year);
        editTextColor = (EditText) findViewById(R.id.editText_color);
        editTextPlate = (EditText) findViewById(R.id.editText_plate);
        editTextGov = (EditText) findViewById(R.id.editText_gov);
        editTextCity = (EditText) findViewById(R.id.editText_city);


        textViewData = (TextView) findViewById(R.id.textView_Data);


    }

    public void out(View view) {
        FirebaseAuth.getInstance().signOut();
    }

    public void addCar(View view) {
        String userId = LogIn_Activity.UserID;
        String brand = editTextBrand.getText().toString();
        String model = editTextModel.getText().toString();
        String year = editTextYear.getText().toString();
        String color = editTextColor.getText().toString();
        String platNo = editTextPlate.getText().toString();
        String gov = editTextGov.getText().toString();
        String city = editTextCity.getText().toString();


        Cars cars = new Cars(userId,brand,model,year,color,platNo,gov,city);


        carsRef.add(cars).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(Test_Data.this, "Done", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Test_Data.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void loadNote(View view) {

  //      carsRef.document().getId();
        CollectionReference aaaa = carsRef.document("KS08HIkQwKLxDCyoiqip").getParent();




    }
}
