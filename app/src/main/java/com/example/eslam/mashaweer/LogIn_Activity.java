package com.example.eslam.mashaweer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LogIn_Activity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    FirebaseAuth mAuth ;
    FirebaseAuth.AuthStateListener mAuthListner ;
    Button any , logInGoogle;

    public static String UserID ;
   public static String User,Mobile;
   public static String User_Type ="Owner";

   //Get Data Strings
   String UserName ,mobile,UserType  ;


    EditText logInName,logInMobile;


    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient ;

    RadioGroup Radio_G_UserType ;
    RadioButton Owner,Renter;
    ProgressBar progressBar;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef =  db.collection("Users");


    String checkedUserId ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_);


        logInGoogle = (Button)findViewById(R.id.btn_Log_In);



        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        logInName = (EditText)findViewById(R.id.logInName) ;
        logInMobile = (EditText)findViewById(R.id.logInMobile) ;
        Owner = (RadioButton)findViewById(R.id.radio_owner);
        Renter = (RadioButton)findViewById(R.id.radio_renter);

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

                    getUserData();


                }
            }


            //==========================================================================================
public void getUserData(){
//                UserName = "";
//                mobile = "";
//                UserType = "";


    progressBar.setVisibility(View.VISIBLE);
        usersRef.whereEqualTo("userID",UserID)
                    .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Users user = documentSnapshot.toObject(Users.class);

                        UserName = user.getUserName();
                        mobile = user.getMobile();
                        UserType = user.getUserType();



                        if (UserName.equals(null)){
                            logInName.setText(User);
                        }else {
                            logInName.setText(UserName);
                        }

                        if (mobile.equals(null)){
                            logInMobile.setText(Mobile);
                        }else {
                            logInMobile.setText(mobile);
                        }

                        if (UserType.equals("Owner")){
                            Owner.setChecked(true);
                        }else {
                            Renter.setChecked(true);
                        }


                       // Toast.makeText(LogIn_Activity.this,user.getEmail(),Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        //logInGoogle.setVisibility(View.GONE);
                        logInGoogle.setBackgroundResource(R.drawable.btn_rounded_login_hidden);
                        logInGoogle.setClickable(false);








                    }

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });


}


//==========================================================================================







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
                      //  progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public void addUser(View view) {


        // =========================================check user ==================================
        usersRef.whereEqualTo("userID",UserID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Users user = documentSnapshot.toObject(Users.class);

                            checkedUserId = user.getUserID();
                        }
                        if (!UserID.equals(checkedUserId)) {

                            String userID = UserID;  // get From Google Account
                            String UserName = logInName.getText().toString();
                            String Mobile = logInMobile.getText().toString();
                            String UserType = User_Type;

                            Users users = new Users(userID, UserName, Mobile, UserType);

                            usersRef.add(users).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(LogIn_Activity.this, "Done", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), Profile_Owner.class));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(LogIn_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                        else {
                            Toast.makeText(LogIn_Activity.this, "Done", Toast.LENGTH_SHORT).show();

                            if (UserType.equals("Renter")) {
                                startActivity(new Intent(getApplicationContext(), Car_Request.class));
                            }else if (UserType.equals("Owner")){
                                startActivity(new Intent(getApplicationContext(), Profile_Owner.class));
                            }
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });



        //=========================================================================================


    }
}