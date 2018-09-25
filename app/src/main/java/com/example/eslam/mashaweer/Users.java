package com.example.eslam.mashaweer;

import static com.example.eslam.mashaweer.LogIn_Activity.Mobile;

public class Users {
    private String documentId;
    private String userID;
    private String userName;
    private String mobile;
    private String userType;

    public Users() {}



    public Users(String UserID, String UserName, String Mobile, String UserType) {
        this.userID = UserID;
        this.userName = UserName;
        this.mobile = Mobile;
        this.userType = UserType;


    }


    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }





    public String getUserID() {
        return userID;
    }

//    public void setUserID(String userID) {
//        userID = userID;
//    }

    public String getUserName() {
        return userName;
    }

    public String getMobile() {
        return mobile;
    }

    public String getUserType() {
        return userType;
    }
}
