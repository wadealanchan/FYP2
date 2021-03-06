package com.example.alan.fyp.model;

import android.databinding.BaseObservable;
import android.util.Log;

import com.example.alan.fyp.viewModel.PostViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Post extends BaseObservable implements Serializable {

    public static final String TAG = "Post";

    @Exclude
    public String postId;

    private String image, title, description, userId, category ;

    private boolean postAnswered;

    private java.util.Date date;



    public Post() {

    }

    public boolean isPostAnswered() {
        return postAnswered;
    }

    public void setPostAnswered(boolean postAnswered) {
        this.postAnswered = postAnswered;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String id) {
        this.userId = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String Category) {
        this.category = Category;
    }


    public PostViewModel toViewModel() {


                PostViewModel p = new PostViewModel();
                p.PostId = this.postId;
                p.image.set(this.image);
                p.title.set(this.title);
                p.description.set(this.description);
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd HH:mm");
                String dateString = sdf.format(this.date);
                p.timestamp.set(dateString);
                return p;



    }



}
