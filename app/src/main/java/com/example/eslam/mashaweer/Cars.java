package com.example.eslam.mashaweer;

import java.util.List;

import static com.example.eslam.mashaweer.LogIn_Activity.UserID;

public class Cars {
    private String userID;
    private String brand;
    private String model;
    private String year;
    private String color;
    private String platNo;
    private String gov;
    private String city;
    private String imageUrl ;
    private List<String> imageList ;

    public Cars (){}

    public Cars(String UserID, String Brand, String Model, String Year, String Color, String PlatNo, String Gov, String City,String ImageUrl , List<String> ImageList) {
        this.userID = UserID;
        this.brand = Brand;
        this.model = Model;
        this.year = Year;
        this.color = Color;
        this.platNo = PlatNo;
        this.gov = Gov;
        this.city = City;
        imageUrl = ImageUrl ;
        imageList = ImageList ;

    }

    public String getUserID() {
        return userID;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getYear() {
        return year;
    }

    public String getColor() {
        return color;
    }

    public String getPlatNo() {
        return platNo;
    }

    public String getGov() {
        return gov;
    }

    public String getCity() {
        return city;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<String> getImageList() {
        return imageList;
    }
}
