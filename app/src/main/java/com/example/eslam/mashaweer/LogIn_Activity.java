package com.example.eslam.mashaweer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class LogIn_Activity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    FirebaseAuth mAuth ;
    FirebaseAuth.AuthStateListener mAuthListner ;
    Button any , google;

   public static String User,Mobile;
   public static String User_Type ="Owner";


    EditText logInName,logInMobile;
    public static String UserID ;

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient ;

    RadioGroup Radio_G_UserType ;
    ProgressBar progressBar;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef =  db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_);



        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        logInName = (EditText)findViewById(R.id.logInName) ;
        logInMobile = (EditText)findViewById(R.id.logInMobile) ;

        Radio_G_UserType = (RadioGroup) findViewById(R.id.radio_User_Type);

        Radio_G_UserType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radio_owner:
                        User_Type = "Owner";
                        break;
                    case R.id.radio_renter:
                        User_Type = "Renter";
                        break;

                }
            }
        });






        GoogleSignInOptions gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        mAuth=FirebaseAuth.getInstance();
        mAuthListner=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null){

                    User = user.getDisplayName();
                    Mobile = user.getPhoneNumber();
                    UserID = user.getUid();
                    logInName.setText(User);
                    logInMobile.setText(Mobile);

                    Toast.makeText(LogIn_Activity.this,user.getEmail(),Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(getApplicationContext(),Profile_Owner.class));

                }
            }

            public void deletUser(){

            }
        };




    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListner);
        finish();
    }

    public void googleclick(View view) {

        progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent,RC_SIGN_IN);




    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode==RC_SIGN_IN){
            GoogleSignInResult result =Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                firbaseAuthWithGoogle(account);
            }
        }
    }

    private void firbaseAuthWithGoogle(GoogleSignInAccount acct){

        progressBar.setVisibility(View.VISIBLE);


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            Toast.makeText(LogIn_Activity.this,"Failed",Toast.LENGTH_LONG).show();

                        }
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public void addUser(View view) {

         String userID = UserID;
         String UserName = logInName.getText().toString();
         String Mobile = logInMobile.getText().toString();
         String UserType=User_Type;


         Users users = new Users(userID,UserName,Mobile,UserType);

        usersRef.add(users).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(LogIn_Activity.this, "Done", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LogIn_Activity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });


    }
}