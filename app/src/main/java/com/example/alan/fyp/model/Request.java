package com.example.alan.fyp.model;

import com.google.firebase.firestore.Exclude;

import java.util.Date;

/**
 * Created by wadealanchan on 9/2/2018.
 */

public class Request {

    @Exclude
    public String requestId;

    private Date time;

    public Date getTime() {
        return time;
    }

    public void setTime(Date timestamp) {
        this.time = timestamp;
    }

}
