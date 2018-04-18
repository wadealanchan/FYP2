package com.example.alan.fyp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.devlomi.record_view.OnBasketAnimationEnd;
import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordButton;
import com.example.alan.fyp.ListViewModel.ChatListViewModel2;
import com.example.alan.fyp.R;
import com.example.alan.fyp.databinding.ActivityChat2Binding;
import com.example.alan.fyp.model.Con_Message;
import com.example.alan.fyp.model.Post;
import com.example.alan.fyp.model.Rating;
import com.example.alan.fyp.model.User;
import com.example.alan.fyp.model.model_conversation;
import com.example.alan.fyp.photoeditor.MediaActivity;
import com.example.alan.fyp.photoeditor.PhotoEditorActivity;
import com.example.alan.fyp.viewModel.Con_MessageViewModel;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Chat2 extends MediaActivity
        implements RatingDialogListener {
    ActivityChat2Binding binding;
    FirebaseUser firebaseuser = FirebaseAuth.getInstance().getCurrentUser();
    Con_MessageViewModel con_messageViewModel = new Con_MessageViewModel();
    ChatListViewModel2 chatListViewModel2 = new ChatListViewModel2();
    public final String TAG = "ChatMain2: ";
    @BindView(R.id.Rview_chat2)
    RecyclerView Rview_chat2;
    @BindView(R.id.emojicon_edit_text)
    EditText emojicon_edit_text;
    @BindView(R.id.submit_button)
    ImageView submit_button;
    @BindView(R.id.camera_button)
    ImageView camera_button;
    @BindView(R.id.record_view)
    com.devlomi.record_view.RecordView recordView;
    @BindView(R.id.record_button)
    com.devlomi.record_view.RecordButton recordButton;
    private Context mContext;


    public static final int MY_CHILD_ACTIVITY = 2034;
    @InjectExtra
    String conversationId;
    @InjectExtra
    String targetUserName;
    model_conversation conversation;
    StorageReference storageReference;


    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String mFileName = null;
    private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;

    // private PlayButton   mPlayButton = null;
    private MediaPlayer mPlayer = null;

    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};
    Con_MessageViewModel msgViewModel = new Con_MessageViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storageReference = FirebaseStorage.getInstance().getReference();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat2);
        Dart.inject(this);
        ButterKnife.bind(this);
        setTitle(null);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        audio();
        textWatcher();
        mFileName = getExternalCacheDir().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";
        // ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        //playAudio("");

        FirebaseFirestore.getInstance().collection("conversation")
                .document(conversationId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                conversation = documentSnapshot.toObject(model_conversation.class);

                //Collections.sort(conversation.getMessageList());
                if (conversation.getMessageList().size() > 0) {
                    Con_MessageViewModel msgViewModel = new Con_MessageViewModel();
                    Con_Message msg = conversation.getMessageList().get(conversation.getMessageList().size() - 1);
                    msgViewModel.getMessageText().set(msg.getMessageText());
                    msgViewModel.getDate().set(msg.getDate());
                    msgViewModel.getSenderID().set(msg.getSenderID());
                    msgViewModel.getImageuri().set(msg.getImageuri());
                    msgViewModel.getAudiouri().set(msg.getAudiouri());
                    //chatListViewModel2.items.add(msgViewModel);
                    chatListViewModel2.items.add(0, msgViewModel);
                }
                Rview_chat2.scrollToPosition(0);
            }
        });

        FirebaseFirestore.getInstance().collection("conversation").document(conversationId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        conversation = documentSnapshot.toObject(model_conversation.class);

                        if (conversation.isChatIsOver()) {
                            chatIsOver();
                            Log.d(TAG, "chat is over");
                        }

                        Collections.sort(conversation.getMessageList());

                        for (int i = conversation.getMessageList().size() - 2; i >= 0; i--) {
                            Con_Message msg = conversation.getMessageList().get(i);
                            Con_MessageViewModel msgViewModel = new Con_MessageViewModel();
                            msgViewModel.getMessageText().set(msg.getMessageText());
                            msgViewModel.getDate().set(msg.getDate());
                            msgViewModel.getSenderID().set(msg.getSenderID());
                            msgViewModel.getImageuri().set(msg.getImageuri());
                            msgViewModel.getAudiouri().set(msg.getAudiouri());
                            chatListViewModel2.items.add(msgViewModel);
                        }

                        binding.executePendingBindings();
                    }
                });

        User user = new User();
        user.setName(targetUserName);

        con_messageViewModel.getUser().set(user);


        binding.setChatList(chatListViewModel2);
        binding.setChat(con_messageViewModel);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) finish();

    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        Log.d(LOG_TAG, mFileName + "");
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }




    private void audio() {

        ActivityCompat.requestPermissions(Chat2.this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        recordButton.setRecordView(recordView);




        // if you want to click the button (in case if you want to make the record button a Send Button for example..)
       // recordButton.setListenForRecord(false);

//        btnChangeOnclick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (recordButton.isListenForRecord()) {
//                    recordButton.setListenForRecord(false);
//                    Toast.makeText(MainActivity.this, "onClickEnabled", Toast.LENGTH_SHORT).show();
//                } else {
//                    recordButton.setListenForRecord(true);
//                    Toast.makeText(MainActivity.this, "onClickDisabled", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        //ListenForRecord must be false ,otherwise onClick will not be called
//        recordButton.setOnRecordClickListener(new OnRecordClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(Chat2.this, "RECORD BUTTON CLICKED", Toast.LENGTH_SHORT).show();
//                Log.d("RecordButton", "RECORD BUTTON CLICKED");
//
//                recordButton.setListenForRecord(true);
//
//            }
//        });


        //Cancel Bounds is when the Slide To Cancel text gets before the timer . default is 130
        recordView.setCancelBounds(130);


        recordView.setSmallMicColor(Color.parseColor("#c2185b"));

        //prevent recording under one Second
        recordView.setLessThanSecondAllowed(false);


        recordView.setSlideToCancelText("Slide To Cancel");


        recordView.setCustomSounds(R.raw.record_start, R.raw.record_finished, 0);


        recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {
                Log.d("RecordView", "onStart");
                emojicon_edit_text.setVisibility(View.INVISIBLE);
                camera_button.setVisibility(View.INVISIBLE);
                startRecording();
               // Toast.makeText(Chat2.this, "OnStartRecord", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancel() {
                Toast.makeText(Chat2.this, "onCancel", Toast.LENGTH_SHORT).show();
                emojicon_edit_text.setVisibility(View.VISIBLE);
                camera_button.setVisibility(View.VISIBLE);
                Log.d("RecordView", "onCancel");
                stopRecording();

            }

            @Override
            public void onFinish(long recordTime) {
                emojicon_edit_text.setVisibility(View.VISIBLE);
                camera_button.setVisibility(View.VISIBLE);
                String time = getHumanTimeText(recordTime);
               // Toast.makeText(Chat2.this, "onFinishRecord - Recorded Time is: " + time, Toast.LENGTH_SHORT).show();
                Log.d("RecordView", "onFinish");
                Log.d("RecordTime", time);
                stopRecording();
                File outputfile = new File(mFileName);
                Uri outputfileuri = Uri.fromFile(outputfile);
                uploadfirebase2(outputfileuri);
            }

            @Override
            public void onLessThanSecond() {
                Toast.makeText(Chat2.this, "OnLessThanSecond", Toast.LENGTH_SHORT).show();
                Log.d("RecordView", "onLessThanSecond");
            }
        });


        recordView.setOnBasketAnimationEndListener(new OnBasketAnimationEnd() {
            @Override
            public void onAnimationEnd() {
                Log.d("RecordView", "Basket Animation Finished");
            }
        });

    }

    private void textWatcher() {
        submit_button.setVisibility(View.GONE);
        emojicon_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (emojicon_edit_text.getText().toString().trim().length() >= 1) {
                    submit_button.setVisibility(View.VISIBLE);
                    recordButton.setVisibility(View.INVISIBLE);
                } else {
                    submit_button.setVisibility(View.GONE);
                    recordButton.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private String getHumanTimeText(long milliseconds) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }


    public void userenter(View v) {


        final String messagetext = con_messageViewModel.getMessageText().get();
        if (!TextUtils.isEmpty(messagetext)) {
            Log.d(TAG, "here");

            Con_Message m = new Con_Message();
            m.setDate(new Date());
            m.setMessageText(messagetext);
            m.setSenderID(firebaseuser.getUid());
            conversation.getMessageList().add(m);
            con_messageViewModel.getMessageText().set("");
            FirebaseFirestore.getInstance().collection("conversation").document(conversationId)
                    .set(conversation)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                            Thread thread = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        Log.d(TAG, " get it here?");
                                        URL url = new URL("https://fcm.googleapis.com/fcm/send");
                                        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                                        con.setRequestProperty("Content-Type", "application/json");
                                        con.setRequestProperty("Authorization", "key=AAAA0PvKSIA:APA91bExyJ3gW6LYKhRvYGPjz6vUOKeC8Tc3Po3oP_uP6FqRis07YoBXl0xe9kzreR1McPZNHN-EUSXtPU9gjYHvGcfWde0XL-hegNQlxxRiCNnIV1tpX5NaWe7Mb6Uh1tQuzZL7-tbZ");
                                        con.setRequestMethod("POST");
                                        con.connect();

                                        JSONObject payload = new JSONObject();
                                        payload.put("to", "/topics/" + (conversation.getPostUserId().equals(firebaseuser.getUid()) ? conversation.getAid() : conversation.getPostUserId()));

                                        JSONObject data = new JSONObject();
                                        data.put("conversationId", conversationId);
                                        data.put("targetUserName", firebaseuser.getDisplayName());

                                        payload.put("data", data);

                                        JSONObject notification = new JSONObject();
                                        notification.put("title", firebaseuser.getDisplayName());
                                        notification.put("body", messagetext);
                                        notification.put("sound", "default");
                                        notification.put("click_action", "com.example.alan.fyp.action.CHAT2");
                                        notification.put("icon",getResources().getDrawable(R.drawable.ic_stat_ic_notification));

                                        payload.put("notification", notification);

                                        OutputStream os = con.getOutputStream();
                                        os.write(payload.toString().getBytes("UTF-8"));
                                        os.close();

                                        Log.d(TAG, payload.toString());

                                        // Read the response into a string
                                        InputStream is = con.getInputStream();
                                        String responseString = new Scanner(is, "UTF-8").useDelimiter("\\A").next();
                                        is.close();

                                        // Parse the JSON string and return the notification key
                                        JSONObject response = new JSONObject(responseString);
                                        //return response.getString("notification_key");
                                        Log.d(TAG, responseString);

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            thread.start();


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error updating document", e);
                        }
                    });

        }

    }


    public void getUserInfo(Con_MessageViewModel viewModel, String ID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(ID).get().addOnCompleteListener(userInfotask -> {

            if (userInfotask.isSuccessful()) {
                viewModel.getUser().set(userInfotask.getResult().toObject(User.class));
            } else {
                viewModel.getUser().set(null);
            }

        });
    }


    public void cameraaction(View v) {

        final String OPTION_CAMERA = "Camera";
        final String OPTION_GALLERY = "Gallery";
        final CharSequence cameraTypes[] = new CharSequence[]{OPTION_CAMERA, OPTION_GALLERY};
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        builder.setTitle("Pick a camera");
        builder.setItems(cameraTypes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (cameraTypes[which].equals(OPTION_CAMERA)) {
                    startCameraActivity();
                } else if (cameraTypes[which].equals(OPTION_GALLERY)) {
                    openGallery();
                }
            }
        });
        builder.show();
    }


    @Override
    protected void onPhotoTaken() {
        Intent intent = new Intent(this, PhotoEditorActivity.class);
        intent.putExtra("selectedImagePath", selectedImagePath);
        startActivityForResult(intent, MY_CHILD_ACTIVITY);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (MY_CHILD_ACTIVITY): {
                if (resultCode == Activity.RESULT_OK) {
                    // TODO Extract the data returned from the child Activity.
                    String imagefile = data.getStringExtra("imageUri");
                    uploadfirebase(Uri.parse(imagefile));
                }
                break;
            }
        }
    }

    private void uploadfirebase(Uri imageuri) {
        if (imageuri != null) {
            StorageReference filePath = storageReference.child("conversation_images").child(imageuri.getLastPathSegment());

            filePath.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    Con_Message m = new Con_Message();
                    m.setDate(new Date());
                    m.setImageuri(taskSnapshot.getDownloadUrl().toString());
                    m.setSenderID(firebaseuser.getUid());
                    conversation.getMessageList().add(m);

                    FirebaseFirestore.getInstance().collection("conversation").document(conversationId)
                            .set(conversation)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            })


                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                }
                            });
                }
            });
        }
    }

    private void uploadfirebase2(Uri imageuri) {
        if (imageuri != null) {
            StorageReference filePath = storageReference.child("conversation_audio/"+conversationId+"/").child(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+".3gp");

            filePath.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    Con_Message m = new Con_Message();
                    m.setDate(new Date());
                    m.setAudiouri(taskSnapshot.getDownloadUrl().toString());
                    m.setSenderID(firebaseuser.getUid());
                    conversation.getMessageList().add(m);

                    FirebaseFirestore.getInstance().collection("conversation").document(conversationId)
                            .set(conversation)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            })


                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                }
                            });
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_rate) {
            if (conversation.getPostUserId().equals(firebaseuser.getUid())) {
                showDialog();
            } else {
                Toast.makeText(Chat2.this, "You are not allowed to rate", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        if (id == R.id.action_endsession) {
            alertdialog();
            return true;
        }
        if (id == R.id.action_questiondetail) {
            questionDetail();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void chatIsOver() {
        con_messageViewModel.ChatisOver.set(true);
        emojicon_edit_text.setHint(getString(R.string.the_chat_is_over));
        emojicon_edit_text.setFocusable(false);
        emojicon_edit_text.setClickable(false);
        emojicon_edit_text.setCursorVisible(false);
        emojicon_edit_text.setInputType(InputType.TYPE_NULL);
        emojicon_edit_text.setKeyListener(null);
        submit_button.setClickable(false);
        recordButton.setClickable(false);
        recordButton.setVisibility(View.GONE);
        camera_button.setClickable(false);
    }


    private void questionDetail() {
        Intent i = getIntent();
        Post post = (Post) i.getSerializableExtra("postObject");
        Intent intent = new Intent(this, QuestionDetailActivity.class);
        intent.putExtra("postObject", post);
        startActivity(intent);

    }

    private void updateChatIsOver() {
        FirebaseFirestore.getInstance().collection("conversation").document(conversationId)
                .update("chatIsOver", true).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully updated!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });


    }

    private void alertdialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Chat2.this, R.style.AlertDialogStyle);
        builder.setMessage(getString(R.string.are_you_sure_to_end_chat));
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                chatIsOver();
                updateChatIsOver();
            }
        });
        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    private void showDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNeutralButtonText("Later")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"))
                .setDefaultRating(1)
                .setTitle("Rate the tutor")
                .setDescription("Please select some stars and give your feedback")
                .setDefaultComment("Please write your comment here ...")
                .setStarColor(R.color.starColor)
                .setNoteDescriptionTextColor(R.color.noteDescriptionTextColor)
                .setTitleTextColor(R.color.titleTextColor)
                .setDescriptionTextColor(R.color.descriptionTextColor)
                .setHint("Please write your comment here ...")
                .setHintTextColor(R.color.hintTextColor)
                .setCommentTextColor(R.color.commentTextColor)
                .setCommentBackgroundColor(R.color.colorPrimaryDark3)
                .setWindowAnimation(R.style.MyDialogFadeAnimation)
                .create(Chat2.this)
                .show();
    }

    @Override
    public void onPositiveButtonClicked(int rate, String comment) {
        // interpret results, send it to analytics etc...
        Rating rating = new Rating();
        rating.setNumberOfStar(rate);
        rating.setComment(comment);

        Log.d(TAG, "" + rate + "  " + comment);

        FirebaseFirestore.getInstance().collection("Users").document(conversation.getAid())
                .collection("rating").document(conversation.getPostId()).set(rating).addOnSuccessListener
                (new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Chat2.this, "Rated", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "DocumentSnapshot successfully updated!");

                        finish();
                    }
                });


    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onNeutralButtonClicked() {

    }


    @Override
    protected void onResume() {
        Log.e("TAG", "onresume");
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

//    public void playAudio(String audioUri) {
//
//
//        ImageView mPlayMedia = findViewById(R.id.play);
//        ImageView mPauseMedia = findViewById(R.id.pause);
//        SeekBar mMediaSeekBar = (SeekBar) findViewById(R.id.media_seekbar);
//        TextView mRunTime = (TextView) findViewById(R.id.run_time);
//        TextView mTotalTime = (TextView) findViewById(R.id.total_time);
//
//        audioUri ="https://firebasestorage.googleapis.com/v0/b/alanchanproject.appspot.com/o/conversation_audio%2Faudiorecordtest.3gp?alt=media&token=7284692c-ffa3-4dd7-a5a0-ad266a7c9a20";
//
//        AudioWife.getInstance()
//                .init(mContext, Uri.parse(audioUri))
//                .setPlayView(mPlayMedia)
//                .setPauseView(mPauseMedia)
//                .setSeekBar(mMediaSeekBar)
//                .setRuntimeView(mRunTime)
//                .setTotalTimeView(mTotalTime);
//
//
//        AudioWife.getInstance().addOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                Toast.makeText(getBaseContext(), "Completed", Toast.LENGTH_SHORT).show();
//                // do you stuff.
//            }
//        });
//
//        AudioWife.getInstance().addOnPlayClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getBaseContext(), "Play", Toast.LENGTH_SHORT).show();
//                // get-set-go. Lets dance.
//            }
//        });
//
//        AudioWife.getInstance().addOnPauseClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getBaseContext(), "Pause", Toast.LENGTH_SHORT).show();
//                // Your on audio pause stuff.
//            }
//        });
//    }

}
