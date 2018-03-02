//package com.example.alan.fyp.util;
//
//import android.databinding.ObservableList;
//
//import com.example.alan.fyp.model.Post;
//
///**
// * Created by wadealanchan on 2/3/2018.
// */
//
//public class QuestionData {
////    public static ObservableList<Post> posts;
//
//
////    public static void showcontent(String category) {
////
////        CollectionReference query = FirebaseFirestore.getInstance().collection("posts");
////
////        if (category.equals(null)) {
////
////        } else {
////            query.whereEqualTo("category", category);
////        }
////
////        query.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
////                @Override
////                public void onEvent(@Nullable QuerySnapshot value,
////                                    @Nullable FirebaseFirestoreException e) {
////                    if (e != null) {
////                        //Log.w(TAG, "Listen failed.", e);
////                        return;
////                    }
////
////                    posts.clear();
////                    for (DocumentSnapshot document : value) {
////                        if (document.exists()) {
////                            Post post = document.toObject(Post.class);
////                            posts.add(post);
////
////
////                        }
////                    }
////                }
////
////            });
////
////    }
//
//
//
//}
