package com.example.alan.fyp.model;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class User {
    @Exclude public String id;
    private String name;
    private String image;
//    private String Type;
//    private List<Rating> rating;

    public User() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

//    public String getType() {
//        return Type;
//    }
//
//    public void setType(String type) {
//        Type = type;
//    }
//
//    public List<Rating> getRating() {
//        return rating;
//    }
//
//    public void setRating(List<Rating> rating) {
//        this.rating = rating;
//    }
}
