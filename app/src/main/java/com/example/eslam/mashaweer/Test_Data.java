package com.example.eslam.mashaweer;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
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

    TextView textView ;
    EditText editText ;
    Button applay , save ;

    public static String SHARED_Pref = "sharedPref";
    public static String TEXT = "text";

    private String text ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test__data);

        textView = (TextView)findViewById(R.id.textview);
        editText = (EditText)findViewById(R.id.edittext);
        applay = (Button)findViewById(R.id.btnapply);
        save = (Button)findViewById(R.id.btnsave);


        applay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textView.setText(editText.getText().toString());

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        loadData();
        updateView();
    }

  public void  saveData(){

      SharedPreferences sharedPreferences = getSharedPreferences(SHARED_Pref,MODE_PRIVATE);
      SharedPreferences.Editor editor = sharedPreferences.edit();

      editor.putString(TEXT,textView.getText().toString());
      editor.apply();

    }

    public void loadData (){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_Pref,MODE_PRIVATE);

        text = sharedPreferences.getString(TEXT,"");


    }

    public void updateView(){

        textView.setText(text);
    }

}


