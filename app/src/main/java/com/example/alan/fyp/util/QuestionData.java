package com.example.alan.fyp.util;

import android.databinding.ObservableList;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.alan.fyp.model.Post;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import static com.google.firebase.firestore.FirebaseFirestore.*;

/**
 * Created by wadealanchan on 2/3/2018.
 */

public class QuestionData {
    public static ObservableList<Post> posts;


    public void showcontent(String category) {

       // CollectionReference query = getInstance().collection("posts");

        if (category.equals("1")) {
            Log.d("QuestionData"," come here? 1" );
            FirebaseFirestore.getInstance().collection("posts").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {


                @Override
                public void onEvent(@Nullable QuerySnapshot value,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        //Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    Log.d("QuestionData"," come here? 2" );
                    posts.clear();
                    for (DocumentSnapshot document : value) {
                        if (document.exists()) {
                            Post post = document.toObject(Post.class);
                            posts.add(post);
                            Log.d("QuestionData"," come here?"+posts );

                        }
                    }
                }

            });

        } else {
            FirebaseFirestore.getInstance().collection("posts").whereEqualTo("category",category).orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        //Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    posts.clear();
                    for (DocumentSnapshot document : value) {
                        if (document.exists()) {
                            Post post = document.toObject(Post.class);
                            posts.add(post);


                        }
                    }
                }

            });

        }



    }



}
