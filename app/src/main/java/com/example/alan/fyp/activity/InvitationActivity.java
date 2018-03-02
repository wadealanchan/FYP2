package com.example.alan.fyp.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;

import com.example.alan.fyp.ListViewModel.InvitationListViewModel;
import com.example.alan.fyp.R;
import com.example.alan.fyp.databinding.ActivityInvitationBinding;
import com.example.alan.fyp.model.User;
import com.example.alan.fyp.viewModel.ConViewModel;
import com.example.alan.fyp.viewModel.InvitationViewModel;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import butterknife.BindView;
import butterknife.ButterKnife;


public class InvitationActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    InvitationViewModel invitationViewModel = new InvitationViewModel();
    InvitationListViewModel invitationListViewModel = new InvitationListViewModel();
    ActivityInvitationBinding binding;
    FirebaseUser firebaseuser = FirebaseAuth.getInstance().getCurrentUser();
    @InjectExtra String postId;
    @InjectExtra String postUserId;
    @InjectExtra String postDescription;
    @InjectExtra String postTtile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_invitation);
        ButterKnife.bind(this);
        Dart.inject(this);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Log.d("InvitationActivity", this.postId+" "+ this.postUserId);

        toolbar.setNavigationIcon(R.drawable.ic_action_back);


        FirebaseFirestore.getInstance().collection("Users")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        invitationListViewModel.items.clear();


                        for (DocumentSnapshot document : documentSnapshots.getDocuments()) {
                            User user = document.toObject(com.example.alan.fyp.model.User.class);
                            user.id =document.getId();
                            if (!document.getId().equals(firebaseuser.getUid()) && !document.getId().equals(postUserId) && user.getType().equals("tutor") ) {
                                InvitationViewModel invitationViewModel = new InvitationViewModel();
                                invitationViewModel.setPostId(postId);
                                invitationViewModel.setPostUserId(postUserId);
                                invitationViewModel.setPostDescription(postDescription);
                                invitationViewModel.setPostTtile(postTtile);
                                invitationViewModel.user.set(user);
                                invitationListViewModel.items.add(invitationViewModel);

                            }
                        }
                        binding.executePendingBindings();
                    }
                });

        binding.setInvitation(invitationViewModel);
        binding.setInviteList(invitationListViewModel);
    }






}
