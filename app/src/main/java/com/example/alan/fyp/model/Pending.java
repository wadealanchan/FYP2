package com.example.alan.fyp.model;

/**
 * Created by wadealanchan on 13/2/2018.
 */

public class Pending {

    private String postUserId;
    private String aid;
    private String postId;


    public String getAid(){
        return aid;
    }

    public void setAid(String aid){
        this.aid=aid;
    }

    public String getPostId(){
        return postId;
    }

    public void setPostId(String postId){
        this.postId=postId;
    }


    public String getPostUserId() {
        return postUserId;
    }

    public void setPostUserId(String postUserId) {
        this.postUserId = postUserId;
    }
}
