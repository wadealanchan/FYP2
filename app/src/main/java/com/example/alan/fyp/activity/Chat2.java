package com.example.alan.fyp.activity;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.alan.fyp.R;
import com.example.alan.fyp.databinding.ActivityChat2Binding;
import com.example.alan.fyp.model.*;
import com.example.alan.fyp.ListViewModel.ChatListViewModel2;
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

import java.util.Collections;
import java.util.Date;

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

    model_conversation conversation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat2);
        Dart.inject(this);
        ButterKnife.bind(this);

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

                    chatListViewModel2.items.clear();

                    Collections.sort(conversation.getMessageList());

                    for (int i=conversation.getMessageList().size()-1 ; i>=0; i--)
                    {
                        Con_Message msg = conversation.getMessageList().get(i);
                        Log.d("chat2 :", msg.getMessageText());
                        Con_MessageViewModel msgViewModel = new Con_MessageViewModel();
                        msgViewModel.getMessageText().set(msg.getMessageText());
                        msgViewModel.getDate().set(msg.getDate());
                        msgViewModel.getSenderID().set(msg.getSenderID());
                        chatListViewModel2.items.add(msgViewModel);
                    }

//                    for(Con_Message msg : conversation.getMessageList()) {
//                        Con_MessageViewModel msgViewModel = new Con_MessageViewModel();
//                        msgViewModel.getMessageText().set(msg.getMessageText());
//                        msgViewModel.getDate().set(msg.getDate());
//                        msgViewModel.getSenderID().set(msg.getSenderID());
//                        chatListViewModel2.items.add(msgViewModel);
//                    }

                    binding.executePendingBindings();
                }
            });

        binding.setChatList(chatListViewModel2);
        binding.setChat(con_messageViewModel);
    }

    @OnClick(R.id.submit_button)
    public void userenter(View v) {

        final String messagetext = con_messageViewModel.getMessageText().get();
        if (!TextUtils.isEmpty(messagetext)  ) {

//            ChatMessage chatMessage = new ChatMessage();


//            Timestamp tsTemp = new Timestamp(System.currentTimeMillis());

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







}
