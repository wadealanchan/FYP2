package com.example.alan.fyp.viewModel;

import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.example.alan.fyp.model.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

/**
 * Created by wadealanchan on 10/12/2017.
 */

public class ChatViewModel {


    public final ObservableField<String> messagetext = new ObservableField<String>();
    public final ObservableField<String> timestamp = new ObservableField<String>();
    public final ObservableField<User> user = new ObservableField<User>();
    public final ObservableField<User> questioner = new ObservableField<User>();
    public final ObservableField<User> answerer = new ObservableField<User>();
    public final ObservableBoolean isclicked = new ObservableBoolean(false);



    public ChatViewModel()
    {

    }

    public void setMessageUserId(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(userId).get().addOnCompleteListener(userInfotask -> {

            if (userInfotask.isSuccessful()) {
                this.user.set(userInfotask.getResult().toObject(User.class));
            } else {
                this.user.set(null);
            }
        });
    }

//    public void setQuestionerrId(String userId) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("Users").document(userId).get().addOnCompleteListener(userInfotask -> {
//
//            if (userInfotask.isSuccessful()) {
//                this.user.set(userInfotask.getResult().toObject(User.class));
//            } else {
//                this.user.set(null);
//            }
//        });
//    }
//
//    public void setAnswererId(String userId) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("Users").document(userId).get().addOnCompleteListener(userInfotask -> {
//
//            if (userInfotask.isSuccessful()) {
//                this.user.set(userInfotask.getResult().toObject(User.class));
//            } else {
//                this.user.set(null);
//            }
//        });
//    }




    public ChatViewModel(String messagetext, User user, String timestamp)
    {
        this.messagetext.set(messagetext);
        this.timestamp.set(timestamp);
        this.user.set(user);
    }



}
