package com.example.alan.fyp.activity;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.alan.fyp.R;
import com.example.alan.fyp.databinding.ChatMainBinding;
import com.example.alan.fyp.model.ChatMessage;
import com.example.alan.fyp.model.Con_Message;
import com.example.alan.fyp.model.model_conversation;
import com.example.alan.fyp.ListViewModel.ChatListViewModel;
import com.example.alan.fyp.viewModel.ChatViewModel;
import com.example.alan.fyp.viewModel.UserViewModel;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class Chat extends BaseActivity {


    ChatMainBinding binding;
    public final String TAG = "ChatMain: ";
    UserViewModel userViewModel = new UserViewModel();
    ChatViewModel chatViewModel = new ChatViewModel();
    ChatListViewModel chatList = new ChatListViewModel();

    FirebaseUser firebaseuser = FirebaseAuth.getInstance().getCurrentUser();

    model_conversation con;

    @InjectExtra String postId;
    @InjectExtra String questioner;
      String answererId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         binding = DataBindingUtil.setContentView(this, R.layout.chat_main);

        Dart.inject(this);
        ButterKnife.bind(this);
        //answererId = firebaseuser.getUid();

        FirebaseFirestore.getInstance().collection("Chatmessage")
                .whereEqualTo("postID",postId)
                .orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value ,
                                @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                chatList.items.clear();
                for (DocumentSnapshot document : value) {
                    ChatMessage chatMessage = document.toObject(ChatMessage.class);
                    ChatViewModel chatViewModel = chatMessage.toViewModel();
                    if(chatMessage.getAnswerer()!=null) {
                        chatViewModel.setMessageUserId(chatMessage.getAnswerer());
                    }
                    chatList.items.add(chatViewModel);
                    Log.d(TAG, document.getId() + " => " + document.getData());
                }
                binding.executePendingBindings();
            }
        });



        binding.setChatList(chatList);
        binding.setChat(chatViewModel);


    }

    @OnClick(R.id.submit_button)
    public void userenter(View v) {




        final String messagetext = chatViewModel.messagetext.get();
        if (!TextUtils.isEmpty(messagetext)  ) {

//            ChatMessage chatMessage = new ChatMessage();


//            Timestamp tsTemp = new Timestamp(System.currentTimeMillis());

                Con_Message m = new Con_Message();
                m.setDate(new Date());
                m.setMessageText(messagetext);
                m.setSenderID(firebaseuser.getUid());

                FirebaseFirestore.getInstance().collection("conversation")
                    .add(m)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            chatViewModel.messagetext.set("");

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });

//            chatMessage.setMessageDate(tsTemp);
//            chatMessage.setMessageText(messagetext);
//            chatMessage.postID = postId;
//            chatMessage.setAnswerer(answererId);
//            chatMessage.setQuestioner(questioner);
//
//            FirebaseFirestore.getInstance().collection("Chatmessage")
//                    .add(chatMessage)
//                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                        @Override
//                        public void onSuccess(DocumentReference documentReference) {
//                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//                            chatViewModel.messagetext.set("");
//
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.w(TAG, "Error adding document", e);
//                        }
//                    });



        }




    }










}
