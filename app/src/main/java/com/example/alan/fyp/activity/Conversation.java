package com.example.alan.fyp.activity;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.alan.fyp.ListViewModel.ConListViewModel;
import com.example.alan.fyp.R;
import com.example.alan.fyp.databinding.ActivityConversationBinding;
import com.example.alan.fyp.model.Con_Message;
import com.example.alan.fyp.model.Post;
import com.example.alan.fyp.model.User;
import com.example.alan.fyp.model.model_conversation;
import com.example.alan.fyp.viewModel.ConViewModel;
import com.example.alan.fyp.viewModel.Con_MessageViewModel;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class Conversation extends BaseActivity {


    public final String TAG = "Conversation: ";
    ActivityConversationBinding binding;
    FirebaseUser firebaseuser = FirebaseAuth.getInstance().getCurrentUser();
    ConListViewModel conListViewModel = new ConListViewModel();
    ConViewModel conViewModel= new ConViewModel();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_conversation);


        FirebaseFirestore.getInstance().collection("conversation")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess( QuerySnapshot documentSnapshots) {
                        for (DocumentSnapshot document : documentSnapshots.getDocuments()) {
                            model_conversation model_conversation = document.toObject(com.example.alan.fyp.model.model_conversation.class);

                            if (model_conversation.getAid().equals(firebaseuser.getUid())) {
                                ConViewModel conViewModel = new ConViewModel();
                                conViewModel.conId = document.getId();
                                getUserInfo(conViewModel, model_conversation.getPostUserId());
                                getPostInfo(conViewModel, model_conversation.getPostId());
                                conListViewModel.items.add(conViewModel);
                            }

                            if (model_conversation.getPostUserId().equals(firebaseuser.getUid())) {
                                ConViewModel conViewModel = new ConViewModel();
                                conViewModel.conId = document.getId();
                                getUserInfo(conViewModel, model_conversation.getAid());
                                getPostInfo(conViewModel, model_conversation.getPostId());
                                conListViewModel.items.add(conViewModel);

                            }

                        }
                        binding.executePendingBindings();
                    }

                });


        binding.setConList(conListViewModel);
        binding.setConversation(conViewModel);

    }



    public void getUserInfo(ConViewModel viewModel, String ID)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(ID).get().addOnCompleteListener(userInfotask -> {

            if (userInfotask.isSuccessful()) {
                viewModel.user.set(userInfotask.getResult().toObject(User.class));
            } else {
                viewModel.user.set(null);
            }

        });
    }

    public void getPostInfo(ConViewModel viewModel, String postid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").document(postid)
        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                viewModel.post.set(documentSnapshot.toObject(Post.class));
            }
        });
    }





}
