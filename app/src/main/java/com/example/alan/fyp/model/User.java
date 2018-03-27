package com.example.alan.fyp.model;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    @Exclude public String id;
    private String name;
    private String image;
    private String type;
    private double avgRating;
    private String institutionOrGrade;



    
    public User() {
    }


    public String getInstitutionOrGrade() {
        return institutionOrGrade;
    }

    public void setInstitutionOrGrade(String institutionOrGrade) {
        this.institutionOrGrade = institutionOrGrade;
    }



    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
