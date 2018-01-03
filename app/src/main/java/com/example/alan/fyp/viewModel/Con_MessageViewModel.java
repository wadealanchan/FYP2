package com.example.alan.fyp.viewModel;

import android.databinding.Observable;
import android.databinding.ObservableField;

import com.example.alan.fyp.model.User;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by wadealanchan on 2/1/2018.
 */

public class Con_MessageViewModel {
    private ObservableField<String> messageText = new ObservableField<String>();
    private ObservableField<String> SenderID = new ObservableField<String>();
    private ObservableField<java.util.Date> date = new ObservableField<java.util.Date>();
    private ObservableField<String> imageuri = new ObservableField<String>();
    private ObservableField<User> user = new ObservableField<User>();

    public ObservableField<String> getMessageText() {
        return messageText;
    }

    public Con_MessageViewModel() {
        this.SenderID.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Users").document(SenderID.get()).get().addOnCompleteListener(userInfotask -> {

                    if (userInfotask.isSuccessful()) {
                        user.set(userInfotask.getResult().toObject(User.class));

                        //postViewModel.user.set(userInfotask.getResult().toObject(User.class));
                    }
                });
            }
        });
    }

    public void setMessageText(ObservableField<String> messageText) {
        this.messageText = messageText;
    }

    public ObservableField<String> getSenderID() {
        return SenderID;
    }

    public void setSenderID(ObservableField<String> senderID) {
        SenderID = senderID;
    }

    public ObservableField<java.util.Date> getDate() {
        return date;
    }

    public void setDate(ObservableField<java.util.Date> date) {
        this.date = date;
    }

    public ObservableField<String> getImageuri() {
        return imageuri;
    }

    public void setImageuri(ObservableField<String> imageuri) {
        this.imageuri = imageuri;
    }

    public ObservableField<User> getUser() {
        return user;
    }

    public void setUser(ObservableField<User> user) {
        this.user = user;
    }
}
