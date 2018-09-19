package com.example.eslam.mashaweer;



import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseInsIDService";

    @Override
    public void onTokenRefresh() {
        //Get Update Token
        String refeshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG,"New Token" + refeshedToken);

        //you can save the token in the third party server to do anything you want


    }
}
