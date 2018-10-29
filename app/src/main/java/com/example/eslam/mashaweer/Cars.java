package com.example.eslam.mashaweer;

import java.util.List;



public class Cars {
    private String userID;
    private String brand;
    private String model;
    private String year;
    private String color;
    private String platNo;
    private String gov;
    private String city;

    private List<String> imageList ;

    public Cars (){}

    public Cars(String UserID, String Brand, String Model, String Year, String Color, String PlatNo, String Gov, String City, List<String> ImageList) {
        this.userID = UserID;
        this.brand = Brand;
        this.model = Model;
        this.year = Year;
        this.color = Color;
        this.platNo = PlatNo;
        this.gov = Gov;
        this.city = City;

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



    public List<String> getImageList() {
        return imageList;
    }
}


class Brands {
    private String brands ;

    public Brands (){}

    public Brands (String brands ) {
        this.brands = brands ;

    }

    public String getBrands() {
        return brands;
    }
}


class Models {
    private String brand ;
    private String models;



    public Models (){}

    public Models (String brand, String model ) {
        this.brand = brand;
        this.models = model;



    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.models = model;
    }

    public String getModels() {
        return models;
    }

    public String getBrand() {
        return brand;
    }
}
