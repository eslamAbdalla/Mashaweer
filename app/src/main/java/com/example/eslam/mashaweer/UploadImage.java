package com.example.eslam.mashaweer;

public class UploadImage {

    private String imageName ;
    private String imageUrl ;
    public UploadImage (){

    }
    public UploadImage (String name , String imageUrl){
        if (name.trim().equals("")){
            name = "No Name";
        }

        imageName = name ;
        this.imageUrl = imageUrl ;

    }
    public String getImageName(){
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    public String getImageUrl(){
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
