package com.example.alan.fyp.model;

import com.google.firebase.firestore.Exclude;

/**
 * Created by wadealanchan on 29/1/2018.
 */

public class Rating {


    @Exclude public String postId;
    private int numberOfStar;
    private String comment;




    public int getNumberOfStar() {
        return numberOfStar;
    }

    public void setNumberOfStar(int numberOfStar) {
        this.numberOfStar = numberOfStar;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


}
