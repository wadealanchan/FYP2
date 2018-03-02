package com.example.alan.fyp.viewModel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.alan.fyp.Henson;
import com.example.alan.fyp.ListViewModel.InvitationListViewModel;
import com.example.alan.fyp.R;
import com.example.alan.fyp.activity.AuthClass;
import com.example.alan.fyp.activity.PostDetail;
import com.example.alan.fyp.model.InvitationRequest;
import com.example.alan.fyp.model.Request;
import com.example.alan.fyp.model.User;
import com.example.alan.fyp.model.model_conversation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Date;

/**
 * Created by wadealanchan on 30/1/2018.
 */

public class InvitationViewModel {



    private String postId;
    private String postUserId;
    private String postDescription;
    private String postTtile;
    public final ObservableField<User> user = new ObservableField<User>();
    FirebaseUser firebaseuser = FirebaseAuth.getInstance().getCurrentUser();


    @BindingAdapter({"bind:imagesrc"})
    public static void loadImage(ImageView view, String image) {
        if(image!=null)
            Picasso.with(view.getContext()).load(image).into(view);
        else
            Picasso.with(view.getContext()).load(R.drawable.ic_avatar_default).into(view);
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPostTtile() {
        return postTtile;
    }

    public void setPostTtile(String postTtile) {
        this.postTtile = postTtile;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postID) {
        this.postId = postID;
    }

    public String getPostUserId() {
        return postUserId;
    }

    public void setPostUserId(String postUserId) {
        this.postUserId = postUserId;
    }



    public void Requestforchat(View v){

        InvitationRequest invitationRequest = new InvitationRequest();
        invitationRequest.setTime(new Date());

        String requestid = postId+":"+postUserId;

        FirebaseFirestore.getInstance().collection("Users").document(user.get().id)
                .collection("invitationRequest").document(requestid).set(invitationRequest).addOnSuccessListener
                (new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        onInviteClick(v);
                        ((Activity)(v.getContext())).finish();
                    }
                });

    }


    public void onInviteClick(View v)
    {

        Log.d("InvitationListViewModel", this.postId+" "+ this.postUserId +" "+this.user.get().id);
        FirebaseFirestore.getInstance().collection("conversation")
                .whereEqualTo("postId", getPostId())
                .whereEqualTo("aid", user.get().id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {

                        if (!documentSnapshots.isEmpty()) {
                            passdata(v, documentSnapshots.getDocuments().get(0).getId(), 1);

                        } else {
                            model_conversation c = new model_conversation();
                            c.setAid(user.get().id);
                            c.setPostId(getPostId());
                            c.setPostUserId(getPostUserId());
                            c.setStatus("pending");
                            FirebaseFirestore.getInstance().collection("conversation")
                                    .add(c)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d("", "DocumentSnapshot added with ID: " + documentReference.getId());
                                            Toast.makeText(v.getContext(), "Invitation sent", Toast.LENGTH_SHORT).show();



                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("", "Error adding document", e);
                                        }
                                    });
                        }
                    }
                });
    }


    public void passdata(View v, String conId, int number) {

        switch (number) {
            case 0:
                break;
            case 1:
                Intent intent1 = Henson.with(v.getContext()).gotoChat2()
                        .conversationId(conId)
                        .targetUserName(user.get().getName())
                        .build();
                v.getContext().startActivity(intent1);
                break;

        }


    }



}
