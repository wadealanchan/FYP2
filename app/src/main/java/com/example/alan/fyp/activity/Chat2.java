package com.example.alan.fyp.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.alan.fyp.ListViewModel.ChatListViewModel2;
import com.example.alan.fyp.R;
import com.example.alan.fyp.databinding.ActivityChat2Binding;
import com.example.alan.fyp.model.*;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Chat2 extends MediaActivity {
    ActivityChat2Binding binding;
    FirebaseUser firebaseuser = FirebaseAuth.getInstance().getCurrentUser();
    Con_MessageViewModel con_messageViewModel = new Con_MessageViewModel();
    ChatListViewModel2 chatListViewModel2 = new ChatListViewModel2();
    public final String TAG = "ChatMain2: ";
    @BindView(R.id.Rview_chat2)
    RecyclerView Rview_chat2;
    public static final int MY_CHILD_ACTIVITY= 2034;
    @InjectExtra String conversationId;
    @InjectExtra String targetUserName;

    model_conversation conversation;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);
        storageReference = FirebaseStorage.getInstance().getReference();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat2);
        Dart.inject(this);
        ButterKnife.bind(this);
        setTitle(null);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);





         FirebaseFirestore.getInstance().collection("conversation")
                .document(conversationId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                        conversation = documentSnapshot.toObject(model_conversation.class);

                        //Collections.sort(conversation.getMessageList());
                        if (conversation.getMessageList().size() > 0) {
                            Con_MessageViewModel msgViewModel = new Con_MessageViewModel();
                            Con_Message msg = conversation.getMessageList().get(conversation.getMessageList().size()-1);
                            msgViewModel.getMessageText().set(msg.getMessageText());
                            msgViewModel.getDate().set(msg.getDate());
                            msgViewModel.getSenderID().set(msg.getSenderID());
                            msgViewModel.getImageuri().set(msg.getImageuri());
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



                    Collections.sort(conversation.getMessageList());

                    for (int i=conversation.getMessageList().size()-2 ; i>=0; i--)
                    {
                        Con_Message msg = conversation.getMessageList().get(i);
                        Con_MessageViewModel msgViewModel = new Con_MessageViewModel();
                        msgViewModel.getMessageText().set(msg.getMessageText());
                        msgViewModel.getDate().set(msg.getDate());
                        msgViewModel.getSenderID().set(msg.getSenderID());
                        msgViewModel.getImageuri().set(msg.getImageuri());
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

    @OnClick(R.id.submit_button)
    public void userenter(View v) {

        final String messagetext = con_messageViewModel.getMessageText().get();
        if (!TextUtils.isEmpty(messagetext)  ) {



            Con_Message m = new Con_Message();
            m.setDate(new Date());
            m.setMessageText(messagetext);
            m.setSenderID(firebaseuser.getUid());

            conversation.getMessageList().add(m);

            FirebaseFirestore.getInstance().collection("conversation").document(conversationId)
                    .set(conversation)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            con_messageViewModel.getMessageText().set("");
                            Log.d(TAG, "DocumentSnapshot successfully updated!");

                            Thread thread = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        Log.d(TAG," get it here?");
                                        URL url = new URL("https://fcm.googleapis.com/fcm/send");
                                        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                                        con.setRequestProperty("Content-Type", "application/json");
                                        con.setRequestProperty("Authorization", "key=AAAA0PvKSIA:APA91bExyJ3gW6LYKhRvYGPjz6vUOKeC8Tc3Po3oP_uP6FqRis07YoBXl0xe9kzreR1McPZNHN-EUSXtPU9gjYHvGcfWde0XL-hegNQlxxRiCNnIV1tpX5NaWe7Mb6Uh1tQuzZL7-tbZ");
                                        con.setRequestMethod("POST");
                                        con.connect();

                                        JSONObject payload = new JSONObject();
                                        payload.put("to","/topics/"+(conversation.getPostUserId().equals(firebaseuser.getUid()) ? conversation.getAid() : conversation.getPostUserId()));

                                        JSONObject data = new JSONObject();
                                        data.put("conversationId", conversationId);
                                        data.put("targetUserName", firebaseuser.getDisplayName());

                                        payload.put("data", data);

                                        JSONObject notification = new JSONObject();
                                        notification.put("title",firebaseuser.getDisplayName());
                                        notification.put("body", messagetext);
                                        notification.put("click_action", "com.example.alan.fyp.action.CHAT2");

                                        payload.put("notification", notification);

                                        OutputStream os = con.getOutputStream();
                                        os.write(payload.toString().getBytes("UTF-8"));
                                        os.close();

                                        Log.d(TAG,payload.toString());

                                        // Read the response into a string
                                        InputStream is = con.getInputStream();
                                        String responseString = new Scanner(is, "UTF-8").useDelimiter("\\A").next();
                                        is.close();

                                        // Parse the JSON string and return the notification key
                                        JSONObject response = new JSONObject(responseString);
                                        //return response.getString("notification_key");
                                        Log.d(TAG,responseString);

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


    public void getUserInfo(Con_MessageViewModel viewModel, String ID)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(ID).get().addOnCompleteListener(userInfotask -> {

            if (userInfotask.isSuccessful()) {
                viewModel.getUser().set(userInfotask.getResult().toObject(User.class));
            } else {
                viewModel.getUser().set(null);
            }

        });
    }

    @OnClick(R.id.camera_button)
    public void cameraaction(View v)
    {

        final String OPTION_CAMERA = "Camera";
        final String OPTION_GALLERY = "Gallery";
        final CharSequence cameraTypes[] = new CharSequence[]{OPTION_CAMERA, OPTION_GALLERY};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

//    @OnClick(R.id.ImageContentUser)
//    public void ImageContentUser(View view)
//    {
//        final String imageuri = con_messageViewModel.getImageuri().get();
//        action_image(imageuri);
//    }
//
//    @OnClick(R.id.ImageContentFriend)
//    public void ImageContentFriend(View view)
//    {
//        final String imageuri = con_messageViewModel.getImageuri().get();
//        action_image(imageuri);
//    }
//
//    private void action_image(String imageuri)
//    {
//        Intent intent = Henson.with(this).gotoPE_MainActivity()
//                .imageuri(imageuri)
//                .build();
//        startActivity(intent);
//    }


    @Override
    protected void onPhotoTaken() {
        Intent intent = new Intent(this, PhotoEditorActivity.class);
        intent.putExtra("selectedImagePath", selectedImagePath);
        startActivityForResult(intent,MY_CHILD_ACTIVITY);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (MY_CHILD_ACTIVITY) : {
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

                                 }})


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






}
