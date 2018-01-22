package com.example.alan.fyp.activity;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.alan.fyp.ListViewModel.ChatListViewModel2;
import com.example.alan.fyp.R;
import com.example.alan.fyp.databinding.ActivityChat2Binding;
import com.example.alan.fyp.model.*;
import com.example.alan.fyp.viewModel.ConViewModel;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Chat2 extends BaseActivity {
    ActivityChat2Binding binding;
    FirebaseUser firebaseuser = FirebaseAuth.getInstance().getCurrentUser();
    Con_MessageViewModel con_messageViewModel = new Con_MessageViewModel();
    ChatListViewModel2 chatListViewModel2 = new ChatListViewModel2();
    public final String TAG = "ChatMain2: ";
    @BindView(R.id.Rview_chat2)
    RecyclerView Rview_chat2;

    @InjectExtra String conversationId;
    @InjectExtra String targetUserName;

    model_conversation conversation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);
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







}
