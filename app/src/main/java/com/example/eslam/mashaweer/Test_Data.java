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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Test_Data extends AppCompatActivity {


    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";

    private EditText editTextTitle;
    private EditText editTextDescription;
    private TextView textViewData;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference noteRef = db.collection("Cars").document("Brands");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test__data);

        editTextTitle = (EditText) findViewById(R.id.editText_title);
        editTextDescription = (EditText) findViewById(R.id.editText_desc);
        textViewData = (TextView) findViewById(R.id.textView_Data);


    }

    public void out(View view) {
        FirebaseAuth.getInstance().signOut();
    }

    public void saveNote(View view) {
        String title = editTextTitle.getText().toString();
        String desc = editTextDescription.getText().toString();

        Map<String, Object> note = new HashMap<>();
        note.put(KEY_TITLE, title);
        note.put(KEY_DESCRIPTION, desc);


        noteRef.set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Test_Data.this, "Note Saved ", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Test_Data.this, "Not Saved", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void loadNote(View view) {

        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String title = documentSnapshot.getString(KEY_TITLE);
                            String desc = documentSnapshot.getString(KEY_DESCRIPTION);

                            textViewData.setText(title + "\n" + desc);

                        } else {
                            Toast.makeText(Test_Data.this, "document not exist", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(Test_Data.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });


    }
}
