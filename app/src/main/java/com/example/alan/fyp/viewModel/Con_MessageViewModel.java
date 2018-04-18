package com.example.alan.fyp.viewModel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import com.example.alan.fyp.Henson;
import com.example.alan.fyp.R;
import com.example.alan.fyp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.IOException;



/**
 * Created by wadealanchan on 2/1/2018.
 */

public class Con_MessageViewModel extends BaseObservable {
    private ObservableField<String> messageText = new ObservableField<String>();
    private ObservableField<String> senderID = new ObservableField<String>();
    private ObservableField<java.util.Date> date = new ObservableField<java.util.Date>();
    private ObservableField<String> imageuri = new ObservableField<String>();
    private ObservableField<String> audiouri = new ObservableField<String>();
    private ObservableField<User> user = new ObservableField<User>();
    public ObservableBoolean ChatisOver = new ObservableBoolean(false);
    public ObservableBoolean toggle = new ObservableBoolean(false);
    public MediaPlayer audioPlayer;
    public ObservableInt max = new ObservableInt();
    public ObservableInt current = new ObservableInt();
    private Handler mProgressUpdateHandler = new Handler();
    private static final int AUDIO_PROGRESS_UPDATE_TIME = 100;
    SeekBar seekBar;

    private Runnable mUpdateProgress = new Runnable() {

        public void run() {

            if (mProgressUpdateHandler != null && audioPlayer.isPlaying()) {
                Log.d("conviewmodel",mProgressUpdateHandler+" "+ audioPlayer.getCurrentPosition());

                seekBar.setProgress(audioPlayer.getCurrentPosition());
                int currentTime = audioPlayer.getCurrentPosition();
                // repeat the process
                current.set(currentTime);
                mProgressUpdateHandler.postDelayed(this, AUDIO_PROGRESS_UPDATE_TIME);
            } else {
                // DO NOT update UI if the player is paused
            }
        }
    };


    public ObservableField<String> getMessageText() {
        return messageText;
    }

    public ObservableInt getMax() {
        return max;
    }

    public void setMax(ObservableInt max) {
        this.max = max;
    }

    public ObservableInt getCurrent() {
        return current;
    }

    public void setCurrent(ObservableInt current) {
        this.current = current;
    }


    public Con_MessageViewModel() {
        this.senderID.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Users").document(senderID.get()).get().addOnCompleteListener(userInfotask -> {

                    if (userInfotask.isSuccessful()) {
                        user.set(userInfotask.getResult().toObject(User.class));

                    }
                });
            }
        });

        this.audiouri.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                audioPlayer = new MediaPlayer();
                audioPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        toggle.set(false);
                    }
                });
                audioPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        max.set(mp.getDuration());
                    }
                });


                if (audiouri.get() != null)
                    try {
                        audioPlayer.setDataSource(audiouri.get());
                        audioPlayer.prepareAsync();

                    } catch (IOException e) {
                        //Log.v("AUDIOHTTPPLAYER", e.getMessage());
                    }
            }
        });


    }

    public void setMessageText(ObservableField<String> messageText) {
        this.messageText = messageText;
    }

    public ObservableField<String> getSenderID() {
        return senderID;
    }

    public void setSenderID(ObservableField<String> senderID) {
        this.senderID = senderID;
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

    public ObservableField<String> getAudiouri() {
        return audiouri;
    }

    public void setAudiouri(ObservableField<String> audiouri) {
        this.audiouri = audiouri;
    }


    @BindingAdapter({"bind:imagesrc"})
    public static void loadImage(ImageView view, String image) {
        if (image != null)
            Picasso.with(view.getContext()).load(image).into(view);
        else
            Picasso.with(view.getContext()).load(R.drawable.ic_avatar_default).into(view);
    }


    public void onSaveClick(View view) {
        Intent intent = Henson.with(view.getContext()).gotoImageDetailActivity()
                .imageuri(this.imageuri.get())
                .build();
        view.getContext().startActivity(intent);
    }


    public void playAudio(View view) {


        seekBar = view.getRootView().getRootView().findViewById(this.senderID.get().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) ? R.id.media_seekbar_user : R.id.media_seekbar_friend);

        Log.d("conviewmodel",seekBar+"");
        mProgressUpdateHandler.postDelayed(mUpdateProgress, AUDIO_PROGRESS_UPDATE_TIME);


        audioPlayer.start();

        this.toggle.set(true);


    }

    public void pauseAudio(View view) {

        audioPlayer.pause();

        this.toggle.set(false);

    }

    public void seekTo(SeekBar view, int value, boolean fromUser) {
        if (fromUser)
            audioPlayer.seekTo(value);
    }


    @BindingAdapter("android:progress")
    public static void setProgress(SeekBar view, int progress) {
        if (progress != view.getProgress()) {
            view.setProgress(progress);
        }
    }



    public void toggleReadmore() {
        if (this.toggle.get()) {
            this.toggle.set(false);
        } else {
            this.toggle.set(true);
        }
    }


}
